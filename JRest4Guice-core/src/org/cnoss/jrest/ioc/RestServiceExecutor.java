package org.cnoss.jrest.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.cnoss.jrest.ResourceFilter;
import org.cnoss.jrest.annotation.FirstResult;
import org.cnoss.jrest.annotation.HttpMethod;
import org.cnoss.jrest.annotation.HttpMethodType;
import org.cnoss.jrest.annotation.MaxResults;
import org.cnoss.jrest.annotation.ModelBean;
import org.cnoss.jrest.annotation.RequestParameter;
import org.cnoss.jrest.util.ParameterNameDiscoverer;

@SuppressWarnings("unchecked")
public class RestServiceExecutor {
	private static Map<String, Map<HttpMethodType, Method>> restServiceBundles = new HashMap<String, Map<HttpMethodType, Method>>(
			0);

	/**
	 * 根据Rest服务的方法类型，执行相应的业务方法
	 * 
	 * @modelMap service
	 * @modelMap methodType
	 * @return
	 */
	public static Object execute(Object service, HttpMethodType methodType) {
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
					if(type == HttpMethodType.DEFAULT){
						String methodName = m.getName();
						if(methodName.startsWith(ResourceFilter.METHOD_OF_GET)){
							type = HttpMethodType.GET;
						}else if(methodName.startsWith(ResourceFilter.METHOD_OF_POST)){
							type = HttpMethodType.POST;
						}else if(methodName.startsWith(ResourceFilter.METHOD_OF_PUT)){
							type = HttpMethodType.PUT;
						}else if(methodName.startsWith(ResourceFilter.METHOD_OF_DELETE)){
							type = HttpMethodType.DELETE;
						}
					}
					restMethods.put(type, m);
				}
			}

			restServiceBundles.put(name, restMethods);
		}

		Method method = restServiceBundles.get(name).get(methodType);
		if(method == null)
			return null;
		
		ModelMap modelMap = IocContextManager.getModelMap();
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
						} else if (annotation instanceof FirstResult) {
							pName = ((FirstResult) annotation).name();
						} else if (annotation instanceof MaxResults) {
							pName = ((MaxResults) annotation).name();
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	private static Object convertValue(Object value, Class type) {
		if(value == null)
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
