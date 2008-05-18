package org.jrest.sample.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.IOFileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jrest.core.fileupload.MonitoredDiskFileItem;
import org.jrest.core.fileupload.MonitoredDiskFileItemFactory;
import org.jrest.core.fileupload.UploadListener;

public class SampleFileUploadServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 495006880576776423L;

	@SuppressWarnings("unchecked")
	@Override
	public void service(ServletRequest servletReqest,
			ServletResponse servletResponse) throws IOException,
			ServletException {

		HttpServletRequest hRequest = (HttpServletRequest) servletReqest;
		
		System.out.println("上传的文件大小："+hRequest.getContentLength());
		
		MonitoredDiskFileItemFactory factory = new MonitoredDiskFileItemFactory(
				new UploadListener((hRequest), 10l));
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<MonitoredDiskFileItem> items = upload
					.parseRequest(hRequest);
			for (MonitoredDiskFileItem fileItem : items) {
				System.out
						.println(fileItem.getName() + "" + fileItem.getSize());
			}
		} catch (FileUploadException e) {
			if(!(e instanceof IOFileUploadException))
					e.printStackTrace();
		}
	}
}
