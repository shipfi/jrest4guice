package org.jrest4guice.rest.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Map;

import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.exception.Need2RedirectException;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class JavaBeanResponseWriter implements ResponseWriter {
	
	/* (non-Javadoc)
	 * @see org.jrest4guice.ResponseWriter#writeResult(javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.String)
	 */
	public void writeResult(Method method,ByteArrayOutputStream out, Object result,Map options) throws Need2RedirectException {
		try {
			ObjectOutputStream obj_out = new ObjectOutputStream(out);
			obj_out.writeObject(result);
			obj_out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getMimeType() {
		return MimeType.MIME_OF_JAVABEAN;
	}
}
