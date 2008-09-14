package org.jrest4guice.rest.sna;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.commons.collections.iterators.IteratorEnumeration;

public class SNAHttpSession implements HttpSession {

	private HttpSession hSession;
	private SNASession snaSession;
	
	public void clear(){
		this.snaSession.clear();
		final Enumeration names = this.hSession.getAttributeNames();
		while(names.hasMoreElements()){
			this.hSession.removeAttribute(names.nextElement().toString());
		}
	}

	public SNAHttpSession(HttpSession hSession, SNASession snaSession) {
		this.hSession = hSession;
		this.snaSession = snaSession;
	}

	@Override
	public Object getAttribute(String name) {
		return this.snaSession.get(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		return new IteratorEnumeration(this.snaSession.keySet().iterator());
	}

	@Override
	public long getCreationTime() {
		return this.hSession.getCreationTime();
	}

	@Override
	public String getId() {
		return this.hSession.getId();
	}

	@Override
	public long getLastAccessedTime() {
		return this.hSession.getLastAccessedTime();
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.hSession.getMaxInactiveInterval();
	}

	@Override
	public ServletContext getServletContext() {
		return this.hSession.getServletContext();
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return this.hSession.getSessionContext();
	}

	@Override
	public Object getValue(String name) {
		return this.snaSession.get(name);
	}

	@Override
	public String[] getValueNames() {
		return (String[])this.snaSession.keySet().toArray(new String[]{});
	}

	@Override
	public void invalidate() {
		this.hSession.invalidate();
		this.snaSession.clear();
	}

	@Override
	public boolean isNew() {
		return this.hSession.isNew();
	}

	@Override
	public void putValue(String name, Object value) {
		this.snaSession.put(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		this.snaSession.remove(name);
	}

	@Override
	public void removeValue(String name) {
		this.removeAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		this.snaSession.put(name,value);
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		this.hSession.setMaxInactiveInterval(interval);
	}
}
