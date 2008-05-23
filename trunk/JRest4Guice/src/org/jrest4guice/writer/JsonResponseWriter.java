package org.jrest4guice.writer;

import org.jrest4guice.annotation.MimeType;
import org.jrest4guice.client.JRestResult;

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
