package org.jrest4guice.core.fileupload;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class UploadListener implements OutputStreamListener {
	private HttpServletRequest request;

	private long startTime = 0;
	private int totalToRead = 0;
	private int totalBytesRead = 0;
	private int totalFiles = -1;

	public UploadListener(HttpServletRequest request) {
		this.request = request;
		this.totalToRead = request.getContentLength();
		this.startTime = System.currentTimeMillis();
	}

	public void start() {
		totalFiles++;
		updateUploadInfo("start");
	}

	public void bytesRead(int bytesRead) {
		totalBytesRead = totalBytesRead + bytesRead;
		updateUploadInfo("progress");
	}

	public void error(String message) {
		updateUploadInfo("error");
	}

	public void done() {
		updateUploadInfo("done");
	}

	private void updateUploadInfo(String status) {
		long delta = (System.currentTimeMillis() - startTime) / 1000;
		request.getSession().setAttribute(
				"uploadInfo",
				new UploadInfo(totalFiles, totalToRead, totalBytesRead, delta,
						status));
	}

}
