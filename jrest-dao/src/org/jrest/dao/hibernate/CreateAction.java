package org.jrest.dao.hibernate;

import org.hibernate.Session;
import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Create;

public class CreateAction extends AbstractAction<Create, HibernateDaoContext> {

	@Override
	public Object execute(Object... parameters) {
		Session session = this.getContext().getSession();
		return session.save(parameters[0]);
	}

	@Override
	protected void initialize() {
		this.annotationClass = Create.class;
		this.contextClass = HibernateDaoContext.class;
	}
}
