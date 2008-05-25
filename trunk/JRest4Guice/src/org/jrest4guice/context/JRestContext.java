package org.jrest4guice.context;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jrest4guice.Service;
import org.jrest4guice.ServiceRoute;
import org.jrest4guice.annotations.Path;
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
	public void addResource(String uri, Class resourceClass,boolean processMethod) {
		ServiceRoute current = this.doAddResource(uri, resourceClass,root,null);
		if(!processMethod)
			return;
		
		Method[] methods = resourceClass.getMethods();
		String[] paths;
		for(Method method :methods){
			if(method.isAnnotationPresent(Path.class)){
				paths = ((Path)method.getAnnotation(Path.class)).value();
				for(String path:paths)
					this.doAddResource(path, resourceClass,current,method);
			}
		}
	}

	private ServiceRoute doAddResource(String uri, Class resourceClass,ServiceRoute parent,Method method) {
		//如果URI以"/"开头，则将当前的父结点指向root
		if(uri.startsWith("/"))
			parent = root;
		
		String[] routePath = uri.split("/");
		ServiceRoute current = parent;
		ServiceRoute child = null;
		for (String s : routePath) {
			s = s.trim();
			if ("".equals(s))
				continue;
			child = current.getRouteChild(s);
			Matcher matcher = paramPattern.matcher(s);
			if (matcher.matches()) {//处理参数类型的路由节点
				child = new ServiceRoute(matcher.group(1));
				current.addParamChild(PARAM_KEY, child);
			} else {//处理变通类型的路由节点（只有最末端的节点才会绑定到服务）
				if(child == null)
					child = new ServiceRoute();
				current.addRouteChild(s, child);
			}
			
			current = child;
		}
		//绑定服务到当前的路由节点
		current.setServiceClass(resourceClass);
		//设置当前服务所对应的Rest方法（只在@Path被声明在方法上时生效，实现subpath的功能）
		current.addRestMethod(method);
		
		return current;
	}
	
	/**
	 * 
	 * @param uri
	 * @param params
	 * @return
	 */
	public Service lookupResource(String uri) {
		String[] routePath = uri.split("/");
		ServiceRoute current = root;
		ServiceRoute child = null;
		List<ServiceRoute> paramChild = null;
		int index = 0;
		int len = routePath.length;
		for (String path : routePath) {
			index ++;
			
			path = path.trim();
			if ("".equals(path))
				continue;
			
			paramChild = current.getParamChild(PARAM_KEY);
			if (paramChild != null){
				for(ServiceRoute p : paramChild)
					HttpContextManager.getModelMap().put(p.getParamName(), path);
			}
			
			child = current.getRouteChild(path);
			if (child == null && paramChild != null && index<len)
				child = paramChild.get(0);

			if (child == null){
				continue;
			}
			
			current = child;
			child = null;
		}

		Class resourceClaz = current.getServiceClass();
		if (resourceClaz != null) {
			try {
				Object instance = GuiceContext.getInstance().getBean(resourceClaz);
				return new Service(instance,current.getMethod());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
