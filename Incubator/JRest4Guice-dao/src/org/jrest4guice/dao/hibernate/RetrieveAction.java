package org.jrest4guice.dao.hibernate;

import java.io.Serializable;

import org.hibernate.Session;
import org.jrest4guice.dao.actions.ActionTemplate;
import org.jrest4guice.dao.annotations.Retrieve;

import com.google.inject.Inject;

public class RetrieveAction extends ActionTemplate<Retrieve, HibernateContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0)
			return null;
		Session session = getContext().getSession();
		Class<?> clazz = getMethodReturnType();
		return session.get(clazz, (Serializable) parameters[0]);
	}

	@Override
	protected void initialize() {
		annotationClass = Retrieve.class;
		contextClass = HibernateContext.class;
	}

	@Inject
	@Override
	public void setContext(HibernateContext context) {
		this.context = context;
	}

}
