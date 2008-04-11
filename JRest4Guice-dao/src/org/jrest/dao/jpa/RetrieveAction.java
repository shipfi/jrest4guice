package org.jrest.dao.jpa;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.StringUtils;
import org.jrest.core.util.Assert;
import org.jrest.dao.actions.ActionTemplate;
import org.jrest.dao.annotations.Retrieve;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class RetrieveAction extends ActionTemplate<Retrieve, JpaContext> {
	
	@Override
	public Object execute(Object[] parameters) {
		Assert.notEmpty(parameters, "查询参数不能为空");
		try {
			if (hasQuerySetting())
				return queryResult(parameters);
			else
				return propertyResult(parameters);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * 获取属性查询方式的返回结果
	 * @param parameters 参数
	 * @return
	 */
	private Object propertyResult(Object[] parameters) {
		EntityManager em = getContext().getEntityManager();
		Annotation[][] annos = method.getParameterAnnotations();
		Named named = null;
		for (Annotation a : annos[0]) {
			final Class<? extends Annotation> clazz = a.annotationType();
			if (clazz.equals(Named.class)) {
				named = (Named) a;
			}
		}
		if (named == null)
			return em.find(getMethodReturnType(), parameters[0]);
		else {
			String ejbql = "from " + getMethodReturnType().getSimpleName() + " entity where entity." + named.value()
			        + "=?";
			Query query = em.createQuery(ejbql);
			query.setParameter(1, parameters[0]);
			return query.getSingleResult();
		}
	}
	
	/**
	 * 获取查询方式的返回结果
	 * @param parameters 参数
	 * @return
	 */
	private Object queryResult(Object[] parameters) {
		Retrieve retrieve = getAnnotation();
		Query query = getQuery();
		QueryParameter queryPara = new QueryParameter(parameters, method.getParameterAnnotations());
		QueryUtils.fittingQuery(query, queryPara);
		if (!retrieve.resultClass().equals(void.class) && !retrieve.nativeQuery())
			return toProjectionalObject(retrieve.resultClass(), query.getSingleResult());
		else
			return query.getSingleResult();
	}
	
	/**
	 * 检查是否有查询设置，有返回<code>true</code>，没有返回<code>false</code>
	 * @return
	 */
	private boolean hasQuerySetting() {
		Retrieve retrieve = getAnnotation();
		if (StringUtils.isNotBlank(retrieve.query()))
			return true;
		if (StringUtils.isNotBlank(retrieve.namedQuery()))
			return true;
		return false;
	}
	
	/**
	 * 将查询结果转换为投影对象
	 * @param clz 投影对象类型
	 * @param initargs 查询结果
	 * @return
	 */
	private Object toProjectionalObject(Class<?> clz, Object obj) {
		if (obj.getClass().equals(clz))
			return obj;
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
			return null;
		}
	}
	
	private Query getQuery() {
		Retrieve retrieve = getAnnotation();
		EntityManager em = getContext().getEntityManager();
		if (StringUtils.isNotBlank(retrieve.namedQuery()))
			return em.createNamedQuery(retrieve.namedQuery());
		if (retrieve.nativeQuery()) {
			if (retrieve.resultClass().equals(void.class))
				return em.createNativeQuery(retrieve.query());
			else
				return em.createNativeQuery(retrieve.query(), retrieve.resultClass());
		} else
			return em.createQuery(retrieve.query());
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
