package org.cnoss.jrest;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("unchecked")
public class ResourceRegistry {
	private ServiceRoute root = new ServiceRoute();
	private Pattern paramPattern = Pattern.compile("\\{([a-zA-Z_]+[0-9]*)\\}");
	private static final String PARAM_KEY = "_$__PARAM_$__";

	public void registerResource(String uriPattern, Class resourceClass) {
		String[] routePath = uriPattern.split("/");
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

	public Object getResource(HttpServletRequest request,
			HttpServletResponse response, String uri, Map<String, String> params) {
		if (params == null)
			return null;

		String[] routePath = uri.split("/");
		ServiceRoute current = root;
		ServiceRoute child = null;
		for (String path : routePath) {
			path = path.trim();
			if ("".equals(path))
				continue;
			child = current.getChild(PARAM_KEY);
			if (child != null) {
				params.put(child.getParamName(), path);
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
				service = ResourceFilter.injector.getInstance(resourceClaz);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return service;
	}
}
