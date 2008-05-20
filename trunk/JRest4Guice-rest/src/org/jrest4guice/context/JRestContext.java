package org.jrest4guice.context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jrest4guice.ServiceRoute;
import org.jrest4guice.core.guice.GuiceContext;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
@SuppressWarnings("unchecked")
public class JRestContext {
	private ServiceRoute root = new ServiceRoute();
	private Pattern paramPattern = Pattern.compile("\\{([a-zA-Z_]+[0-9]*)\\}");
	private static final String PARAM_KEY = "_$__PARAM_$__";

	private JRestContext() {
	}

	private static class SingletonHolder {
		static JRestContext instance = new JRestContext();
	}

	public static JRestContext getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 添加资源到上下文
	 * @param uri			资源的路径
	 * @param resourceClass	资源对应的实现类
	 */
	public void addResource(String uri, Class resourceClass) {
		String[] routePath = uri.split("/");
		ServiceRoute current = root;
		ServiceRoute child = null;
		for (String s : routePath) {
			s = s.trim();
			if ("".equals(s))
				continue;
			child = current.getChild(s);
			if (child == null) {
				Matcher matcher = paramPattern.matcher(s);
				if (matcher.matches()) {
					if ((child = current.getChild(PARAM_KEY)) == null) {
						child = new ServiceRoute(matcher.group(1));
						current.addChild(PARAM_KEY, child);
					}
				} else {
					child = new ServiceRoute();
					current.addChild(s, child);
				}
			}
			current = child;
			child = null;
		}

		current.setServiceClass(resourceClass);
	}
	
	/**
	 * 
	 * @param uri
	 * @param params
	 * @return
	 */
	public Object lookupResource(String uri) {
		String[] routePath = uri.split("/");
		ServiceRoute current = root;
		ServiceRoute child = null;
		for (String path : routePath) {
			path = path.trim();
			if ("".equals(path))
				continue;
			child = current.getChild(PARAM_KEY);
			if (child != null) {
				HttpContextManager.getModelMap().put(child.getParamName(), path);
			} else {
				child = current.getChild(path);
				if (child == null)
					return null;
			}
			current = child;
			child = null;
		}

		Object service = null;
		Class resourceClaz = current.getServiceClass();
		if (resourceClaz != null) {
			try {
				service = GuiceContext.getInstance().getBean(resourceClaz);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return service;
	}
}
