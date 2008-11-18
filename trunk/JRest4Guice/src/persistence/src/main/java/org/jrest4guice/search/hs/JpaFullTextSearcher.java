package org.jrest4guice.search.hs;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.jrest4guice.client.Page;
import org.jrest4guice.client.Pagination;
import org.jrest4guice.persistence.EntityAble;

class JpaFullTextSearcher<PK extends Serializable, E extends EntityAble<PK>> implements FullTextSearcher<PK, E> {
	/** 实体类型 */
	private final Class<E> type;
	/** 全文实体管理器 */
	private final FullTextEntityManager fem;

	public JpaFullTextSearcher(final Class<E> type, final FullTextEntityManager fem) {
		this.type = type;
		this.fem = fem;
	}

	@Override
	public List<E> dynamicList(Map<String, Object> parameters) {
		return null;
	}

	@Override
	public Page<E> dynamicPage(Pagination pagination,
			Map<String, Object> parameters) {
		return null;
	}

	@Override
	public List<E> list(String qname, Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> list(String qname, Object... parameters) {
		return null;
	}

	@Override
	public List<E> list(String qname, Pagination pagination,
			Map<String, Object> parameters) {
		return null;
	}

	@Override
	public List<E> list(String qname, Pagination pagination,
			Object... parameters) {
		return null;
	}

	@Override
	public List<E> listAll() {
		return null;
	}

	@Override
	public List<E> listInvalidAll() {
		return null;
	}
}
