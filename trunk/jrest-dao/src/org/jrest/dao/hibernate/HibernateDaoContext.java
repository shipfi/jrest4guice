package org.jrest.dao.hibernate;

import org.hibernate.Session;

public class HibernateDaoContext {
	
	private Session session;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
