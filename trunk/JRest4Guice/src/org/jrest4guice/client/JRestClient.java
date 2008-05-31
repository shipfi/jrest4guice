package org.jrest4guice.client;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest4guice.annotations.MimeType;

public class JRestClient {
	private final static Log log = LogFactory.getLog(JRestClient.class);
	
	public Object callRemote(String url,String methodType, Map<String, String> urlParam)
			throws Exception {
		HttpClient client = new HttpClient();
		HttpMethod method = initMethod(url,methodType,urlParam);
		//设置连接超时
		client.getHttpConnectionManager().getParams().setConnectionTimeout(3000); 
//		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1,false));
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
			log.error("连接错误: " + e.getMessage(),e);
			throw e;
		} finally {
			method.releaseConnection();
		}

		return responseBody;
	}

	private HttpMethod initMethod(String url,String methodType,Map<String, String> urlParam) {
		HttpMethod method = null;

		String queryString = "";
		if (urlParam != null) {
			Set<String> keySet = urlParam.keySet();
			List<String> params = new ArrayList<String>();
			for (String key : keySet) {
				params.add(key + "=" + urlParam.get(key));
			}
			queryString = StringUtils.join(params.toArray(), "&");
		}
		
		if(methodType.equalsIgnoreCase("get")){
			method = new GetMethod(url);
			method.setQueryString(queryString);
		}else if(methodType.equalsIgnoreCase("post")){
			method = new PostMethod(url);
			((PostMethod)method).setRequestBody(queryString);
		}else if(methodType.equalsIgnoreCase("put")){
			method = new PutMethod(url);
			((PutMethod)method).setRequestBody(queryString);
		}else if(methodType.equalsIgnoreCase("delete")){
			method = new DeleteMethod(url);
			method.setQueryString(queryString);
		}
			
		return method;
	}
}
