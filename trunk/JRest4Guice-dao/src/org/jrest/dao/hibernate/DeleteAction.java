package org.jrest.dao.hibernate;

import org.hibernate.Session;
import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Delete;

public class DeleteAction extends AbstractAction<Delete, HibernateDaoContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0)
			return null;
		Session session = this.getContext().getSession();
		if (parameters[0].getClass().isArray()) {
			for (Object entity : (Object[]) parameters[0]) {
				session.delete(entity);
			}
		} else {
			session.delete(parameters[0]);
		}
		return null;
	}

	@Override
	protected void initialize() {
		this.annotationClass = Delete.class;
		this.contextClass = HibernateDaoContext.class;
	}

}
