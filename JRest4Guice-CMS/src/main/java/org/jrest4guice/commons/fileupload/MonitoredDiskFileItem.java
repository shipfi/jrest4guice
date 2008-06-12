package org.jrest4guice.commons.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.fileupload.disk.DiskFileItem;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 *
 */
@SuppressWarnings("serial")
public class MonitoredDiskFileItem extends DiskFileItem {
	private MonitoredOutputStream mOuts = null;
	private OutputStreamListener listener;

	public MonitoredDiskFileItem(String fieldName, String contentType,
			boolean isFormField, String fileName, int sizeThreshold,
			File repository, OutputStreamListener listener) {
		super(fieldName, contentType, isFormField, fileName, sizeThreshold,
				repository);
		this.listener = listener;
	}

	public OutputStream getOutputStream() throws IOException {
		if (mOuts == null) {
			mOuts = new MonitoredOutputStream(super.getOutputStream(), listener);
		}
		return mOuts;
	}
}
