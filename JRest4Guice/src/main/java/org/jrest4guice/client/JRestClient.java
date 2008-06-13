package org.jrest4guice.client;

import java.io.ByteArrayOutputStream;
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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest4guice.rest.annotations.MimeType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 *
 */
public class JRestClient {
	private final static Log log = LogFactory.getLog(JRestClient.class);
	
	private HttpClient client;
	public JRestClient(){
		this.client = new HttpClient();
	}

	public Object callRemote(String url, String methodType,
			ModelMap<String, Object> parameters) throws Exception {
		HttpMethod method = initMethod(url, methodType, parameters);
		// 设置连接超时
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(3000);
		// method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new
		// DefaultHttpMethodRetryHandler(1,false));
		Object responseBody = null;
		try {
			method.addRequestHeader("accept", MimeType.MIME_OF_JAVABEAN);

			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.error("调用Http方法出错: " + method.getStatusLine());
			}

			ObjectInputStream obj_in = new ObjectInputStream(method
					.getResponseBodyAsStream());
			responseBody = obj_in.readObject();

		} catch (Exception e) {
			log.error("连接错误: " + e.getMessage(), e);
			throw e;
		} finally {
			method.releaseConnection();
		}

		return responseBody;
	}

	private HttpMethod initMethod(String url, String methodType,
			ModelMap<String, Object> parameters) throws Exception {
		HttpMethod method = null;

		Object args = parameters!=null?parameters.get(ModelMap.RPC_ARGS_KEY):null;

		if (methodType.equalsIgnoreCase("get")) {
			method = new GetMethod(url);
		} else if (methodType.equalsIgnoreCase("post")) {
			method = new PostMethod(url);
			if (args != null) {
				byte[] output = constructArgs(method, args);
				((PostMethod) method).setRequestEntity(new ByteArrayRequestEntity(output));
			}
		} else if (methodType.equalsIgnoreCase("put")) {
			method = new PutMethod(url);
			if (args != null) {
				byte[] output = constructArgs(method, args);
				((PutMethod) method).setRequestEntity(new ByteArrayRequestEntity(output));
			}
		} else if (methodType.equalsIgnoreCase("delete")) {
			method = new DeleteMethod(url);
		}
		
		if (parameters != null) {
			Object value;
			List<String> queryStringList = new ArrayList<String>();
			Set<String> keySet = parameters.keySet();
			for (String key : keySet) {
				if (!key.toString().equalsIgnoreCase(ModelMap.RPC_ARGS_KEY)){
					value = parameters.get(key);
					method.getParams().setParameter(key, value);
					queryStringList.add(key+"="+value);
				}
			}
			
			if(methodType.equalsIgnoreCase("get"))
				method.setQueryString(StringUtils.join(queryStringList,"&"));
		}

		return method;
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
