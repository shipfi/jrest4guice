package org.jrest4guice.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.client.Page;
import org.jrest4guice.commons.i18n.annotations.ResourceBundle;
import org.jrest4guice.commons.lang.ParameterNameDiscoverer;
import org.jrest4guice.persistence.ParameterObject;
import org.jrest4guice.rest.annotations.Action;
import org.jrest4guice.rest.annotations.BodyBytes;
import org.jrest4guice.rest.annotations.Cache;
import org.jrest4guice.rest.annotations.Element;
import org.jrest4guice.rest.annotations.FileItems;
import org.jrest4guice.rest.annotations.HttpMethodType;
import org.jrest4guice.rest.annotations.MapBean;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.annotations.ModelBean;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.annotations.Parameter;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.ProduceMime;
import org.jrest4guice.rest.commons.cache.ResourceCacheManager;
import org.jrest4guice.rest.exception.Need2RedirectException;
import org.jrest4guice.rest.exception.ValidatorException;
import org.jrest4guice.rest.helper.JRestGuiceProcessorHelper;
import org.jrest4guice.rest.helper.RequestHelper;
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
	protected HttpServletResponse response;

	private static Map<String, Map<String, Method>> restServiceMethodMap = new HashMap<String, Map<String, Method>>(
			0);

	private static Map<Method, String[]> paramNameMap = new HashMap<Method, String[]>(
			0);

	private static ResponseWriterRegister responseWriterRegister;

	public static final String PARAMETER_CACHED_KEY = "_$_param_cached_key_$_";

	private Map options;

	public ServiceExecutor() {
		if (responseWriterRegister == null)
			responseWriterRegister = ResponseWriterRegister.getInstance();

		this.options = new HashMap(0);
	}

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

		Method method = service.getMethod().get(methodType);
		if (method == null)
			method = this.try2GetMethod(name, methodType);

		if (method == null){
			return;
		}

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

				// 清除分页查询处理的参数缓存
				if (methodType == HttpMethodType.POST
						&& method.isAnnotationPresent(PageFlow.class)) {
					String cahced_key = PARAMETER_CACHED_KEY
							+ method.getDeclaringClass().getName();
					HttpSession session = this.request.getSession();
					session.removeAttribute(cahced_key);
				}

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
	
	private Method try2GetMethod(String name,HttpMethodType methodType){
		String methodTypeName = methodType.name();
		if(methodType == HttpMethodType.ACTION){
			String uri = this.request.getRequestURI();
			String action = uri.substring(uri.lastIndexOf("!")+1).trim();
			methodTypeName = methodType.name()+":"+action;
		}
		return restServiceMethodMap.get(name).get(methodTypeName);
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
		List params = new ArrayList(0);
		Annotation[][] annotationArray = method.getParameterAnnotations();

		if (annotationArray.length <= 0)
			return params;

		Class[] parameterTypes = method.getParameterTypes();
		Class<?> returnType = method.getReturnType();
		boolean isPageResult = Page.class.isAssignableFrom(returnType);
		int nullParamCount = 0;

		String pName;
		Object value;
		int index = 0;

		String[] parameterNames = null;
		parameterNames = paramNameMap.get(method);
		if (parameterNames == null) {
			ParameterNameDiscoverer pnDiscoverer = new ParameterNameDiscoverer();
			parameterNames = pnDiscoverer.getParameterNames(method);
			paramNameMap.put(method, parameterNames);
		}

		boolean isModelBean = false;
		boolean cached = false;
		for (Annotation[] annotations : annotationArray) {
			value = null;
			pName = parameterNames[index];
			isModelBean = cached = false;
			for (Annotation annotation : annotations) {
				if (annotation instanceof Parameter) {
					pName = ((Parameter) annotation).value();
				} else if (annotation instanceof ModelBean) {
					value = parameterTypes[index].newInstance();
					BeanUtils.populate(value, modelMap);
					isModelBean = true;
				} else if (annotation instanceof MapBean) {
					value = this.constructMapBean((MapBean)annotation, modelMap);
				} else if (annotation instanceof FileItems) {
					pName = ModelMap.FILE_ITEM_ARGS_KEY;
					value = modelMap.get(pName);
				} else if (annotation instanceof BodyBytes) {
					pName = ModelMap.BYTE_ARRAY_ARGS_KEY;
					value = modelMap.get(pName);
				} else if (annotation instanceof Cache) {
					cached = true;
				}
			}

			// 转换参数值
			if (value == null) {
				String pValue = (String) modelMap.get(pName);
				value = BeanUtilsBean.getInstance().getConvertUtils().convert(
						pValue, parameterTypes[index]);
				if (pValue == null)
					nullParamCount++;
			}

			if (cached && value != null) {
				this.options.put(pName, value);
			}

			if (isModelBean) {
				// 启用验证
				Class<?> modelClass = value.getClass();
				java.util.ResourceBundle rb = null;
				if (modelClass.isAnnotationPresent(ResourceBundle.class))
					rb = java.util.ResourceBundle.getBundle(modelClass
							.getAnnotation(ResourceBundle.class).value());
				ClassValidator validator = new ClassValidator(modelClass, rb);
				InvalidValue[] invalidValues = validator
						.getInvalidValues(value);
				if (invalidValues != null && invalidValues.length > 0) {
					throw new ValidatorException(invalidValues);
				}
			}

			// 添加当前参数
			params.add(value);

			index++;
		}

		// 提供对分页查询处理的参数缓存功能
		if (isPageResult && method.isAnnotationPresent(PageFlow.class)) {
			String cahced_key = PARAMETER_CACHED_KEY
					+ method.getDeclaringClass().getName();
			HttpSession session = this.request.getSession();
			if (nullParamCount == parameterNames.length) {
				Object cachedParam = session.getAttribute(cahced_key);
				if (cachedParam != null) {
					params = (List) cachedParam;
				}
			}
			session.setAttribute(cahced_key, params);
		}

		return params;
	}
	
	private Map<String, Object> constructMapBean(MapBean annotation,ModelMap<String, Object> modelMap){
		Element[] elements = annotation.value();
		if(elements.length<1)
			return null;
		
		Map<String, Object> result = new HashMap<String, Object>();
		Object value;
		String key,targetName;
		for(Element elem:elements){
			key = elem.name().trim();
			if(key.equals(""))
				continue;
			targetName = key;
			value = modelMap.get(key);
			if(value== null || value.toString().trim().equals(""))
				continue;

			if(!elem.targetName().trim().equals(""))
				targetName = elem.targetName().trim();

			result.put(key, new ParameterObject(targetName,value,elem.logic(),elem.relation(),elem.dataType()));
		}
		
		return result;
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
		Map<String, Method> restMethods = new HashMap<String, Method>(
				0);
		String methodName;
		HttpMethodType type = null;
		for (Method m : methods) {
			type = null;
			methodName = m.getName();
			if (methodName.equalsIgnoreCase("getClass")
					|| m.isAnnotationPresent(Path.class))
				continue;

			type = JRestGuiceProcessorHelper.getHttpMethodType(m);

			if (type != null){
				if(type == HttpMethodType.ACTION) {
					String value = m.getAnnotation(Action.class).value();
					if(value.trim().equals(""))
						value = m.getName();
					restMethods.put(type.name()+":"+value, m);
				} else
					restMethods.put(type.name(), m);
			}
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
		String accept = RequestHelper.getAccepte(this.request);
		String mimeType = RequestHelper.getMimeType(this.request);

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

		if (mimeType == null) {// 如果不存在指定的返回类型数据，系统向客户端写回异常
			result = new Exception("服务端没有提供{" + accept + "}类型的数据返回");
			mimeType = MimeType.MIME_OF_ALL;
		}

		// 向客户端写回结果数据
		ResponseWriter responseWriter = responseWriterRegister
				.getResponseWriter(mimeType);
		if (responseWriter != null) {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				//将结果写入缓冲区
				responseWriter.writeResult(method, out, result, charset, this.options);
				
				final byte[] bytes = out.toByteArray();
				if (bytes.length > 0) {
					//将结果写回客户端
					response.setCharacterEncoding(charset);
					response.setContentType(responseWriter.getMimeType()+";charset="+charset);
					response.getOutputStream().write(bytes);
					
					//如果方法打开了缓存声明，则将结果缓存到服务器
					if (method.isAnnotationPresent(Cache.class)) {
						ResourceCacheManager.getInstance().cacheStaticResource(
								RestContextManager.getCurrentRestUri(),
								responseWriter.getMimeType(), bytes, request);
					}
				}
			} catch (Need2RedirectException e) {
				try {
					response.sendRedirect(e.getRedirectUrl());
				} catch (IOException ioE) {
					throw new RuntimeException("重定向到\""+e.getRedirectUrl()+"\"错误",ioE);
				}
			} catch (Exception e) {
				throw new RuntimeException("向客户端写回信息错误"+e,e);
			}
		}
	}
}
