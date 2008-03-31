package org.jrest.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jrest.dao.AbstractRegister;
import org.jrest.dao.Register;
import org.jrest.dao.annotations.Create;
import org.jrest.dao.annotations.Delete;
import org.jrest.dao.annotations.Find;
import org.jrest.dao.annotations.Retrieve;
import org.jrest.dao.annotations.Update;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

public class HibernateRegister extends AbstractRegister implements Register {

	private HibernateTemplate hibernateTemplate;

	@Override
	public Object createContext(Class<?> clazz) {
		// TODO 需要改为通过Guice获取对象
		if (clazz.equals(HibernateDaoContext.class)) {
			HibernateDaoContext context = new HibernateDaoContext();
			context.setSession(this.getSession(true));
			return context;
		}
		return null;
	}

	@Override
	protected void initialize() {
		// TODO 需要改为通过Guice注入内容
		register(Create.class, CreateAction.class);
		register(Delete.class, DeleteAction.class);
		register(Retrieve.class, RetrieveAction.class);
		register(Update.class, UpdateAction.class);
		register(Find.class, FindAction.class);
		register(HibernateDaoContext.class);
	}

	protected final Session getSession(boolean allowCreate)
			throws DataAccessResourceFailureException, IllegalStateException {
		return (!allowCreate ? SessionFactoryUtils.getSession(
				getSessionFactory(), false) : SessionFactoryUtils.getSession(
				getSessionFactory(), this.hibernateTemplate
						.getEntityInterceptor(), this.hibernateTemplate
						.getJdbcExceptionTranslator()));
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	public final SessionFactory getSessionFactory() {
		return (this.hibernateTemplate != null ? this.hibernateTemplate
				.getSessionFactory() : null);
	}

}
