package org.jrest4guice.rest.reader;

import java.net.URLDecoder;

import org.jrest4guice.client.ModelMap;
import org.jrest4guice.rest.annotations.MimeType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings( { "unchecked" })
public class SimpleFormContentRader extends ByteArrayContentRader {

	@Override
	public String getContentType() {
		return MimeType.CONTENT_OF_APPLICATION_X_WWW_FORM_URLENCODED;
	}

	@Override
	protected void processByteArray(ModelMap params, byte[] bytes,
			String charset) {
		try {
			// URL解码
			String content = URLDecoder.decode(new String(bytes).trim(),
					charset);
			// 组装参数
			if (content != "") {
				String[] param_pairs = content.split("&");
				String[] kv;
				for (String p : param_pairs) {
					kv = p.split("=");
					if (kv.length > 1)
						params.put(kv[0], kv[1]);
				}
			}
		} catch (Exception e) {
		}
	}
}
