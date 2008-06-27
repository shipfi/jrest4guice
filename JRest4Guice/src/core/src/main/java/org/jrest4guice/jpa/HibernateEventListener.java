package org.jrest4guice.jpa;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.event.Initializable;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.jrest4guice.guice.GuiceContext;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class HibernateEventListener implements PostDeleteEventListener,
		PostInsertEventListener, PostUpdateEventListener, Initializable {
	
	private static List<PostDeleteEventListener> postDeleteEventListeners;
	private static List<PostInsertEventListener> postInsertEventListeners;
	private static List<PostUpdateEventListener> postUpdateEventListeners;
	private static List<Initializable> initializables;
	
	static{
		postDeleteEventListeners = new ArrayList<PostDeleteEventListener>(0);
		postInsertEventListeners = new ArrayList<PostInsertEventListener>(0);
		postUpdateEventListeners = new ArrayList<PostUpdateEventListener>(0);
		initializables = new ArrayList<Initializable>(0);
	}

	public void addPostDeleteEventListener(PostDeleteEventListener listener) {
		postDeleteEventListeners.add(listener);
	}
	public void addPostInsertEventListener(PostInsertEventListener listener) {
		postInsertEventListeners.add(listener);
	}
	public void addPostUpdateEventListener(PostUpdateEventListener listener) {
		postUpdateEventListeners.add(listener);
	}
	public void addInitializable(Initializable listener) {
		initializables.add(listener);
	}

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		for(PostDeleteEventListener listener:postDeleteEventListeners){
			listener.onPostDelete(event);
		}
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		for(PostInsertEventListener listener:postInsertEventListeners){
			listener.onPostInsert(event);
		}
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		for(PostUpdateEventListener listener:postUpdateEventListeners){
			listener.onPostUpdate(event);
		}
	}

	@Override
	public void initialize(Configuration config) {
		for(Initializable listener:initializables){
			listener.initialize(config);
		}
	}
	
	public static HibernateEventListener getInstance(){
		return GuiceContext.getInstance().getBean(HibernateEventListener.class);
	}
}
