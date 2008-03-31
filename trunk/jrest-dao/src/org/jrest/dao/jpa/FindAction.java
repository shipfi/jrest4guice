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
import org.jrest.dao.annotations.Find.Named;

public class FindAction extends AbstractAction<Find, JpaDaoContext> {

	protected final static Log log = LogFactory.getLog(FindAction.class);

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object[] parameters) {
		Query query = getQuery();
		QueryParameters queryPara = new QueryParameters(parameters, method.getParameterAnnotations());
		fittingQuery(query, queryPara);
		Find find = this.getAnnotation();
		if (!find.resultClass().equals(void.class) && !find.nativeQuery()) {
			return toProjectionalList(find.resultClass(), query.getResultList());
		}
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
				log.error("无法构建投影类型对象", e.getCause());	// 不应该出现该错误
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
		Find find = this.getAnnotation();
		EntityManager em = this.getContext().getEntityManager();
		if (StringUtils.isNotBlank(find.namedQuery()))
			return em.createNamedQuery(find.namedQuery());
		if (find.nativeQuery()) {
			if (find.resultClass().equals(void.class)) {
				return em.createNativeQuery(find.query());
			} else {
				return em.createNativeQuery(find.query(), find.resultClass());
			}
		} else {
			return em.createQuery(find.query());
		}
	}
	
	@Override
	protected void initialize() {
		this.annotationClass = Find.class;
		this.contextClass = JpaDaoContext.class;
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
