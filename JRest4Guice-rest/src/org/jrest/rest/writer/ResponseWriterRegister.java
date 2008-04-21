package org.jrest.rest.writer;

import java.util.HashMap;
import java.util.Map;

import org.jrest.core.guice.GuiceContext;

import com.google.inject.Singleton;

@Singleton
public class ResponseWriterRegister {

	private Map<String, ResponseWriter> responseWriters;

	public ResponseWriterRegister() {
		this.responseWriters = new HashMap<String, ResponseWriter>(0);
	}

	public ResponseWriterRegister registResponseWriter(String mimeType,
			ResponseWriter responseWriter) {
		this.responseWriters.put(mimeType, responseWriter);
		return this;
	}

	public ResponseWriter getResponseWriter(String mimeType) {
		return this.responseWriters.get(mimeType);
	}
	
	public static ResponseWriterRegister getInstance(){
		return GuiceContext.getInstance().getBean(ResponseWriterRegister.class);
	}
}
