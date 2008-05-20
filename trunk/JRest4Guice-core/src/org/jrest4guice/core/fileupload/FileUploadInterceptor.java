package org.jrest4guice.core.fileupload;

import java.util.Map;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public interface FileUploadInterceptor {
	public String decorateFileName(Map<String,String> parameters,String fileName, String extName);
	public void onUploaded(Map<String,String> parameters);
}
