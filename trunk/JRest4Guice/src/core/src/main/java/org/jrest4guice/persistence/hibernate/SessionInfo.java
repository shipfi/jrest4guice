package org.jrest4guice.persistence.hibernate;

import org.hibernate.Session;

public class SessionInfo {
	private Session session;
	private boolean need2ProcessTransaction = false;
	
	public SessionInfo(Session session){
		this.session = session;
	}
	
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public boolean isNeed2ProcessTransaction() {
		return need2ProcessTransaction;
	}
	public void setNeed2ProcessTransaction(boolean need2ProcessTransaction) {
		this.need2ProcessTransaction = need2ProcessTransaction;
	}
}
