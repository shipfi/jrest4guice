package org.jrest4guice.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class JRestClient {
	private final static Log log = LogFactory.getLog(JRestClient.class);

	private HttpClient httpClient;

	public JRestClient() {
		this.httpClient = new HttpClient();
		this.httpClient.getParams().setParameter(
				"http.protocol.allow-circular-redirects", true);
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	/**
	 * 将HttpClient与当前会员进行绑定，可以共享JAAS状态
	 * 
	 * @param jsessionId
	 */
	public void registJSessionId(String jsessionId) {
	}

	public Object callRemote(String url, String methodType,
			ModelMap<String, Object> parameters) throws Exception {
		HttpMethod method = initMethod(url, methodType, parameters);
		// 设置连接超时
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				3000);
		Object responseBody = null;
		try {
			method.addRequestHeader("accept", "application/javabean");
			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.error("调用Http方法出错: " + method.getStatusLine());
			}

			try {
				ObjectInputStream obj_in = new ObjectInputStream(method
						.getResponseBodyAsStream());
				responseBody = obj_in.readObject();
			} catch (Exception e) {
				responseBody = method.getResponseBodyAsString();
			}

			if (responseBody instanceof Exception) {
				throw (Exception) responseBody;
			}

		} catch (Exception e) {
			log.error("连接错误: " + e.getMessage(), e);
			throw e;
		} finally {
			method.releaseConnection();
		}

		return responseBody;
	}

	/**
	 * 构造http方法
	 * @param url
	 * @param methodType
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	private HttpMethod initMethod(String url, String methodType,
			ModelMap<String, Object> parameters) throws Exception {
		HttpMethod method = null;

		Object args = parameters != null ? parameters
				.get(ModelMap.RPC_ARGS_KEY) : null;
		
		if (methodType.equalsIgnoreCase("get")) {
			method = new GetMethod(url);
		} else if (methodType.equalsIgnoreCase("post")) {
			PostMethod postMethod = new PostMethod(url);
			if (args != null) {
				byte[] output = constructArgs(method, args);
				postMethod.setRequestEntity(new ByteArrayRequestEntity(output));
			}
			
			//处理多文件上传
			this.processMultipartRequest(parameters, postMethod);

			method = postMethod;
		} else if (methodType.equalsIgnoreCase("put")) {
			method = new PutMethod(url);
			if (args != null) {
				byte[] output = constructArgs(method, args);
				((PutMethod) method)
						.setRequestEntity(new ByteArrayRequestEntity(output));
			}
		} else if (methodType.equalsIgnoreCase("delete")) {
			method = new DeleteMethod(url);
		}

		if (parameters.get(ModelMap.RPC_ARGS_KEY) != null)
			method.addRequestHeader("content-type", "application/javabean");

		if (parameters != null) {
			Object value;
			List<String> queryStringList = new ArrayList<String>();
			Set<String> keySet = parameters.keySet();
			for (String key : keySet) {
				if (!key.toString().equalsIgnoreCase(ModelMap.RPC_ARGS_KEY) && !key.toString().equalsIgnoreCase(ModelMap.FILE_ITEM_ARGS_KEY)) {
					value = parameters.get(key);
					method.getParams().setParameter(key, value);
					queryStringList.add(key + "=" + value);
				}
			}

			if (methodType.equalsIgnoreCase("get"))
				method.setQueryString(StringUtils.join(queryStringList, "&"));
		}
		return method;
	}

	@SuppressWarnings("unchecked")
	/**
	 * 处理多文件上传
	 */
	private void processMultipartRequest(ModelMap<String, Object> parameters,
			PostMethod postMethod) throws FileNotFoundException {
		Object _files = parameters.get(ModelMap.FILE_ITEM_ARGS_KEY);
		if (_files != null) {
			File[] fileArray = null; 
			if(_files.getClass().isArray()){
				fileArray = (File[])_files;
			}else if (_files instanceof List){
				fileArray = ((List<File>)_files).toArray(new File[]{});
			}
			List<Part> parts = new ArrayList<Part>();
			int index = 0;
			for (File f : fileArray) {
				parts.add( new FilePart("file_"+index++,f.getName(), f));
			}
			postMethod.setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[]{}),postMethod.getParams()));
		}
	}

	private byte[] constructArgs(HttpMethod method, Object args)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream obj_out = new ObjectOutputStream(bos);
		obj_out.writeObject(args);
		byte[] output = bos.toByteArray();
		return output;
	}
}
