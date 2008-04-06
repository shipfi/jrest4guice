package org.jrest.dao.jpa;

import javax.persistence.EntityManager;

import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Retrieve;

import com.google.inject.Inject;

public class RetrieveAction extends AbstractAction<Retrieve, JpaContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0)
			return null;
		EntityManager em = getContext().getEntityManager();
		return em.find(getMethodReturnType(), parameters[0]);
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
