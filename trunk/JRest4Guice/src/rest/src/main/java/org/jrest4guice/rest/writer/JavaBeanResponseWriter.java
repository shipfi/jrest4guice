package org.jrest4guice.rest.writer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest4guice.rest.annotations.MimeType;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class JavaBeanResponseWriter implements ResponseWriter {
	
	@Inject
	private HttpServletResponse response;
	
	/* (non-Javadoc)
	 * @see org.jrest4guice.ResponseWriter#writeResult(javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.String)
	 */
	public void writeResult(Method method,Object result,String charset) {
		try {
			ObjectOutputStream obj_out = new ObjectOutputStream(response.getOutputStream());
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
