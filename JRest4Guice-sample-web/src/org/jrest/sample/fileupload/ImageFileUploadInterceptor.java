package org.jrest.sample.fileupload;

import java.util.Map;

import org.jrest.core.fileupload.FileUploadInterceptorAdapter;

public class ImageFileUploadInterceptor extends FileUploadInterceptorAdapter {
	@Override
	public String decorateFileName(Map<String, String> parameters,
			String fileName, String extName) {
		if(!extName.startsWith("."))
			extName = "."+extName;
		String contactId = parameters.get("id");
		if (contactId != null)
			return contactId + extName;
		else
			return fileName;
	}
}
