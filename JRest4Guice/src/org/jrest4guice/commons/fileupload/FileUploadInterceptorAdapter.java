package org.jrest4guice.commons.fileupload;

import java.util.Map;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 *
 */
public class FileUploadInterceptorAdapter implements FileUploadInterceptor {
	@Override
	public String decorateFileName(Map<String,String> parameters, String fileName, String extName) {
		return null;
	}

	@Override
	public void onUploaded(Map<String,String> parameters) {
	}
}
