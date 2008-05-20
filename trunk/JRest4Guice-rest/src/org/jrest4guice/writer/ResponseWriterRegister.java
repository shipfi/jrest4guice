package org.jrest4guice.writer;

import java.util.HashMap;
import java.util.Map;

import org.jrest4guice.core.guice.GuiceContext;

import com.google.inject.Singleton;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
@Singleton
public class ResponseWriterRegister {

	private static Map<String, Class<ResponseWriter>> responseWriters = new HashMap<String, Class<ResponseWriter>>(0);

	public ResponseWriterRegister registResponseWriter(String mimeType,
			Class<ResponseWriter> responseWriter) {
		responseWriters.put(mimeType, responseWriter);
		return this;
	}

	public ResponseWriter getResponseWriter(String mimeType) {
		Class<ResponseWriter> clazz = responseWriters.get(mimeType);
		if(clazz != null)
			return  GuiceContext.getInstance().getBean(clazz);
		else
			return null;
	}
	
	public static ResponseWriterRegister getInstance(){
		return GuiceContext.getInstance().getBean(ResponseWriterRegister.class);
	}
}
