package org.jrest.dao.jpa;

import javax.persistence.EntityManager;

import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Update;

public class UpdateAction extends AbstractAction<Update, JpaDaoContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0)
			return null;
		EntityManager em = this.getContext().getEntityManager();
		if (parameters[0].getClass().isArray()) {
			for (Object entity : (Object[]) parameters[0]) {
				entity = em.merge(entity);
			}
		} else {
			parameters[0] = em.merge(parameters[0]);
		}
		return null;
	}

	@Override
	protected void initialize() {
		this.annotationClass = Update.class;
		this.contextClass = JpaDaoContext.class;
	}

}
