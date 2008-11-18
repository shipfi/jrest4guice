package org.jrest4guice.search.hs;

import java.io.Serializable;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.jrest4guice.client.Page;
import org.jrest4guice.client.Pagination;
import org.jrest4guice.persistence.EntityAble;

@SuppressWarnings("unchecked")
class JpaFullTextSearcher<PK extends Serializable, E extends EntityAble<PK>>
		implements FullTextSearcher<PK, E> {
	/** 实体类型 */
	private final Class<E> type;
	/** 全文实体管理器 */
	private final FullTextEntityManager fem;

	public JpaFullTextSearcher(final Class<E> type,
			final FullTextEntityManager fem) {
		this.type = type;
		this.fem = fem;
	}

	public List<E> list(String search, String... fields) {
		return this.list(search, new StandardAnalyzer(), fields);
	}

	public List<E> list(String search, Analyzer analyzer, String... fields) {
		Query query = this.buildQuery(search, fields);
		if (query != null)
			return this.list(query);
		else
			return null;
	}

	public List<E> list(Query query) {
		FullTextQuery fQuery = this.fem.createFullTextQuery(query, this.type);
		// 查询结果
		return fQuery.getResultList();
	}

	public Page<E> page(Pagination pagination, String search,
			Analyzer analyzer, String... fields) {
		Query query = this.buildQuery(search, fields);

		if (query != null)
			return this.page(pagination, query);
		else
			return null;
	}

	public Page<E> page(Pagination pagination, String search, String... fields) {
		return this.page(pagination, search, new StandardAnalyzer(), fields);
	}

	private Query buildQuery(String search, String... fields) {
		Query query = null;
		try {
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fields,
					new StandardAnalyzer());
			if (search != null && !search.trim().equals("")) {
				StringBuffer cds = new StringBuffer();
				// 分词并构造组合查询条件
				final String[] tmp_conditions = search.split(" ");
				int index = 0;
				for (final String c : tmp_conditions) {
					if (c.equals("")) {
						continue;
					}
					if (index > 0) {
						cds.append(" AND ");
					}
					cds.append("(" + c + ")");
					index++;
				}
				query = parser.parse(cds.toString());

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return query;
	}

	public Page<E> page(Pagination pagination, Query query) {
		if (pagination == null)
			throw new RuntimeException("分页对象不能为空");

		FullTextQuery fQuery = this.fem.createFullTextQuery(query, this.type);
		// 结果总数
		int size = fQuery.getResultSize();

		// 分页处理
		fQuery.setFirstResult(pagination.getFirstResult());
		fQuery.setMaxResults(pagination.getMaxResults());

		// 查询结果
		List<E> resultList = fQuery.getResultList();
		// 生成分页对象
		Page<E> page = new Page<E>(pagination.getFirstResult(), size,
				pagination.getPageSize(), resultList);
		return page;
	}
}
