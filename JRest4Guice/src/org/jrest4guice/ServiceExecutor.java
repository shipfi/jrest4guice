package org.jrest4guice;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.jrest4guice.annotation.Delete;
import org.jrest4guice.annotation.Get;
import org.jrest4guice.annotation.HttpMethodType;
import org.jrest4guice.annotation.MimeType;
import org.jrest4guice.annotation.ModelBean;
import org.jrest4guice.annotation.Parameter;
import org.jrest4guice.annotation.Path;
import org.jrest4guice.annotation.Post;
import org.jrest4guice.annotation.ProduceMime;
import org.jrest4guice.annotation.Put;
import org.jrest4guice.context.HttpContextManager;
import org.jrest4guice.context.ModelMap;
import org.jrest4guice.core.util.ParameterNameDiscoverer;
import org.jrest4guice.writer.ResponseWriter;
import org.jrest4guice.writer.ResponseWriterRegister;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
@SuppressWarnings("unchecked")
public class ServiceExecutor {
	@Inject
	private HttpServletRequest request;
	@Inject
	private HttpServletResponse response;

	private static Map<String, Map<HttpMethodType, Method>> restServiceMethodMap = new HashMap<String, Map<HttpMethodType, Method>>(
			0);

	/**
	 * 身份验证的URL
	 */
	private String loginUrl;
	/**
	 * 身份验证的URL
	 */
	private String loginErrorUrl;

	/**
	 * 根据Rest服务的方法类型，执行相应的业务方法
	 * 
	 * @modelMap service
	 * @modelMap methodType
	 * @return
	 */
	public void execute(Service service, HttpMethodType methodType,
			String charset) throws Throwable {
		Object result = null;
		Object instance = service.getInstance();
		String name = instance.getClass().getName();
		if (!restServiceMethodMap.containsKey(name)) {
			// 初始化当前Rest服务的方法映射字典
			this.initCurrentServiceMethod(instance, name);
		}

		Method method = service.getMethod();
		if(method == null)
			method = restServiceMethodMap.get(name).get(methodType);
		
		if (method == null)
			return;

		Exception exception = null;

		ModelMap modelMap = HttpContextManager.getModelMap();
		if (method != null) {
			try {
				// 构造参数集合
				List params = constructParams(method, modelMap);
				// 执行业务方法
				result = method.invoke(instance, params.size() > 0 ? params
						.toArray() : null);
				// 向客户端写回结果
				writeResult(charset, result, method);
			} catch (RuntimeException e) {
				exception = e;
			} catch (Exception e) {
				exception = e;
			}

			// 向客户端写回异常结果
			if (exception != null) {
				Throwable throwable = exception;
				if (exception instanceof java.lang.IllegalArgumentException) {
					throwable = new Exception("调用" + method.getName()
							+ "时出错: 原因是参数不完整!", exception);
				} else if (exception instanceof java.lang.reflect.InvocationTargetException) {
					throwable = ((InvocationTargetException) exception).getTargetException();
				}
				writeResult(charset, throwable, method);
			}
		}
	}

	/**
	 * 构造当前方法调用的参数集合
	 * 
	 * @param method
	 * @param modelMap
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private List constructParams(Method method, ModelMap modelMap)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException {
		Annotation[][] annotationArray = method.getParameterAnnotations();
		Class<?>[] parameterTypes = method.getParameterTypes();

		String pName;
		List params = new ArrayList(0);
		Object value;
		int index = 0;

		ParameterNameDiscoverer pnDiscoverer = new ParameterNameDiscoverer();
		String[] parameterNames = pnDiscoverer.getParameterNames(method);

		for (Annotation[] annotations : annotationArray) {
			value = null;
			pName = parameterNames[index];
			for (Annotation annotation : annotations) {
				if (annotation instanceof Parameter) {
					pName = ((Parameter) annotation).value();
				} else if (annotation instanceof ModelBean) {
					value = parameterTypes[index].newInstance();
					BeanUtils.populate(value, modelMap);
				}
			}

			// 转换参数值
			if (value == null)
				value = convertValue(modelMap.get(pName), parameterTypes[index]);
			// 添加当前参数
			params.add(value);

			index++;
		}
		return params;
	}

	/**
	 * 初始化当前Rest服务的方法映射字典
	 * 
	 * @param service
	 * @param name
	 */
	private void initCurrentServiceMethod(Object service, String name) {
		Class<?> clazz = service.getClass();
		Method[] methods = clazz.getMethods();
		Map<HttpMethodType, Method> restMethods = new HashMap<HttpMethodType, Method>(
				0);
		String methodName;
		HttpMethodType type = null;
		for (Method m : methods) {
			type = null;
			methodName = m.getName();
			if (methodName.equalsIgnoreCase("getClass") || m.isAnnotationPresent(Path.class))
				continue;

			if (m.isAnnotationPresent(Get.class)) {
				type = HttpMethodType.GET;
			} else if (m.isAnnotationPresent(Post.class)) {
				type = HttpMethodType.POST;
			} else if (m.isAnnotationPresent(Put.class)) {
				type = HttpMethodType.PUT;
			} else if (m.isAnnotationPresent(Delete.class)) {
				type = HttpMethodType.DELETE;
			} else {
				if (methodName.startsWith(RequestProcessor.METHOD_OF_GET)) {
					type = HttpMethodType.GET;
				} else if (methodName
						.startsWith(RequestProcessor.METHOD_OF_POST)) {
					type = HttpMethodType.POST;
				} else if (methodName
						.startsWith(RequestProcessor.METHOD_OF_PUT)) {
					type = HttpMethodType.PUT;
				} else if (methodName
						.startsWith(RequestProcessor.METHOD_OF_DELETE)) {
					type = HttpMethodType.DELETE;
				}
			}

			if (type != null)
				restMethods.put(type, m);
		}

		restServiceMethodMap.put(name, restMethods);
	}

	/**
	 * 根据服务方法中申明的返回类型与请求中的数据返回类型来输出数据到客户端，如果指定的返回类型不存在，则向客户端写回异常
	 * 
	 * @param charset
	 *            字符编码
	 * @param result
	 *            要输出的结果对象
	 * @param method
	 *            当前调用的服务方法
	 */
	private void writeResult(String charset, Object result, Method method) {
		// 获取客户端中的请求数据类型
		String accepts = request.getHeader("accept");
		if (accepts == null)
			accepts = "*/*";

		accepts = accepts.toLowerCase();

		// 缺省的数据返回类型
		String mimeType = accepts.split(",")[0];

		if (mimeType.equals(MimeType.MIME_OF_ALL))
			mimeType = MimeType.MIME_OF_JSON;

		// 获取服务方法上的数据返回类型
		if (method.isAnnotationPresent(ProduceMime.class)) {
			ProduceMime pmAnnotation = method.getAnnotation(ProduceMime.class);
			String[] mimeTypes = pmAnnotation.value();
			for (String mime : mimeTypes) {
				if (accepts.indexOf(mime) != -1) {
					mimeType = mime;
					break;
				}
			}
		}

		if (mimeType == null) {// 如果不存在指定的返回类型数据，系统向客户端写回异常
			result = new Exception("服务端没有提供{" + accepts + "}类型的数据返回");
			mimeType = MimeType.MIME_OF_ALL;
		}

		// 向客户端写回结果数据
		ResponseWriter responseWriter = ResponseWriterRegister.getInstance()
				.getResponseWriter(mimeType);
		if (responseWriter != null)
			responseWriter.writeResult(result, charset);
	}

	/**
	 * 转换参数值到指定的类型
	 * 
	 * @param value
	 * @param type
	 * @return
	 */
	private Object convertValue(Object value, Class type) {
		if (value == null)
			return null;

		if (type == Date.class) {
			value = new Date(value.toString());
		} else if (type == String.class) {
			value = value.toString();
		} else if (type == Timestamp.class) {
			value = Timestamp.valueOf(value.toString());
		} else if (type == Time.class) {
			value = Time.valueOf(value.toString());
		} else if (type == Long.class || type == long.class) {
			value = Long.valueOf(value.toString());
		} else if (type == Integer.class || type == int.class) {
			value = Integer.valueOf(value.toString());
		} else if (type == Float.class || type == float.class) {
			value = Float.valueOf(value.toString());
		} else if (type == Double.class || type == double.class) {
			value = Double.valueOf(value.toString());
		}

		return value;
	}
}
