package org.jrest4guice.dao.jpa;

import javax.persistence.Query;

/**
 * 查询对象处理 工具类
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
public abstract class QueryUtils {
	
	/**
	 * 配置查询对象
	 * @param query 查询对象
	 * @param para 查询参数对象
	 */
	public static void fittingQuery(Query query, QueryParameter para) {
		if (para.hasNamedParameters()) {
			for (String name : para.getNamedParameters().keySet()) {
				query.setParameter(name, para.getNamedParameters().get(name));
			}
		}
		if (para.hasPositionParameters()) {
			for (Integer index : para.getPositionParameters().keySet()) {
				query.setParameter(index, para.getPositionParameters().get(index));
			}
		}
		if (para.getFirstResult() != null) {
			query.setFirstResult(para.getFirstResult());
		}
		if (para.getMaxResults() != null) {
			query.setMaxResults(para.getMaxResults());
		}
	}
	
}
