package org.jrest.dao.hibernate;

import java.io.Serializable;

import org.hibernate.Session;
import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Retrieve;

public class RetrieveAction extends AbstractAction<Retrieve, HibernateDaoContext> {

	@Override
	public Object execute(Object... parameters) {
		Session session = this.getContext().getSession();
		Class<?> clazz = this.getMethodReturnType();
		return session.get(clazz, (Serializable) parameters[0]);
	}

	@Override
	protected void initialize() {
		this.annotationClass = Retrieve.class;
		this.contextClass = HibernateDaoContext.class;
	}

}
