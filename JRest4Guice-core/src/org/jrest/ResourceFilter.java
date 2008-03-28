package org.jrest;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest.annotation.HttpMethodType;
import org.jrest.annotation.JndiResource;
import org.jrest.annotation.Restful;
import org.jrest.ioc.IocContextManager;
import org.jrest.ioc.JRestModule;
import org.jrest.ioc.JndiProvider;
import org.jrest.ioc.JndiServiceInfo;
import org.jrest.ioc.ModelMap;
import org.jrest.ioc.RestServiceExecutor;
import org.jrest.util.ClassPathScanner;
import org.jrest.util.ClassPathScanner.ClassFilter;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

@SuppressWarnings("unchecked")
public class ResourceFilter implements Filter {
	public static final String METHOD_OF_GET = "get";
	public static final String METHOD_OF_POST = "post";
	public static final String METHOD_OF_PUT = "put";
	public static final String METHOD_OF_DELETE = "delete";

	private ResourceRegistry resourceReg = new ResourceRegistry();
	
	private static Set<String> extNameExcludes;
	static{
		extNameExcludes = new HashSet<String>(11);
		extNameExcludes.add("js");
		extNameExcludes.add("jsp");
		extNameExcludes.add("jspa");
		extNameExcludes.add("do");
		extNameExcludes.add("html");
		extNameExcludes.add("htm");
		extNameExcludes.add("jpg");
		extNameExcludes.add("gif");
		extNameExcludes.add("png");
		extNameExcludes.add("bmp");
		extNameExcludes.add("swf");
	}

	public static Injector injector;

	@Override
	public void init(FilterConfig config) throws ServletException {
		//初始化需要被过滤器忽略的资源扩展名
		String _extNameExcludes = config.getInitParameter("extNameExcludes");
		if (_extNameExcludes != null && !_extNameExcludes.trim().equals("")) {
			String[] exts = _extNameExcludes.split(",");
			for(String ext:exts)
				extNameExcludes.add(ext);
		}
		
		List<Class<?>> resources = new ArrayList<Class<?>>(0);
		// 获取要扫描的资源所对应的包路径
		String resource_package = config.getInitParameter("resource-package");
		if (resource_package != null && !resource_package.trim().equals("")) {
			String[] packages = resource_package.split(",");
			for (String packageName : packages)
				this.scanResource(resources, packageName);
		}

		// 加载Guice的模块
		String guiceModuleClass = config.getInitParameter("GuiceModuleClass");
		final List<Module> modules = new ArrayList<Module>(0);
		modules.add(new JRestModule());
		modules.add(this.generateGuiceProviderModule(resources));

		try {
			if (guiceModuleClass != null && !guiceModuleClass.trim().equals("")) {
				modules.add((Module) Class.forName(guiceModuleClass)
						.newInstance());
			}
		} catch (Exception e) {
			throw new ServletException("初始化ResourceFilter错误：\n"
					+ e.getMessage());
		}
		// 初始化Guice的注入器
		injector = Guice.createInjector(new Iterable<Module>() {
			@Override
			public Iterator<Module> iterator() {
				return modules.iterator();
			}
		});

		// 注册资源
		this.registResource(resources);
	}

	@Override
	public void doFilter(ServletRequest servletReqest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletReqest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		request.setCharacterEncoding("UTF-8");

		// REST资源的参数，这些参数都包含在URL中
		ModelMap params = new ModelMap<String, String>();
		String uri = request.getRequestURI();
		uri = uri.replace(request.getContextPath(), "");

		// 忽略以下的文件不处理
		String _uri = uri.trim().toLowerCase();
		int index = _uri.lastIndexOf(".");
		if(index != -1){
			String ext_name = _uri.substring(index+1);
			if (extNameExcludes.contains(ext_name)) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		if (uri == null || "".equals(uri)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 设置上下文中的环境变量
		IocContextManager.setContext(request, response, params);
		try {
			// 从REST资源注册表中查找此URI对应的资源
			Object service = resourceReg.getResource(request, response, uri,
					params);
			if (service == null)
				filterChain.doFilter(servletReqest, servletResponse);
			else {
				// 填充参数
				fillParameters(request, params);
				// 根据不同的请求方法调用REST对象的不同方法
				String method = request.getMethod();
				if (METHOD_OF_GET.equalsIgnoreCase(method))
					writeResult(response, RestServiceExecutor.execute(service,
							HttpMethodType.GET));
				else if (METHOD_OF_POST.equalsIgnoreCase(method))
					writeResult(response, RestServiceExecutor.execute(service,
							HttpMethodType.POST));
				else if (METHOD_OF_PUT.equalsIgnoreCase(method))
					writeResult(response, RestServiceExecutor.execute(service,
							HttpMethodType.PUT));
				else if (METHOD_OF_DELETE.equalsIgnoreCase(method))
					writeResult(response, RestServiceExecutor.execute(service,
							HttpMethodType.DELETE));
				else
					filterChain.doFilter(servletReqest, servletResponse);
			}
		} finally {
			// 清除上下文中的环境变量
			IocContextManager.clearContext();
		}

	}

	/**
	 * 填充参数
	 * 
	 * @modelMap request
	 * @modelMap params
	 */
	private void fillParameters(HttpServletRequest request, ModelMap params) {
		Enumeration names = request.getAttributeNames();
		String name;
		while (names.hasMoreElements()) {
			name = names.nextElement().toString();
			params.put(name, request.getAttribute(name));
		}

		// url中的参数
		names = request.getParameterNames();
		while (names.hasMoreElements()) {
			name = names.nextElement().toString();
			params.put(name, request.getParameter(name));
		}

		try {
			// 以http body方式提交的参数
			String charset = request.getCharacterEncoding();
			if (charset == null)
				charset = "UTF-8";
			BufferedReader in = new BufferedReader(new InputStreamReader(
					request.getInputStream(), charset));
			// Read the request
			CharArrayWriter data = new CharArrayWriter();
			char buf[] = new char[4096];
			int ret;
			while ((ret = in.read(buf, 0, 4096)) != -1)
				data.write(buf, 0, ret);

			// URL解码
			String content = URLDecoder.decode(data.toString().trim(), charset);
			// 组装参数
			if (content != "") {
				String[] param_pairs = content.split("&");
				String[] kv;
				for (String p : param_pairs) {
					kv = p.split("=");
					if (kv.length > 1)
						params.put(kv[0], kv[1]);
				}
			}

			data.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void scanResource(List<Class<?>> resources, String packageName) {
		List<Class<?>> list = new ClassPathScanner(packageName,
				new ClassFilter() {
					public boolean accept(Class<?> clazz) {
						return clazz.isAnnotationPresent(Restful.class);
					}
				}).scan();

		resources.addAll(list);
	}

	/**
	 * 生成Guice的Provider模块
	 * 
	 * @modelMap resources
	 * @return
	 */
	private Module generateGuiceProviderModule(List<Class<?>> resources) {
		final Set<JndiServiceInfo> jndiServiceInfos = new HashSet<JndiServiceInfo>(
				0);
		JndiResource annotation;
		for (Class<?> clazz : resources) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				annotation = field.getAnnotation(JndiResource.class);
				if (annotation != null) {
					jndiServiceInfos.add(new JndiServiceInfo(field.getType(),
							annotation.jndi()));
				}
			}
		}

		Module module = new Module() {
			@Override
			public void configure(Binder binder) {
				Class serviceClass;
				for (JndiServiceInfo info : jndiServiceInfos) {
					serviceClass = info.getServiceClass();
					binder.bind(serviceClass).toProvider(
							JndiProvider.fromJndi(serviceClass, info
									.getJndiName()));
				}
			}
		};

		return module;
	}

	private void registResource(List<Class<?>> resources) {
		for (Class<?> clazz : resources) {
			// 获取注解中的URI映射规则
			// 一个资源可以映射到多个URL上
			String[] uri = clazz.getAnnotation(Restful.class).uri();
			for (String s : uri) {
				// 注册此REST资源
				resourceReg.registerResource(s, clazz);
			}
		}
	}

	private void writeResult(HttpServletResponse response, Object result) {
		if (result == null)
			return;

		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void destroy() {
	}
}
