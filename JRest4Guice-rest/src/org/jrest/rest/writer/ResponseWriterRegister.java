package org.jrest.rest.writer;

import java.util.HashMap;
import java.util.Map;

import org.jrest.core.guice.GuiceContext;

import com.google.inject.Singleton;

@Singleton
public class ResponseWriterRegister {

	private static Map<String, ResponseWriter> responseWriters = new HashMap<String, ResponseWriter>(0);

	public ResponseWriterRegister registResponseWriter(String mimeType,
			ResponseWriter responseWriter) {
		responseWriters.put(mimeType, responseWriter);
		return this;
	}

	public ResponseWriter getResponseWriter(String mimeType) {
		return responseWriters.get(mimeType);
	}
	
	public static ResponseWriterRegister getInstance(){
		return GuiceContext.getInstance().getBean(ResponseWriterRegister.class);
	}
}
