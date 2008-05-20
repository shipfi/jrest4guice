package org.jrest4guice.core.fileupload;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
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
