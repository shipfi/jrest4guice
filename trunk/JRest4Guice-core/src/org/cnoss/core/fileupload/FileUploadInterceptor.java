package org.cnoss.core.fileupload;

import java.util.Map;

public interface FileUploadInterceptor {
	public String decorateFileName(Map<String,String> parameters,String fileName, String extName);
	public void onUploaded(Map<String,String> parameters);
}
