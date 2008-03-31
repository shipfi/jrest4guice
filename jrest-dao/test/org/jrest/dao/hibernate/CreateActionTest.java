package org.jrest.dao.hibernate;

import org.jrest.dao.actions.Action;

public class CreateActionTest {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Action action = new CreateAction();
		System.out.println("annotation:" + action.getAnnotationClass().getName() + " context:" + action.getContextClass().getName());
		action = new DeleteAction();
		System.out.println("annotation:" + action.getAnnotationClass().getName() + " context:" + action.getContextClass().getName());
		action = new UpdateAction();
		System.out.println("annotation:" + action.getAnnotationClass().getName() + " context:" + action.getContextClass().getName());
		action = new RetrieveAction();
		System.out.println("annotation:" + action.getAnnotationClass().getName() + " context:" + action.getContextClass().getName());
	}

}
