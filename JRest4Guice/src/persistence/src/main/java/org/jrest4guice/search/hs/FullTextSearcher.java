package org.jrest4guice.search.hs;

import java.io.Serializable;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.jrest4guice.client.Page;
import org.jrest4guice.client.Pagination;
import org.jrest4guice.persistence.EntityAble;

import com.google.inject.ProvidedBy;

@ProvidedBy(FullTextSearcherProvider.class)
public interface FullTextSearcher<PK extends Serializable, E extends EntityAble<PK>> {
	public List<E> list(String search, String... fields);

	public List<E> list(String search, Analyzer analyzer, String... fields);

	public Page<E> page(Pagination pagination, String search, String... fields);

	public Page<E> page(Pagination pagination, String search,
			Analyzer analyzer, String... fields);

	public List<E> list(Query query);

	public Page<E> page(Pagination pagination, Query query);
}
