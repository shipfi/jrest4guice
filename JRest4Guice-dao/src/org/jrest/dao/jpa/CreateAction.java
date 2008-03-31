package org.jrest.dao.jpa;

import javax.persistence.EntityManager;

import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Create;

public class CreateAction extends AbstractAction<Create, JpaDaoContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0)
			return null;
		EntityManager em = this.getContext().getEntityManager();
		if (parameters[0].getClass().isArray()) {
			for (Object entity : (Object[]) parameters[0]) {
				em.persist(entity);
			}
		} else {
			em.persist(parameters[0]);
		}
		return null;
	}

	@Override
	protected void initialize() {
		this.annotationClass = Create.class;
		this.contextClass = JpaDaoContext.class;
	}

}
