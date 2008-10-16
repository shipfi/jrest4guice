package org.jrest4guice.rest.reader;

import org.jrest4guice.client.ModelMap;
import org.jrest4guice.rest.annotations.MimeType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings({"unchecked"})
public class JavaBeanContentRader extends ByteArrayContentRader {

	@Override
	public String getContentType() {
		return MimeType.MIME_OF_JAVABEAN;
	}

	@Override
	protected void processByteArray(ModelMap params,byte[] bytes,String charset) {
		params.put(ModelMap.RPC_ARGS_KEY, bytes);
	}
}
