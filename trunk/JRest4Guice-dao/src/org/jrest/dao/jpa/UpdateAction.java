package org.jrest.dao.jpa;

import javax.persistence.EntityManager;

import org.jrest.dao.actions.ActionTemplate;
import org.jrest.dao.annotations.Update;

import com.google.inject.Inject;

public class UpdateAction extends ActionTemplate<Update, JpaContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0)
			return null;
		EntityManager em = getContext().getEntityManager();
		if (parameters[0].getClass().isArray()) {
			for (Object entity : (Object[]) parameters[0]) {
				entity = em.merge(entity);
			}
		} else {
			parameters[0] = em.merge(parameters[0]);
		}
		return null;
	}

	@Override
	protected void initialize() {
		annotationClass = Update.class;
		contextClass = JpaContext.class;
	}

	@Inject
	@Override
	public void setContext(JpaContext context) {
		this.context = context;
	}

}
