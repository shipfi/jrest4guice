package org.jrest.dao.jpa;

import javax.persistence.EntityManager;

import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Delete;

public class DeleteAction extends AbstractAction<Delete, JpaDaoContext> {

	@Override
	public Object execute(Object... parameters) {
		if (parameters.length == 0)
			return null;
		EntityManager em = this.getContext().getEntityManager();
		for (Object entity : parameters) {
			em.remove(entity);
		}
		return null;
	}

	@Override
	protected void initialize() {
		this.annotationClass = Delete.class;
		this.contextClass = JpaDaoContext.class;
	}

}
