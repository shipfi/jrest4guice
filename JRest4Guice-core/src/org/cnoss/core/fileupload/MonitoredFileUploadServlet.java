package org.cnoss.core.fileupload;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.IOFileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

@SuppressWarnings( { "unused", "unchecked" })
public class MonitoredFileUploadServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 495006880576776423L;

	// 文件上传的目标路径
	private String uploadPath = "/upload";
	// 是否是绝对路径
	private boolean isAbsolute = false;
	// 单个文件的最大上传大小：(缺省1M)
	private long fileSizeMax = 1024 * 1024;
	// 整个request的最大大小：(缺省10M)
	private long sizeMax = fileSizeMax * 10;
	// 允许上传的文件类型
	private Set<String> fileTypeAllowed;
	// 上传完成后跳转的地址: (缺省upload.html)
	private String finishUrl = "upload.html";
	// 文件上传的拦截器
	private FileUploadInterceptor interceptor;

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.fileTypeAllowed = new HashSet<String>(0);
		String param = config.getInitParameter("uploadPath");
		if (param != null && !param.trim().equals(""))
			this.uploadPath = param;

		param = config.getInitParameter("isAbsolute");
		if (param != null && !param.trim().equals(""))
			this.isAbsolute = param.equalsIgnoreCase("true");

		param = config.getInitParameter("fileSizeMax");
		if (param != null && !param.trim().equals(""))
			this.fileSizeMax = Long.parseLong(param);

		param = config.getInitParameter("sizeMax");
		if (param != null && !param.trim().equals(""))
			this.sizeMax = Long.parseLong(param);

		param = config.getInitParameter("fileTypeAllowed");
		if (param != null && !param.trim().equals("")) {
			String params[] = param.split(",");
			for (String p : params)
				this.fileTypeAllowed.add(p);
		}

		param = config.getInitParameter("interceptor");
		if (param != null && !param.trim().equals("")) {
			try {
				this.interceptor = (FileUploadInterceptor) Class.forName(param)
						.newInstance();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void destroy() {
		this.fileTypeAllowed.clear();
		this.fileTypeAllowed = null;
	}

	@Override
	public void service(ServletRequest servletReqest,
			ServletResponse servletResponse) throws IOException,
			ServletException {

		HttpServletRequest hRequest = (HttpServletRequest) servletReqest;
		// 构建带上传进度监视的文件工厂
		MonitoredDiskFileItemFactory factory = new MonitoredDiskFileItemFactory(
				new UploadListener(hRequest));
		// 初始化上传参数
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(this.fileSizeMax);
		upload.setSizeMax(this.sizeMax);
		upload.setHeaderEncoding("UTF-8");

		Set<String> fileNames = new HashSet<String>();
		try {
			List<MonitoredDiskFileItem> items = upload.parseRequest(hRequest);
			long size;
			String fileName, extName;
			// 上传的路径
			File target = new File(this.isAbsolute ? this.uploadPath : hRequest
					.getRealPath(this.uploadPath));
			if (!target.exists())
				target.mkdirs();

			Map<String, String> parameters = new HashMap<String, String>();
			for (MonitoredDiskFileItem fileItem : items) {
				if (fileItem.isFormField())
					parameters.put(fileItem.getFieldName(), fileItem
							.getString());
			}

			// 处理上传的文件
			for (MonitoredDiskFileItem fileItem : items) {
				fileName = fileItem.getName();
				if (fileName==null || fileName.trim().equals(""))// 如果没有指定上传的文件
					continue;
				int index = fileName.lastIndexOf(File.separator);
				if (index != -1)
					index++;
				else
					index = 0;
				fileName = fileName.substring(index).toLowerCase();

				extName = fileName.substring(fileName.lastIndexOf("."))
						.toLowerCase();
				size = fileItem.getSize();
				if (this.fileTypeAllowed.size() > 0
						&& !this.fileTypeAllowed.contains(extName)) {

					// TODO 处理不允许的上传文件类型
					continue;
				}

				// 将文件写入磁盘
				if (fileItem != null && size > 0) {
					// 处理保存到磁盘的文件名
					if (this.interceptor != null)
						fileName = this.interceptor.decorateFileName(
								parameters, fileName, extName);

					fileNames.add(fileName);
					fileName = target.getPath() + File.separator + fileName;
					// 写入文件
					fileItem.write(new File(fileName));
				}
			}

			//处理上传完毕后的业务逻辑
			if (this.interceptor != null)
				this.interceptor.onUploaded(parameters);
		} catch (Exception e) {
			if (e instanceof SizeLimitExceededException) {
				System.out.println("上传的文件大小超过许可限制，最大为:"
						+ ((SizeLimitExceededException) e).getPermittedSize());
			} else if (e instanceof FileSizeLimitExceededException) {
				System.out.println("上传的文件数量超过许可限制，最大为:"
						+ ((FileSizeLimitExceededException) e)
								.getPermittedSize());
			} else if (!(e instanceof IOFileUploadException)) {
				System.out.println("上传的文件错误");
				System.out
						.println("========================================================");
				e.printStackTrace();
				System.out
						.println("========================================================");
			}
		} finally {
			HttpServletResponse hResponse = (HttpServletResponse) servletResponse;
			String fileUrl = StringUtils.join(fileNames, ",");
			hResponse.sendRedirect(this.finishUrl
					+ "?fileUrl='" + fileUrl + "'");
		}
	}
}
