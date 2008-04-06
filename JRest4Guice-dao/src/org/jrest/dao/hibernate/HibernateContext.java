package org.jrest.dao.hibernate;

import java.lang.reflect.Method;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jrest.dao.actions.Action;
import org.jrest.dao.actions.ActionContext;

import com.google.inject.Inject;

@SuppressWarnings("unchecked")
public class HibernateContext implements ActionContext {

	@Inject
	private Session session;
	private Action action;
	private boolean withoutService = false;

	@Override
	public Object execute(Method method, Object[] parameters) throws Throwable {
		if (!withoutService)
			return action.execute(method, parameters);
		else {
			Transaction tx = session.beginTransaction();
			try {
				Object result = action.execute(method, parameters);
				tx.commit();
				return result;
			} catch (Exception e) {
				tx.rollback();
				throw e.getCause();
			}
		}
	}

	public Session getSession() {
		return session;
	}

	@Override
	public void setAction(Action action) {
		this.action = action;
	}
}
