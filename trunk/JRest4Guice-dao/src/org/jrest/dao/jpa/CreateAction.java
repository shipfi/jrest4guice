package org.jrest.dao.jpa;

import javax.persistence.EntityManager;

import org.jrest.dao.actions.AbstractAction;
import org.jrest.dao.annotations.Create;

import com.google.inject.Inject;

public class CreateAction extends AbstractAction<Create, JpaContext> {

	@Override
	public Object execute(Object[] parameters) {
		if (parameters.length == 0)
			return null;
		EntityManager em = getContext().getEntityManager();
		if (parameters[0].getClass().isArray()) {
			for (Object entity : (Object[]) parameters[0]) {
				em.persist(entity);
			}
		} else {
			em.persist(parameters[0]);
		}
		return null;
	}

	@Override
	protected void initialize() {
		annotationClass = Create.class;
		contextClass = JpaContext.class;
	}

	@Inject
	@Override
	public void setContext(JpaContext context) {
		this.context = context;
	}
}
