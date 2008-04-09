package org.jrest.dao.jpa;

import java.lang.reflect.Constructor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.StringUtils;
import org.jrest.dao.actions.ActionTemplate;
import org.jrest.dao.annotations.Retrieve;

import com.google.inject.Inject;

public class RetrieveAction extends ActionTemplate<Retrieve, JpaContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0) {
			return null;
		}
		Retrieve retrieve = getAnnotation();
		if (StringUtils.isBlank(retrieve.query()) && StringUtils.isBlank(retrieve.namedQuery())) {
			EntityManager em = getContext().getEntityManager();
			return em.find(getMethodReturnType(), parameters[0]);
		} else {
			Query query = getQuery();
			QueryParameter queryPara = new QueryParameter(parameters, method.getParameterAnnotations());
			fittingQuery(query, queryPara);
			try {
				if (!retrieve.resultClass().equals(void.class) && !retrieve.nativeQuery()) {
					return toProjectionalObject(retrieve.resultClass(), query.getSingleResult());
				} else {
					return query.getSingleResult();
				}
			} catch (NoResultException e) {
				return null;
			}
		}

	}

	/**
	 * 将查询结果转换为投影对象
	 * @param clz 投影对象类型
	 * @param initargs 查询结果
	 * @return
	 */
	protected Object toProjectionalObject(Class<?> clz, Object obj) {
		if (obj.getClass().equals(clz)) {
			return obj;
		}
		Object[] initargs;
		if (obj.getClass().isArray()) {
			initargs = (Object[]) obj;
		} else {
			initargs = new Object[] { obj };
		}
		Class<?> parameterTypes[] = new Class[initargs.length];
		for (int i = 0; i < initargs.length; i++) {
			parameterTypes[i] = initargs[i].getClass();
		}
		Constructor<?> constructor = ConstructorUtils.getAccessibleConstructor(clz, parameterTypes);
		if (constructor == null) {
			String message = "找不到适合的构造方法:" + clz.getName() + "(" + StringUtils.join(parameterTypes, ", ") + ")";
			throw new IllegalArgumentException(message);
		}
		try {
			return constructor.newInstance(initargs);
		} catch (Exception e) {
			// 不应该出现的错误
			log.error("无法创建对象实例", e);
		}
		return null;
	}

	private void fittingQuery(Query query, QueryParameter para) {
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

	private Query getQuery() {
		Retrieve retrieve = getAnnotation();
		EntityManager em = getContext().getEntityManager();
		if (StringUtils.isNotBlank(retrieve.namedQuery())) {
			return em.createNamedQuery(retrieve.namedQuery());
		}
		if (retrieve.nativeQuery()) {
			if (retrieve.resultClass().equals(void.class)) {
				return em.createNativeQuery(retrieve.query());
			} else {
				return em.createNativeQuery(retrieve.query(), retrieve.resultClass());
			}
		} else {
			return em.createQuery(retrieve.query());
		}
	}

	@Override
	protected void initialize() {
		annotationClass = Retrieve.class;
		contextClass = JpaContext.class;
	}

	@Inject
	@Override
	public void setContext(JpaContext context) {
		this.context = context;
	}

}
