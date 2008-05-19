package org.jrest.sample.fileupload;

import java.util.Map;
import java.util.UUID;

import org.jrest.core.fileupload.FileUploadInterceptorAdapter;

public class ImageFileUploadInterceptor extends FileUploadInterceptorAdapter {
	@Override
	public String decorateFileName(Map<String, String> parameters,
			String fileName, String extName) {
		if(!extName.startsWith("."))
			extName = "."+extName;
		String oldImgUrl = parameters.get("oldImgUrl");
		if (oldImgUrl != null && !oldImgUrl.trim().equals(""))
			return oldImgUrl;
		else
			return UUID.randomUUID()+extName;
	}
}
