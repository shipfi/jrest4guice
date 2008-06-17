package org.jrest4guice.dao.jpa;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jrest4guice.dao.actions.Action;
import org.jrest4guice.dao.actions.ActionContext;

import com.google.inject.Inject;

/**
 * JPA环境的<code>ActionContext</code>实现
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
@SuppressWarnings("unchecked")
public class JpaContext implements ActionContext {
	
	@Inject
	private EntityManager entityManager;
	private Action action;
	private boolean withoutService = false;
	
	@Override
	public Object execute(Method method, Object[] parameters) throws Throwable {
		if (!withoutService)
			return action.execute(method, parameters);
		else {
			EntityTransaction tx = entityManager.getTransaction();
			try {
				tx.begin();
				Object result = action.execute(method, parameters);
				tx.commit();
				return result;
			} catch (Exception e) {
				tx.rollback();
				throw e.getCause();
			}
		}
	}
	
	@Override
	public void setAction(Action action) {
		this.action = action;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public boolean isWithoutService() {
		return withoutService;
	}
	
	public void setWithoutService(boolean withoutService) {
		this.withoutService = withoutService;
	}
}
