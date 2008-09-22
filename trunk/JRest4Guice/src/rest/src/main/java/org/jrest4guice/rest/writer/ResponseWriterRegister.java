package org.jrest4guice.rest.writer;

import java.util.HashMap;
import java.util.Map;

import org.jrest4guice.guice.GuiceContext;

import com.google.inject.Singleton;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Singleton
public class ResponseWriterRegister {

	private static Map<String, Class<ResponseWriter>> responseWriters = new HashMap<String, Class<ResponseWriter>>(0);
	private static Map<String, ResponseWriter> responseWriterInstances = new HashMap<String, ResponseWriter>(0);

	public ResponseWriterRegister registResponseWriter(String mimeType,
			Class<ResponseWriter> responseWriter) {
		responseWriters.put(mimeType, responseWriter);
		return this;
	}

	public ResponseWriter getResponseWriter(String mimeType) {
		ResponseWriter bean = responseWriterInstances.get(mimeType);
		if(bean == null){
			Class<ResponseWriter> clazz = responseWriters.get(mimeType);
			if(clazz != null) {
				bean = GuiceContext.getInstance().getBean(clazz);
				responseWriterInstances.put(mimeType, bean);
			}
		}else{
			GuiceContext.getInstance().injectorMembers(bean);
		}
		return bean;
	}
	
	public static ResponseWriterRegister getInstance(){
		return GuiceContext.getInstance().getBean(ResponseWriterRegister.class);
	}
}
