package org.jrest.dao.jpa;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Find;
import org.jrest.dao.annotations.Find.FirstResult;
import org.jrest.dao.annotations.Find.MaxResults;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class FindAction extends AbstractAction<Find, JpaContext> {

	protected final static Log log = LogFactory.getLog(FindAction.class);

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object[] parameters) {
		Query query = getQuery();
		QueryParameters queryPara = new QueryParameters(parameters, method.getParameterAnnotations());
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

	private void fittingQuery(Query query, QueryParameters para) {
		if (para.namedParameters != null) {
			for (String name : para.namedParameters.keySet()) {
				query.setParameter(name, para.namedParameters.get(name));
			}
		}
		if (para.positionParameters != null) {
			for (Integer index : para.positionParameters.keySet()) {
				query.setParameter(index, para.positionParameters.get(index));
			}
		}
		if (para.firstResult != null) {
			query.setFirstResult(para.firstResult);
		}
		if (para.maxResults != null) {
			query.setMaxResults(para.maxResults);
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

	class QueryParameters {

		Annotation[][] annotations;
		Object[] parameters;
		Integer firstResult;
		Integer maxResults;
		Map<String, Object> namedParameters;
		Map<Integer, Object> positionParameters;

		QueryParameters(Object[] parameters, Annotation[][] annotations) {
			this.annotations = annotations;
			this.parameters = parameters;
			if (parameters == null)
				return;
			label1: for (int index = 0; index < parameters.length; index++) {
				final Object para = parameters[index];
				if (this.annotations == null) {
					getPositionParameters().put(index + 1, para);
					continue;
				}
				for (final Annotation annotation : annotations[index]) {
					final Class<? extends Annotation> clazz = annotation.annotationType();
					if (clazz.equals(Named.class)) {
						final Named named = (Named) annotation;
						getNamedParameters().put(named.value(), para);
						continue label1;
					} else if (clazz.equals(FirstResult.class)) {
						firstResult = (Integer) para;
						continue label1;
					} else if (clazz.equals(MaxResults.class)) {
						maxResults = (Integer) para;
						continue label1;
					}
				}
				getPositionParameters().put(index + 1, para);
			}
		}

		public Map<String, Object> getNamedParameters() {
			if (namedParameters == null)
				namedParameters = new HashMap<String, Object>();
			return namedParameters;
		}

		public Map<Integer, Object> getPositionParameters() {
			if (positionParameters == null)
				positionParameters = new HashMap<Integer, Object>();
			return positionParameters;
		}
	}
}
