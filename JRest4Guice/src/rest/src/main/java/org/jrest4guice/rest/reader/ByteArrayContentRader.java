package org.jrest4guice.rest.reader;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.jrest4guice.client.ModelMap;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings( { "unchecked", "unused" })
public abstract class ByteArrayContentRader extends ParameterPairContentRader {
	@Override
	public void readData(HttpServletRequest request, ModelMap params,
			String charset) {

		super.readData(request, params, charset);

		// 以http body方式提交的参数
		try {
			ServletInputStream inputStream = request.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] b = new byte[4096];
			for (int n; (n = inputStream.read(b)) != -1;) {
				baos.write(b);
			}
			this.processByteArray(params, baos.toByteArray(), charset);
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void processByteArray(ModelMap params, byte[] bytes,
			String charset);
}
