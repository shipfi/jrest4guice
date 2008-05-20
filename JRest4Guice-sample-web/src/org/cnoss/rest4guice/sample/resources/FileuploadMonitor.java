package org.cnoss.rest4guice.sample.resources;

import org.cnoss.core.fileupload.UploadInfo;
import org.cnoss.core.fileupload.UploadMonitor;
import org.cnoss.rest4guice.annotation.Get;
import org.cnoss.rest4guice.annotation.Restful;

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
