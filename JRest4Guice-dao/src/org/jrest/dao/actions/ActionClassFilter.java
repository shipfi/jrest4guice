package org.jrest.dao.actions;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ActionClassFilter {

	private final List<Class<Action>> actionClasses = new ArrayList<Class<Action>>();

	public ActionClassFilter(List<Class<?>> classes) {
		for (Class<?> clz : classes) {
			onScan(clz);
		}
	}

	public List<Class<Action>> getActionClasses() {
		return actionClasses;
	}

	private void onScan(Class<?> clazz) {
		if (Action.class.isAssignableFrom(clazz))
			actionClasses.add((Class<Action>) clazz);
	}

}
