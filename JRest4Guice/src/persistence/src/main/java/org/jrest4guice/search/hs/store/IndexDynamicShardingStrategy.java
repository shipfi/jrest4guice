package org.jrest4guice.search.hs.store;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.hibernate.HibernateException;
import org.hibernate.search.annotations.ShardKey;
import org.hibernate.search.reader.ReaderProvider;
import org.hibernate.search.reader.SharedReaderProvider;
import org.hibernate.search.store.DirectoryProvider;
import org.hibernate.search.store.IndexShardingStrategy;

/**
 * 
 * @author <a href="mailto:zhyhongyuan@gmail.com">jerry</a>
 *
 */
@SuppressWarnings("unchecked")
public class IndexDynamicShardingStrategy implements IndexShardingStrategy {
	private static Log log = LogFactory
			.getLog(IndexDynamicShardingStrategy.class);
	private DirectoryProvider[] providers;
	private HashMap<String, DirectoryProvider> providerMap = new HashMap<String, DirectoryProvider>();

	// 分割的关键字
	private Map<Class, String> shardKeyMap;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.search.store.IndexShardingStrategy#initialize(java.util
	 * .Properties, org.hibernate.search.store.DirectoryProvider[])
	 */
	public void initialize(Properties properties, DirectoryProvider[] providers) {
		this.providers = providers;
		this.localDirectoryToMap(null);
		this.shardKeyMap = new HashMap<Class, String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.hibernate.search.store.IndexShardingStrategy#
	 * getDirectoryProvidersForAllShards()
	 */
	public DirectoryProvider[] getDirectoryProvidersForAllShards() {
		return mapToArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.hibernate.search.store.IndexShardingStrategy#
	 * getDirectoryProviderForAddition(java.lang.Class, java.io.Serializable,
	 * java.lang.String, org.apache.lucene.document.Document)
	 */
	public DirectoryProvider getDirectoryProviderForAddition(Class entity,
			Serializable id, String idInString, Document document) {
		return localDirectoryToMap(this.docKey(entity, document));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.hibernate.search.store.IndexShardingStrategy#
	 * getDirectoryProvidersForDeletion(java.lang.Class, java.io.Serializable,
	 * java.lang.String, org.apache.lucene.document.Document)
	 */
	public DirectoryProvider[] getDirectoryProvidersForDeletion(Class entity,
			Serializable id, String idInString, Document document) {
		String key = this.docKey(entity, document);
		if (key == null || key.trim().equals(""))
			return mapToArray();
		return new DirectoryProvider[] { localDirectoryToMap(key) };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.hibernate.search.store.IndexShardingStrategy#
	 * getDirectoryProvidersForSearch(java.lang.Class,
	 * org.apache.lucene.search.Query)
	 */
	public DirectoryProvider[] getDirectoryProvidersForSearch(Class entity,
			Query query) {
		String key = this.docKey(entity, query);
		if (key == null || key.trim().equals(""))
			return mapToArray();
		return new DirectoryProvider[] { localDirectoryToMap(key) };
	}

	private DirectoryProvider[] mapToArray() {
		int size = providerMap.size();
		Collection<DirectoryProvider> dps = providerMap.values();
		return dps.toArray(new DirectoryProvider[size]);
	}

	private DirectoryProvider localDirectoryToMap(String key) {
		DynamicFSDirectoryProvider localdp = null;
		if (providers[0] instanceof DynamicFSDirectoryProvider) {
			localdp = (DynamicFSDirectoryProvider) providers[0];
		}
		
		if(localdp == null)
			return null;
		
		FSDirectory dir = localdp.getDirectory();
		File indexRoot = dir.getFile();
		if (key == null) {
			File[] subIndex = indexRoot.listFiles();
			for (File sub : subIndex) {
				localDirectoryToMap(sub.getName());
			}
			return localdp;
		}
		DirectoryProvider dp = providerMap.get(key);
		if (dp != null)
			return dp;
		File indexFile = new File(indexRoot, key);
		String indexName = "";
		boolean create = !indexFile.exists();
		FSDirectory directory = null;
		try {
			indexName = indexFile.getCanonicalPath();
			directory = FSDirectory.getDirectory(indexName);
			if (create) {
				log.debug("Initialize index: '" + indexFile + "'");
				IndexWriter iw = new IndexWriter(directory,
						new StandardAnalyzer(), create);
				iw.close();
			}
		} catch (IOException e) {
			throw new HibernateException("Unable to initialize index: "
					+ indexFile, e);
		}
		dp = new DynamicFSDirectoryProvider(indexName, localdp.getIndexProps(),
				localdp.getSearchFactoryImplementor());
		ReaderProvider rp = localdp.getSearchFactoryImplementor()
				.getReaderProvider();
		if (rp instanceof SharedReaderProvider)
			((SharedReaderProvider) rp).addLock(dp);
		providerMap.put(key, dp);
		return dp;
	}

	private String docKey(Class entity, Document doc) {
		if (doc == null)
			return null;
		return doc.get(this.getShardKey(entity));
	}

	private String docKey(Class entity, Query query) {
		String sid = null;
		HashSet<Term> terms = new HashSet<Term>();
		query.extractTerms(terms);
		for (Term tr : terms) {
			if (tr.field().equals(this.getShardKey(entity))) {
				sid = tr.text();
				return sid;
			}
		}
		return sid;
	}

	private String getShardKey(Class entity) {
		String shardKey = this.shardKeyMap.get(entity);

		if (shardKey == null) {
			Field[] declaredFields = entity.getDeclaredFields();
			for (Field f : declaredFields) {
				if (f.isAnnotationPresent(ShardKey.class)) {
					shardKey = f.getName();

					this.shardKeyMap.put(entity, shardKey);
					break;
				}
			}
		}

		return shardKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.hibernate.search.store.IndexShardingStrategy#
	 * getDirectoryProvidersForDeletion(java.lang.Class, java.io.Serializable,
	 * java.lang.String)
	 */
	public DirectoryProvider[] getDirectoryProvidersForDeletion(Class entity,
			Serializable id, String idInString) {
		return null;
	}
}
