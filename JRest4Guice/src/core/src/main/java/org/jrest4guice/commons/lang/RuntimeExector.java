package org.jrest4guice.commons.lang;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class RuntimeExector {
	public synchronized static void execute(String command) throws Exception {
		Process process = Runtime.getRuntime().exec(command);
		InputStream input = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		reader.close();
		process.destroy();
	}
}
