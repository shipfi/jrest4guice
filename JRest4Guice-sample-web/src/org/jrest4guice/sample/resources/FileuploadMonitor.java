package org.jrest4guice.sample.resources;

import org.jrest4guice.annotation.Get;
import org.jrest4guice.annotation.Restful;
import org.jrest4guice.core.fileupload.UploadInfo;
import org.jrest4guice.core.fileupload.UploadMonitor;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
@Restful(uri = "/monitor")
public class FileuploadMonitor {
	@Inject
	private UploadMonitor monitor;
	
	@Get
	public UploadInfo getUploadInfo(){
		return this.monitor.getUploadInfo();
	}
}
