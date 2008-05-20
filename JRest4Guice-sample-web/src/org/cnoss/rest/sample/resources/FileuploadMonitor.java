package org.cnoss.rest.sample.resources;

import org.cnoss.core.fileupload.UploadInfo;
import org.cnoss.core.fileupload.UploadMonitor;
import org.cnoss.rest.annotation.Get;
import org.cnoss.rest.annotation.Restful;

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
