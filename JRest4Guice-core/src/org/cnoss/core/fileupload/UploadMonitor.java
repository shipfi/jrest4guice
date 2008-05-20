package org.cnoss.core.fileupload;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

public class UploadMonitor {
	@Inject
	private HttpServletRequest request;

	public UploadInfo getUploadInfo() {
		if (request.getSession().getAttribute("uploadInfo") != null)
			return (UploadInfo) request.getSession().getAttribute("uploadInfo");
		else
			return new UploadInfo();
	}
}
