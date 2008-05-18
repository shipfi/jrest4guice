package org.jrest.sample.resources;

import org.jrest.core.fileupload.UploadInfo;
import org.jrest.core.fileupload.UploadMonitor;
import org.jrest.rest.annotation.Get;
import org.jrest.rest.annotation.Restful;

import com.google.inject.Inject;

@Restful(uri = "/monitor")
public class FileuploadMonitor {
	@Inject
	private UploadMonitor monitor;
	
	@Get
	public UploadInfo getUploadInfo(){
		return this.monitor.getUploadInfo();
	}
}
