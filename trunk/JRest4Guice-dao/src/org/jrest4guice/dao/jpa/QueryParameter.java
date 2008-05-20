package org.jrest4guice.dao.jpa;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.jrest4guice.dao.annotations.Find.FirstResult;
import org.jrest4guice.dao.annotations.Find.MaxResults;

import com.google.inject.name.Named;

/**
 * 查询参数对象
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
public class QueryParameter {
	
	private Integer firstResult;
	private Integer maxResults;
	private Map<String, Object> namedParameters = new HashMap<String, Object>();
	private Map<Integer, Object> positionParameters = new HashMap<Integer, Object>();
	
	public QueryParameter(Object[] parameters, Annotation[][] annotations) {
		if (parameters == null)
			return;
		label1: for (int index = 0; index < parameters.length; index++) {
			final Object para = parameters[index];
			if (annotations == null) {
				// if (this.annotations == null) {
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
	
	public Integer getFirstResult() {
		return firstResult;
	}
	
	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}
	
	public Integer getMaxResults() {
		return maxResults;
	}
	
	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}
	
	public Map<String, Object> getNamedParameters() {
		return namedParameters;
	}
	
	public void setNamedParameters(Map<String, Object> namedParameters) {
		this.namedParameters = namedParameters;
	}
	
	public boolean hasNamedParameters() {
		if (namedParameters.size() > 0)
			return true;
		return false;
	}
	
	public void setPositionParameters(Map<Integer, Object> positionParameters) {
		this.positionParameters = positionParameters;
	}
	
	public Map<Integer, Object> getPositionParameters() {
		return positionParameters;
	}
	
	public boolean hasPositionParameters() {
		if (positionParameters.size() > 0)
			return true;
		return false;
	}
	
}
