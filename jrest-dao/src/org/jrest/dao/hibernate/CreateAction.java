package org.jrest.dao.hibernate;

import org.hibernate.Session;
import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Create;

public class CreateAction extends AbstractAction<Create, HibernateDaoContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0)
			return null;
		Session session = this.getContext().getSession();
		if (parameters[0].getClass().isArray()) {
			for (Object obj : (Object[]) parameters[0]) {
				session.save(obj);
			}
		} else {
			session.save(parameters[0]);
		}
		return null;
	}

	@Override
	protected void initialize() {
		this.annotationClass = Create.class;
		this.contextClass = HibernateDaoContext.class;
	}
}
