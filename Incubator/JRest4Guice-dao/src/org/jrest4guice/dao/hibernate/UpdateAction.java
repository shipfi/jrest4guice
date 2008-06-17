package org.jrest4guice.dao.hibernate;

import org.hibernate.Session;
import org.jrest4guice.dao.actions.ActionTemplate;
import org.jrest4guice.dao.annotations.Update;

import com.google.inject.Inject;

public class UpdateAction extends ActionTemplate<Update, HibernateContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0)
			return null;
		Session session = getContext().getSession();
		if (parameters[0].getClass().isArray()) {
			for (Object entity : (Object[]) parameters[0]) {
				session.update(entity);
			}
		} else {
			session.update(parameters[0]);
		}
		return null;
	}

	@Override
	protected void initialize() {
		annotationClass = Update.class;
		contextClass = HibernateContext.class;
	}

	@Inject
	@Override
	public void setContext(HibernateContext context) {
		this.context = context;
	}

}
