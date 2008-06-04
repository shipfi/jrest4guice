package org.jrest4guice.commons.fileupload;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public interface OutputStreamListener {
	public void start();

	public void bytesRead(int bytesRead);

	public void error(String message);

	public void done();
}
