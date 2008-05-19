package org.jrest.core.fileupload;

import java.util.Map;

public class FileUploadInterceptorAdapter implements FileUploadInterceptor {
	@Override
	public String decorateFileName(Map<String,String> parameters, String fileName, String extName) {
		return null;
	}

	@Override
	public void onUploaded(Map<String,String> parameters) {
	}
}
