package org.jrest4guice.jpa;

import org.hibernate.cfg.Configuration;
import org.hibernate.event.Initializable;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;

@SuppressWarnings("serial")
public class HibernateEventListener implements PostDeleteEventListener,
		PostInsertEventListener, PostUpdateEventListener, Initializable {

	public void addPostDeleteEventListener(PostDeleteEventListener listener) {
	}
	public void addPostInsertEventListener(PostInsertEventListener listener) {
	}
	public void addPostUpdateEventListener(PostUpdateEventListener listener) {
	}
	public void addInitializable(Initializable listener) {
	}

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(Configuration config) {
		// TODO Auto-generated method stub

	}
}
