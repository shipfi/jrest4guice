// $Id: NotShardedStrategy.java 14012 2007-09-16 19:57:36Z hardy.ferentschik $
package org.hibernate.search.store;

import java.io.Serializable;
import java.util.Properties;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.hibernate.annotations.common.AssertionFailure;

/**
 * @author Emmanuel Bernard
 */
@SuppressWarnings("unchecked")
public class NotShardedStrategy implements IndexShardingStrategy {
	private DirectoryProvider[] directoryProvider;
	public void initialize(Properties properties, DirectoryProvider[] providers) {
		this.directoryProvider = providers;
		if ( directoryProvider.length > 1) {
			throw new AssertionFailure("Using SingleDirectoryProviderSelectionStrategy with multiple DirectryProviders");
		}
	}

	public DirectoryProvider[] getDirectoryProvidersForAllShards() {
		return directoryProvider;
	}

	public DirectoryProvider getDirectoryProviderForAddition(Class entity, Serializable id, String idInString, Document document) {
		return directoryProvider[0];
	}

	public DirectoryProvider[] getDirectoryProvidersForDeletion(Class entity, Serializable id, String idInString) {
		return directoryProvider;
	}

	@Override
	public DirectoryProvider[] getDirectoryProvidersForDeletion(Class entity,
			Serializable id, String idInString, Document document) {
		return directoryProvider;
	}

	@Override
	public DirectoryProvider[] getDirectoryProvidersForSearch(Class entity,
			Query query) {
		return directoryProvider;
	}
}
