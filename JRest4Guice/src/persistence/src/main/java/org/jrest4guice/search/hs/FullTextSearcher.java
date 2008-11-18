package org.jrest4guice.search.hs;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jrest4guice.client.Page;
import org.jrest4guice.client.Pagination;
import org.jrest4guice.persistence.EntityAble;

public interface FullTextSearcher<PK extends Serializable, E extends EntityAble<PK>> {
	public List<E> dynamicList(final Map<String, Object> parameters);

	public Page<E> dynamicPage(final Pagination pagination,
			final Map<String, Object> parameters);

	public List<E> listAll();

	public List<E> list(final String qname, final Map<String, Object> parameters);

	public List<E> list(final String qname, final Object... parameters);

	public List<E> list(final String qname, final Pagination pagination,
			final Map<String, Object> parameters);

	public List<E> list(final String qname, final Pagination pagination,
			final Object... parameters);

	public List<E> listInvalidAll();
}
