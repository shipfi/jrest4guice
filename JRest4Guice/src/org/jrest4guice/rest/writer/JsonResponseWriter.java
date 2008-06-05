package org.jrest4guice.rest.writer;

import org.jrest4guice.rest.JRestResult;
import org.jrest4guice.rest.annotations.MimeType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class JsonResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		return JRestResult.createHttpResult(result).toJson();
	}

	@Override
	public String getMimeType() {
		return MimeType.MIME_OF_JSON;
	}
}
