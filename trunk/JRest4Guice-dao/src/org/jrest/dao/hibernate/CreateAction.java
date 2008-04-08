package org.jrest.dao.hibernate;

import org.hibernate.Session;
import org.jrest.dao.actions.ActionTemplate;
import org.jrest.dao.annotations.Create;

import com.google.inject.Inject;

public class CreateAction extends ActionTemplate<Create, HibernateContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0)
			return null;
		Session session = getContext().getSession();
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
		annotationClass = Create.class;
		contextClass = HibernateContext.class;
	}

	@Inject
	@Override
	public void setContext(HibernateContext context) {
		this.context = context;
	}
}
