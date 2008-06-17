package org.jrest4guice.commons.fileupload;

import java.util.Map;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public interface FileUploadInterceptor {
	public String decorateFileName(Map<String,String> parameters,String fileName, String extName);
	public void onUploaded(Map<String,String> parameters);
}
