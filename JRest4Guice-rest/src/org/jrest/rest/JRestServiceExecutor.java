package org.jrest.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.jrest.core.util.ParameterNameDiscoverer;
import org.jrest.rest.annotation.HttpMethod;
import org.jrest.rest.annotation.HttpMethodType;
import org.jrest.rest.annotation.MimeType;
import org.jrest.rest.annotation.ModelBean;
import org.jrest.rest.annotation.ProduceMime;
import org.jrest.rest.annotation.RequestParameter;
import org.jrest.rest.http.HttpContextManager;
import org.jrest.rest.http.ModelMap;
import org.jrest.rest.writer.ResponseWriterRegister;

import com.google.inject.Inject;

@SuppressWarnings("unchecked")
public class JRestServiceExecutor {
	@Inject
	private HttpServletRequest request;

	private static Map<String, Map<HttpMethodType, Method>> restServiceBundles = new HashMap<String, Map<HttpMethodType, Method>>(
			0);

	/**
	 * 根据Rest服务的方法类型，执行相应的业务方法
	 * 
	 * @modelMap service
	 * @modelMap methodType
	 * @return
	 */
	public Object execute(Object service, HttpMethodType methodType,
			String charset) {
		Object result = null;
		String name = service.getClass().getName();
		if (!restServiceBundles.containsKey(name)) {
			Class<?> clazz = service.getClass();
			Method[] methods = clazz.getMethods();
			Map<HttpMethodType, Method> restMethods = new HashMap<HttpMethodType, Method>(
					0);
			for (Method m : methods) {
				if (m.isAnnotationPresent(HttpMethod.class)) {
					HttpMethod annotation = m.getAnnotation(HttpMethod.class);
					HttpMethodType type = annotation.type();
					if (type == HttpMethodType.DEFAULT) {
						String methodName = m.getName();
						if (methodName
								.startsWith(RequestProcessor.METHOD_OF_GET)) {
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
					restMethods.put(type, m);
				}
			}

			restServiceBundles.put(name, restMethods);
		}

		Method method = restServiceBundles.get(name).get(methodType);
		if (method == null)
			return null;

		ModelMap modelMap = HttpContextManager.getModelMap();
		if (method != null) {
			try {
				Annotation[][] annotationArray = method
						.getParameterAnnotations();
				Class<?>[] parameterTypes = method.getParameterTypes();

				String pName;
				List params = new ArrayList(0);
				Object value;
				int index = 0;

				ParameterNameDiscoverer pnDiscoverer = new ParameterNameDiscoverer();
				String[] parameterNames = pnDiscoverer
						.getParameterNames(method);

				for (Annotation[] annotations : annotationArray) {
					value = null;
					pName = parameterNames[index];
					for (Annotation annotation : annotations) {
						if (annotation instanceof RequestParameter) {
							pName = ((RequestParameter) annotation).value();
						} else if (annotation instanceof ModelBean) {
							value = parameterTypes[index].newInstance();
							BeanUtils.populate(value, modelMap);
						}
					}

					// 转换参数值
					if (value == null)
						value = convertValue(modelMap.get(pName),
								parameterTypes[index]);
					// 添加当前参数
					params.add(value);

					index++;
				}

				// 执行业务方法
				result = method.invoke(service, params.size() > 0 ? params
						.toArray() : null);
				writeResult(charset, result, method);

			} catch (Exception e) {
				if(e instanceof java.lang.IllegalArgumentException){
					writeResult(charset, "调用"+method.getName()+"时出错: 原因是参数不完整!", method);
				}else
					writeResult(charset, e.getMessage(), method);
			}
		}

		return result;
	}

	/**
	 * 根据服务方法中申明的返回类型与请求中的数据返回类型来输出数据到客户端，如果指定的返回类型不存在，则向客户端写回异常
	 * 
	 * @param charset
	 *            字符编码
	 * @param result
	 *            要写加的结果
	 * @param method
	 *            当前调用的服务方法
	 */
	private void writeResult(String charset, Object result, Method method) {
		Class<?> returnType = method.getReturnType();
		if (returnType.getName().toLowerCase().equals("void")) {
			return;
		}

		// 缺省的返回类型是JSON
		String mimeType = MimeType.MIME_OF_JSON;

		// 获取客户端中的请求数据类型
		String accepts = request.getHeader("accept").toLowerCase();

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
			result = "服务端没有提供{" + accepts + "}类型的数据返回";
			mimeType = MimeType.MIME_OF_ALL;
		}

		// 向客户端写回结果数据
		ResponseWriterRegister.getInstance().getResponseWriter(mimeType)
				.writeResult(result, charset);
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
