package org.jrest4guice.search.hs.store;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.store.FSDirectory;
import org.hibernate.HibernateException;
import org.hibernate.search.SearchException;
import org.hibernate.search.backend.LuceneIndexingParameters;
import org.hibernate.search.engine.SearchFactoryImplementor;
import org.hibernate.search.store.DirectoryProvider;
import org.hibernate.search.store.FSDirectoryProvider;
import org.hibernate.search.store.optimization.IncrementalOptimizerStrategy;
import org.hibernate.search.store.optimization.NoOpOptimizerStrategy;
import org.hibernate.search.store.optimization.OptimizerStrategy;
import org.hibernate.search.util.DirectoryProviderHelper;
import org.hibernate.util.StringHelper;

/**
 * 
 * @author <a href="mailto:zhyhongyuan@gmail.com">jerry</a>
 *
 */
@SuppressWarnings("unused")
public class DynamicFSDirectoryProvider extends FSDirectoryProvider{
	private static Log log = LogFactory.getLog( FSDirectoryProvider.class );
	private static String LUCENE_PREFIX = "hibernate.search.";
	private static String LUCENE_DEFAULT = LUCENE_PREFIX + "default.";
	private static final String MERGE_FACTOR = "merge_factor";
	private static final String MAX_MERGE_DOCS = "max_merge_docs";
	private static final String MAX_BUFFERED_DOCS = "max_buffered_docs";
	private static final String BATCH = "batch.";
	private static final String TRANSACTION = "transaction.";
	private static final String SHARDING_STRATEGY = "sharding_strategy";
	private static final String NBR_OF_SHARDS = SHARDING_STRATEGY + ".nbr_of_shards";
	
	private FSDirectory directory;
	private String indexName;
	private Properties indexProps; 
	private SearchFactoryImplementor searchFactoryImplementor;
	
	public DynamicFSDirectoryProvider(){}
	
	public DynamicFSDirectoryProvider(String indexName,Properties indexProps, SearchFactoryImplementor searchFactoryImplementor){
		this.indexName=indexName;
		try {
			directory = FSDirectory.getDirectory( indexName );
		} catch (IOException e) {
			throw new HibernateException( "Unable to initialize index: " + indexName, e );
		}
		configureOptimizerStrategy(searchFactoryImplementor, indexProps, this);
		configureIndexingParameters(searchFactoryImplementor, indexProps, this);
		if ( !searchFactoryImplementor.getLockableDirectoryProviders().containsKey( this ) ) {
			searchFactoryImplementor.getLockableDirectoryProviders().put( this, new ReentrantLock() );
		}
	}
	
	public FSDirectory getDirectory() {
		return directory;
	}

	public void initialize(String directoryProviderName, Properties properties,
			SearchFactoryImplementor searchFactoryImplementor) {
		this.indexProps=properties;
		this.searchFactoryImplementor=searchFactoryImplementor;
		File indexDir = DirectoryProviderHelper.determineIndexDir( directoryProviderName, properties );
		try {
			boolean create = !indexDir.exists();
			if (create) {
				log.debug( "index directory not found, creating: '" + indexDir.getAbsolutePath() + "'" );
				try {
					indexDir.mkdirs();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			indexName = indexDir.getCanonicalPath();
			directory = FSDirectory.getDirectory( indexName );
		}
		catch (IOException e) {
			throw new HibernateException( "Unable to initialize index: " + directoryProviderName, e );
		}

	}

	public void start() {
		super.start();
	}

	@Override
	public boolean equals(Object obj) {
		if ( obj == this ) return true;
		if ( obj == null || !( obj instanceof DynamicFSDirectoryProvider ) ) return false;
		return indexName.equals( ( (DynamicFSDirectoryProvider) obj ).indexName );
	}

	@Override
	public int hashCode() {
		int hash = 11;
		return 37 * hash + indexName.hashCode();
	}

	public Properties getIndexProps() {
		return indexProps;
	}

	public SearchFactoryImplementor getSearchFactoryImplementor() {
		return searchFactoryImplementor;
	}

	private void configureOptimizerStrategy(SearchFactoryImplementor searchFactoryImplementor, Properties indexProps, DirectoryProvider<?> provider) {
		boolean incremental = indexProps.containsKey( "optimizer.operation_limit.max" )
				|| indexProps.containsKey( "optimizer.transaction_limit.max" );
		OptimizerStrategy optimizerStrategy;
		if (incremental) {
			optimizerStrategy = new IncrementalOptimizerStrategy();
			optimizerStrategy.initialize( provider, indexProps, searchFactoryImplementor);
		}
		else {
			optimizerStrategy = new NoOpOptimizerStrategy();
		}
		searchFactoryImplementor.addOptimizerStrategy(provider, optimizerStrategy);
	}
	
	private void configureIndexingParameters(SearchFactoryImplementor searchFactoryImplementor, Properties indexProps, DirectoryProvider<?> provider) {
		
		LuceneIndexingParameters indexingParams = new LuceneIndexingParameters();
		String s = indexProps.getProperty(TRANSACTION + MERGE_FACTOR);
		
		if (!StringHelper.isEmpty( s )) {
			try{
				indexingParams.setTransactionMergeFactor(Integer.valueOf(s));
				indexingParams.setBatchMergeFactor(Integer.valueOf(s));
			} catch (NumberFormatException ne) {
				throw new SearchException("Invalid value for " + TRANSACTION + MERGE_FACTOR + ": " + s);
			}
		}

		s = indexProps.getProperty(TRANSACTION + MAX_MERGE_DOCS);
		if (!StringHelper.isEmpty( s )) {
			try{
				indexingParams.setTransactionMaxMergeDocs(Integer.valueOf(s));
				indexingParams.setBatchMaxMergeDocs(Integer.valueOf(s));
			} catch (NumberFormatException ne) {
				throw new SearchException("Invalid value for " + TRANSACTION + MAX_MERGE_DOCS + ": " + s);
			}
		}
		
		s = indexProps.getProperty(TRANSACTION + MAX_BUFFERED_DOCS);
		if (!StringHelper.isEmpty( s )) {
			try{
				indexingParams.setTransactionMaxBufferedDocs(Integer.valueOf(s));
				indexingParams.setBatchMaxBufferedDocs(Integer.valueOf(s));
			} catch (NumberFormatException ne) {
				throw new SearchException("Invalid value for " + TRANSACTION + MAX_BUFFERED_DOCS + ": " + s);
			}
		}		
				
		s = indexProps.getProperty(BATCH + MERGE_FACTOR);
		if (!StringHelper.isEmpty( s )) {
			try{
				indexingParams.setBatchMergeFactor(Integer.valueOf(s));
			} catch (NumberFormatException ne) {
				throw new SearchException("Invalid value for " + BATCH + MERGE_FACTOR + ": " + s);
			}
		}
		
		s = indexProps.getProperty(BATCH + MAX_MERGE_DOCS);
		if (!StringHelper.isEmpty( s )) {
			try{
				indexingParams.setBatchMaxMergeDocs(Integer.valueOf(s));
			} catch (NumberFormatException ne) {
				throw new SearchException("Invalid value for " + BATCH + MAX_MERGE_DOCS + ": " + s);
			}
		}
		
		s = indexProps.getProperty(BATCH + MAX_BUFFERED_DOCS);
		if (!StringHelper.isEmpty( s )) {
			try{
				indexingParams.setBatchMaxBufferedDocs(Integer.valueOf(s));
			} catch (NumberFormatException ne) {
				throw new SearchException("Invalid value for " + BATCH + MAX_BUFFERED_DOCS + ": " + s);
			}
		}	
		searchFactoryImplementor.addIndexingParmeters(provider, indexingParams);
	}

	public String getIndexName() {
		return indexName;
	}
}
