package org.jrest.dao.hibernate;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Find;
import org.jrest.dao.annotations.Find.FirstResult;
import org.jrest.dao.annotations.Find.MaxResults;
import org.jrest.dao.annotations.Find.Named;

public class FindAction extends AbstractAction<Find, HibernateDaoContext> {
	
	@Override
	public Object execute(Object... parameters) {
		Query query = getQuery();
		QueryParameters queryPara = new QueryParameters(parameters, method.getParameterAnnotations());
		fittingQuery(query, queryPara);
		return query.list();
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
		Find find = this.getAnnotation();
		if (StringUtils.isNotBlank(find.namedQuery())) {
			return this.getSession().getNamedQuery(find.namedQuery());
		}
		if (find.nativeQuery()) {
			return this.getSession().createSQLQuery(find.query());
		} else {
			return this.getSession().createQuery(find.query());
		}
	}
	
	private Session getSession() {
		return this.getContext().getSession();
	}

	@Override
	protected void initialize() {
		this.annotationClass = Find.class;
		this.contextClass = HibernateDaoContext.class;
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
			label1:
			for (int index = 0; index < parameters.length; index++) {
				final Object para = parameters[index];
				if (this.annotations == null) {
					this.getPositionParameters().put(index, para);
					continue;
				}
				for (final Annotation annotation : annotations[index]) {
					final Class<? extends Annotation> clazz = annotation.annotationType();
					if (clazz.equals(Named.class)) {
						final Named named = (Named) annotation;
						this.getNamedParameters().put(named.value(), para);
						break label1;
					} else if (clazz.equals(FirstResult.class)) {
						this.firstResult = (Integer) para;
						break label1;
					} else if (clazz.equals(MaxResults.class)) {
						this.maxResults = (Integer) para;
						break label1;
					}
				}
				this.getPositionParameters().put(index, para);
			}
		}

		public Map<String, Object> getNamedParameters() {
			if (this.namedParameters == null)
				this.namedParameters = new HashMap<String, Object>();
			return namedParameters;
		}

		public Map<Integer, Object> getPositionParameters() {
			if (this.positionParameters == null)
				this.positionParameters = new HashMap<Integer, Object>();
			return this.positionParameters;
		}
	}
}
