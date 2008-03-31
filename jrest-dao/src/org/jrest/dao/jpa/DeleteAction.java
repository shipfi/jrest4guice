package org.jrest.dao.jpa;

import javax.persistence.EntityManager;

import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Delete;

public class DeleteAction extends AbstractAction<Delete, JpaDaoContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0)
			return null;
		EntityManager em = this.getContext().getEntityManager();
		if (parameters[0].getClass().isArray()) {
			for (Object entity : (Object[]) parameters[0]) {
				em.remove(entity);
			}
		} else {
			em.remove(parameters[0]);
		}
		return null;
	}

	@Override
	protected void initialize() {
		this.annotationClass = Delete.class;
		this.contextClass = JpaDaoContext.class;
	}

}
