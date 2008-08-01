package org.jrest4guice.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.google.inject.Singleton;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Singleton
public class SessionFactoryHolder {
	private SessionFactory sessionFactory;
	private final ThreadLocal<Session> session = new ThreadLocal<Session>();

	public SessionFactoryHolder() {
		try {
			sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		} catch (Exception e) {
			throw new RuntimeException("初始化 SessionFactoryHolder 失败",e);
		}
	}

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public Session getSession() {
		Session s = this.session.get();
		// 如果不存在，则创建一个新的
		if (s == null) {
			s = getSessionFactory().openSession();
			this.session.set(s);
		}
		return s;
	}

	public void closeSession() {
		Session s = this.session.get();
		if (s != null) {
			try {
				if (s.isOpen())
					s.close();
			} finally {
				this.session.remove();
			}
		}
	}
}
