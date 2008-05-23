package org.jrest4guice.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.jrest4guice.core.security.Role;


public class JRestClient {

	public JRestResult doGet(String url, Map<String, String> urlParam,Map classMap)
			throws Exception {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		String responseBody = null;
		try {
			
			if(urlParam != null){
				Set<String> keySet = urlParam.keySet();
				List<String> params = new ArrayList<String>();
				for(String key:keySet){
					params.add(key+"="+urlParam.get(key));
				}
				String queryString = StringUtils.join(params.toArray(),"&");
				method.setQueryString(queryString);
			}
			
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}

			// Read the response body.
			responseBody = method.getResponseBodyAsString();
			
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}

		if (responseBody != null) {
			try{
				Object bean = JSONObject.toBean(JSONObject.fromObject(responseBody), JRestResult.class,classMap);
				return (JRestResult) bean;
			}catch(Exception e){
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		JRestClient client = new JRestClient();
		Map<String, String> urlParam = new HashMap<String, String>();
		try {
			urlParam.put("userName", "cnoss");
			urlParam.put("userPassword", "123");
			Map classMap = new HashMap();
			classMap.put("content", Role.class);
			JRestResult result = client.doGet("http://localhost/JRest4Guice-sample/resource/userRole", urlParam,classMap);
			if(result != null){
				Boolean sucess = Boolean.valueOf(result.getContent().toString());
				System.out.println(sucess);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
