package org.jrest.dao.jpa;

import javax.persistence.EntityManager;

import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Update;

public class UpdateAction extends AbstractAction<Update, JpaDaoContext> {

	@Override
	public Object execute(Object... parameters) {
		if (parameters.length == 0)
			return null;
		EntityManager em = this.getContext().getEntityManager();
		for (Object entity : parameters) {
			entity = em.merge(entity);
		}
		return null;
	}

	@Override
	protected void initialize() {
		this.annotationClass = Update.class;
		this.contextClass = JpaDaoContext.class;
	}

}
