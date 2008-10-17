package org.jrest4guice.rest.reader;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.commons.fileupload.MonitoredDiskFileItemFactory;
import org.jrest4guice.commons.fileupload.UploadListener;
import org.jrest4guice.rest.annotations.MimeType;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings( { "unchecked" })
public class MultipartFormDataContentRader extends ParameterPairContentRader {

	public static final String FILE_SIZE_MAX = "fileUpload:fileSizeMax";
	public static final String SIZE_MAX = "fileUpload:sizeMax";

	@Inject
	@Named(FILE_SIZE_MAX)
	int fileSizeMax = 1024 * 1024 * 10;
	@Inject
	@Named(SIZE_MAX)
	int sizeMax = fileSizeMax * 10;

	@Override
	public String getContentType() {
		return MimeType.CONTENT_OF_MULTIPART_FORM_DATA;
	}

	@Override
	public void readData(HttpServletRequest request, ModelMap params,
			String charset) {
		// 构建带上传进度监视的文件工厂
		MonitoredDiskFileItemFactory factory = new MonitoredDiskFileItemFactory(
				new UploadListener(request));
		// 初始化上传参数
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(this.fileSizeMax);
		upload.setSizeMax(this.sizeMax);
		upload.setHeaderEncoding(charset);

		try {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {
				if (item.isFormField()) {
					try {
						params
								.put(item.getFieldName(), item
										.getString(charset));
					} catch (UnsupportedEncodingException e) {
					}
				}
			}

			params.put(ModelMap.FILE_ITEM_ARGS_KEY, items);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
}
