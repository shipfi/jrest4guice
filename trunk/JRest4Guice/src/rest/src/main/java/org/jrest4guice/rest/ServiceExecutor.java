package org.jrest4guice.rest;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.commons.lang.ParameterNameDiscoverer;
import org.jrest4guice.rest.annotations.Delete;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.HttpMethodType;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.annotations.ModelBean;
import org.jrest4guice.rest.annotations.Parameter;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.Post;
import org.jrest4guice.rest.annotations.ProduceMime;
import org.jrest4guice.rest.annotations.Put;
import org.jrest4guice.rest.annotations.RESTful;
import org.jrest4guice.rest.context.RestContextManager;
import org.jrest4guice.rest.exception.ValidatorException;
import org.jrest4guice.rest.writer.ResponseWriter;
import org.jrest4guice.rest.writer.ResponseWriterRegister;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
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
			String charset, boolean isRpc) throws Throwable {
		Object result = null;
		Object instance = service.getInstance();
		String name = instance.getClass().getName();
		if (!restServiceMethodMap.containsKey(name)) {
			// 初始化当前Rest服务的方法映射字典
			this.initCurrentServiceMethod(instance, name);
		}

		Method method = service.getMethod();
		if (method == null)
			method = restServiceMethodMap.get(name).get(methodType);

		if (method == null)
			return;

		Exception exception = null;

		ModelMap modelMap = RestContextManager.getModelMap();
		if (method != null) {
			try {
				Object[] args = null;
				// 构造参数集合
				if (!isRpc) {
					List params = constructParams(method, modelMap);
					args = params.size() > 0 ? params.toArray() : null;
				} else {
					byte[] bytes = (byte[]) modelMap.get(ModelMap.RPC_ARGS_KEY);
					ObjectInputStream obj_in = new ObjectInputStream(
							new ByteArrayInputStream(bytes));
					args = (Object[]) obj_in.readObject();
				}

				// 执行业务方法
				result = method.invoke(instance, args);
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
					throwable = ((InvocationTargetException) exception)
							.getTargetException();
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
		Class[] parameterTypes = method.getParameterTypes();

		String pName;
		List params = new ArrayList(0);
		Object value;
		int index = 0;

		ParameterNameDiscoverer pnDiscoverer = new ParameterNameDiscoverer();
		String[] parameterNames = pnDiscoverer.getParameterNames(method);

		boolean isModelBean = false;
		for (Annotation[] annotations : annotationArray) {
			value = null;
			pName = parameterNames[index];
			isModelBean = false;
			for (Annotation annotation : annotations) {
				if (annotation instanceof Parameter) {
					pName = ((Parameter) annotation).value();
				} else if (annotation instanceof ModelBean) {
					value = parameterTypes[index].newInstance();
					BeanUtils.populate(value, modelMap);
					isModelBean = true;
				}
			}

			// 转换参数值
			if (value == null)
				value = BeanUtilsBean.getInstance().getConvertUtils().convert(
						(String) modelMap.get(pName), parameterTypes[index]);
			
			if(isModelBean){
				//启用验证
				ClassValidator validator = new ClassValidator(value.getClass());
				InvalidValue[] invalidValues = validator.getInvalidValues(value);
				if(invalidValues != null && invalidValues.length>0){
					throw new ValidatorException(invalidValues);
				}
			}
			
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
			if (methodName.equalsIgnoreCase("getClass")
					|| m.isAnnotationPresent(Path.class))
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
				if (methodName.startsWith(RESTful.METHOD_OF_GET)) {
					type = HttpMethodType.GET;
				} else if (methodName.startsWith(RESTful.METHOD_OF_POST)) {
					type = HttpMethodType.POST;
				} else if (methodName.startsWith(RESTful.METHOD_OF_PUT)) {
					type = HttpMethodType.PUT;
				} else if (methodName.startsWith(RESTful.METHOD_OF_DELETE)) {
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
		String accept = RequestUtil.getAccepte(this.request);
		String mimeType = RequestUtil.getMimeType(this.request);

		// 获取服务方法上的数据返回类型
		if (method.isAnnotationPresent(ProduceMime.class)) {
			ProduceMime pmAnnotation = method.getAnnotation(ProduceMime.class);
			String[] mimeTypes = pmAnnotation.value();
			for (String mime : mimeTypes) {
				if (accept.indexOf(mime) != -1) {
					mimeType = mime;
					break;
				}
			}
		}

		method.getDeclaringClass().getName();

		if (mimeType == null) {// 如果不存在指定的返回类型数据，系统向客户端写回异常
			result = new Exception("服务端没有提供{" + accept + "}类型的数据返回");
			mimeType = MimeType.MIME_OF_ALL;
		}

		// 向客户端写回结果数据
		ResponseWriter responseWriter = ResponseWriterRegister.getInstance()
				.getResponseWriter(mimeType);
		if (responseWriter != null)
			responseWriter.writeResult(method, result, charset);
	}
}
