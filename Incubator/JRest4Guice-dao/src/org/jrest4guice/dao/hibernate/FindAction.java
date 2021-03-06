package org.jrest4guice.dao.hibernate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jrest4guice.dao.actions.ActionTemplate;
import org.jrest4guice.dao.annotations.Find;
import org.jrest4guice.dao.annotations.Find.FirstResult;
import org.jrest4guice.dao.annotations.Find.MaxResults;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class FindAction extends ActionTemplate<Find, HibernateContext> {

	protected final static Log log = LogFactory.getLog(FindAction.class);

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object[] parameters) {
		Query query = getQuery();
		QueryParameters queryPara = new QueryParameters(parameters, method.getParameterAnnotations());
		fittingQuery(query, queryPara);
		Find find = getAnnotation();
		if (!find.resultClass().equals(void.class))
			return toProjectionalList(find.resultClass(), query.list());
		return query.list();
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
		if (StringUtils.isNotBlank(find.namedQuery()))
			return getSession().getNamedQuery(find.namedQuery());
		if (find.nativeQuery())
			return getSession().createSQLQuery(find.query());
		else
			return getSession().createQuery(find.query());
	}

	private Session getSession() {
		return getContext().getSession();
	}

	@Override
	protected void initialize() {
		annotationClass = Find.class;
		contextClass = HibernateContext.class;
	}

	@Inject
	@Override
	public void setContext(HibernateContext context) {
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
					getPositionParameters().put(index, para);
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
				getPositionParameters().put(index, para);
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
