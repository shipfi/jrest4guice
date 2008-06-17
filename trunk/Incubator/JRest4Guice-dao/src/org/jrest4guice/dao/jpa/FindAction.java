package org.jrest4guice.dao.jpa;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest4guice.dao.actions.ActionTemplate;
import org.jrest4guice.dao.annotations.Find;

import com.google.inject.Inject;

public class FindAction extends ActionTemplate<Find, JpaContext> {
	
	protected final static Log log = LogFactory.getLog(FindAction.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object[] parameters) {
		Query query = getQuery();
		QueryParameter queryPara = new QueryParameter(parameters, method.getParameterAnnotations());
		fittingQuery(query, queryPara);
		Find find = getAnnotation();
		if (!find.resultClass().equals(void.class) && !find.nativeQuery())
			return toProjectionalList(find.resultClass(), query.getResultList());
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	protected List toProjectionalList(Class<?> clz, List<Object[]> records) {
		List result = new ArrayList();
		if (records.size() == 0)
			return result;
		Object[] args = records.get(0);
		Class parameterTypes[] = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			parameterTypes[i] = args[i].getClass();
		}
		Constructor constructor = ConstructorUtils.getAccessibleConstructor(clz, parameterTypes);
		if (constructor == null) {
			String message = "找不到适合的构造方法:" + clz.getName() + "(" + StringUtils.join(parameterTypes, ", ") + ")";
			log.error(message);
			throw new IllegalArgumentException(message);
		}
		for (Object[] parameters : records) {
			try {
				result.add(constructor.newInstance(parameters));
			} catch (Exception e) {
				log.error("无法构建投影类型对象", e.getCause()); // 不应该出现该错误
			}
		}
		return result;
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
		Find find = getAnnotation();
		EntityManager em = getContext().getEntityManager();
		if (StringUtils.isNotBlank(find.namedQuery()))
			return em.createNamedQuery(find.namedQuery());
		if (find.nativeQuery()) {
			if (find.resultClass().equals(void.class))
				return em.createNativeQuery(find.query());
			else
				return em.createNativeQuery(find.query(), find.resultClass());
		} else
			return em.createQuery(find.query());
	}
	
	@Override
	protected void initialize() {
		annotationClass = Find.class;
		contextClass = JpaContext.class;
	}
	
	@Inject
	@Override
	public void setContext(JpaContext context) {
		this.context = context;
	}
	
}
