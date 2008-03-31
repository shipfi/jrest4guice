package org.jrest.dao.jpa;

import javax.persistence.EntityManagerFactory;

import org.jrest.dao.AbstractRegister;
import org.jrest.dao.annotations.Create;
import org.jrest.dao.annotations.Delete;
import org.jrest.dao.annotations.Find;
import org.jrest.dao.annotations.Retrieve;
import org.jrest.dao.annotations.Update;

public class JpaRegister extends AbstractRegister {
	
	private EntityManagerFactory emf;

	@Override
	protected void initialize() {
		// TODO 需要改为通过Guice注入内容
		register(Create.class, CreateAction.class);
		register(Delete.class, DeleteAction.class);
		register(Retrieve.class, RetrieveAction.class);
		register(Update.class, UpdateAction.class);
		register(Find.class, FindAction.class);
		register(JpaDaoContext.class);
	}

	@Override
	public Object createContext(Class<?> clazz) {
		// TODO 需要改为通过Guice获取对象
		if (clazz.equals(JpaDaoContext.class)) {
			JpaDaoContext context = new JpaDaoContext();
			context.setEntityManager(this.emf.createEntityManager());
			return context;
		}
		return null;
	}

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

}
