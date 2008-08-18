package org.jrest4guice.commons.lang;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class RuntimeExector {
	static Log log = LogFactory.getLog(RuntimeExector.class);

	public synchronized static void execute(String command) throws Exception {
		Process process = Runtime.getRuntime().exec(command);
		InputStream input = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line;
		while ((line = reader.readLine()) != null) {
			log.debug(line);
		}
		reader.close();
	}
}
