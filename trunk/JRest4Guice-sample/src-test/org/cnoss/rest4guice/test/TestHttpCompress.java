package org.cnoss.rest4guice.test;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class TestHttpCompress {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		doTest("http://localhost/JRest4Guice-sample/resource/security/cnoss/roles");
//		doTest("http://localhost/JRest4Guice-sample/main.html");
//		doTest("http://localhost/JRest4Guice-sample/main.js");
//		doTest("http://localhost/JRest4Guice-sample/css/default.css");
	}

	private static void doTest(String url) throws IOException, HttpException {
		HttpClient http = new HttpClient();
		GetMethod get = new GetMethod(url);
		try {
			get.addRequestHeader("accept-encoding", "gzip,deflate");
			get
					.addRequestHeader(
							"user-agent",
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; Alexa Toolbar; Maxthon 2.0)");
			int er = http.executeMethod(get);
			if (er == 200) {
				System.out.println(get.getResponseContentLength());
				String html = get.getResponseBodyAsString();
				System.out.println(html);
				
				System.out.println("\n\n文件大小"+html.getBytes().length);
			}
		} finally {
			get.releaseConnection();
		}
	}

}
