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
/**
 * Hibernate的持久化事件监听器，用户可能添加自己感兴趣的事件监听器来处理持久化后的额外业务逻辑
 */
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
	
	/**
	 * 添加PostDeleteEventListener
	 * @param listener
	 */
	public void addPostDeleteEventListener(PostDeleteEventListener listener) {
		postDeleteEventListeners.add(listener);
	}
	/**
	 * 添加PostInsertEventListener
	 * @param listener
	 */
	public void addPostInsertEventListener(PostInsertEventListener listener) {
		postInsertEventListeners.add(listener);
	}
	/**
	 * 添加PostUpdateEventListener
	 * @param listener
	 */
	public void addPostUpdateEventListener(PostUpdateEventListener listener) {
		postUpdateEventListeners.add(listener);
	}
	/**
	 * 添加Initializable
	 * @param initializable
	 */
	public void addInitializable(Initializable initializable) {
		initializables.add(initializable);
	}

	/* (non-Javadoc)
	 * @see org.hibernate.event.PostDeleteEventListener#onPostDelete(org.hibernate.event.PostDeleteEvent)
	 */
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		for(PostDeleteEventListener listener:postDeleteEventListeners){
			listener.onPostDelete(event);
		}
	}

	/* (non-Javadoc)
	 * @see org.hibernate.event.PostInsertEventListener#onPostInsert(org.hibernate.event.PostInsertEvent)
	 */
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

	/* (non-Javadoc)
	 * @see org.hibernate.event.Initializable#initialize(org.hibernate.cfg.Configuration)
	 */
	@Override
	public void initialize(Configuration config) {
		for(Initializable listener:initializables){
			listener.initialize(config);
		}
	}
	
	/**
	 * 返回HibernateEventListener实例的引用
	 * @return
	 */
	public static HibernateEventListener getInstance(){
		return GuiceContext.getInstance().getBean(HibernateEventListener.class);
	}
}
