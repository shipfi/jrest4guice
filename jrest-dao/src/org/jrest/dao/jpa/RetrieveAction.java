package org.jrest.dao.jpa;

import javax.persistence.EntityManager;

import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Retrieve;

public class RetrieveAction extends AbstractAction<Retrieve, JpaDaoContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0)
			return null;
		EntityManager em = this.getContext().getEntityManager();
		return em.find(this.getMethodReturnType(), parameters[0]);
	}

	@Override
	protected void initialize() {
		this.annotationClass = Retrieve.class;
		this.contextClass = JpaDaoContext.class;
	}

}
