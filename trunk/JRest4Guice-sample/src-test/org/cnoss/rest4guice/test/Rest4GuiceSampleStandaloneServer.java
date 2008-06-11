package org.cnoss.rest4guice.test;

import java.io.IOException;

import org.jrest4guice.rest.JRest4GuiceHelper;
import org.jrest4guice.rest.JRestGrizzlyResourceAdapter;

import com.sun.grizzly.http.SelectorThread;
import com.sun.grizzly.standalone.StaticStreamAlgorithm;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 * 
 */
public class Rest4GuiceSampleStandaloneServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		final long start = System.currentTimeMillis();
		final SelectorThread selectorThread = new SelectorThread() {
			public void listen() throws IOException, InstantiationException {
				super.listen();
				System.out.println("Server started in "
						+ (System.currentTimeMillis() - start)
						+ " milliseconds.");

				JRest4GuiceHelper.useJRest("org.jrest4guice.sample.resources")
						.useJPA().useSecurity().init();
				System.out.println("完成初始化Guice上下文");
			}
		};
		selectorThread.setAlgorithmClassName(StaticStreamAlgorithm.class
				.getName());
		selectorThread.setPort(9999);
		String folder = "E:\\Cnoss-Google\\JRest4Guice\\JRest4Guice-sample-web\\WebContent";
		SelectorThread.setWebAppRootPath(folder);
		JRestGrizzlyResourceAdapter adapter = new JRestGrizzlyResourceAdapter(
				folder);
		adapter.setRootFolder(folder);
		selectorThread.setAdapter(adapter);
		selectorThread.setDisplayConfiguration(false);
		selectorThread.listen();
	}
}
