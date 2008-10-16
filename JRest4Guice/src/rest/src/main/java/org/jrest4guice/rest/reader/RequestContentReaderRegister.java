package org.jrest4guice.rest.reader;

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
public class RequestContentReaderRegister {

	private static Map<String, Class<RequestContentReader>> contentReaders = new HashMap<String, Class<RequestContentReader>>(
			0);

	public RequestContentReaderRegister registResponseWriter(String contentType,
			Class<RequestContentReader> contentReader) {
		contentReaders.put(contentType, contentReader);
		return this;
	}

	public RequestContentReader getRequestReader(String contentType) {
		RequestContentReader bean = null;
		Class<RequestContentReader> clazz = contentReaders.get(contentType);
		if (clazz != null) {
			bean = GuiceContext.getInstance().getBean(clazz);
		}
		return bean;
	}

	public static RequestContentReaderRegister getInstance() {
		return GuiceContext.getInstance().getBean(RequestContentReaderRegister.class);
	}
}
