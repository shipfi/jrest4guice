package org.jrest4guice.sample.contact.test;
import java.io.File;

import org.jrest4guice.client.JRestClient;

public class TestUpload {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JRestClient client = new JRestClient();
		try {
			Object result = client.uploadFiles(
					"http://sample.snifast.com/full/fileUpload", null,
					new File[] { new File("F:\\KuGou\\陈倩倩 - 婴儿.mp3"),
							new File("F:\\KuGou\\wendy.jpg") });
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
