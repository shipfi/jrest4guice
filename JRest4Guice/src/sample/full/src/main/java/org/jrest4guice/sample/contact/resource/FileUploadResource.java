package org.jrest4guice.sample.contact.resource;

import java.io.File;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.jrest4guice.rest.annotations.FileItems;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.Post;
import org.jrest4guice.rest.annotations.RESTful;

@RESTful
@Path( { "/fileUpload" })
public class FileUploadResource {
	
	@Post
	public String upload(String url,@FileItems List<FileItem> fileItems){
		if(url == null || url.trim().equals(""))
			url = "F:\\Temp\\upload\\";
		if(!url.endsWith(File.separator)){
			url += File.separator;
		}
		for(FileItem file :fileItems){
			String fileName = file.getName();
			if (fileName==null || fileName.trim().equals(""))
				continue;
			int index = fileName.lastIndexOf(File.separator);
			if (index != -1)
				index++;
			else
				index = 0;
			fileName = fileName.substring(index).toLowerCase();

			try {
				file.write(new File(url+fileName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "文件已经成功上传到"+url+"！";
	}
}
