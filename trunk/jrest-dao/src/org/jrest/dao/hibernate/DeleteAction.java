package org.jrest.dao.hibernate;

import org.hibernate.Session;
import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Delete;

public class DeleteAction extends AbstractAction<Delete, HibernateDaoContext> {

	@Override
	public Object execute(Object... parameters) {
		Session session = this.getContext().getSession();
		session.delete(parameters[0]);
		return null;
	}

	@Override
	protected void initialize() {
		this.annotationClass = Delete.class;
		this.contextClass = HibernateDaoContext.class;
	}

}
