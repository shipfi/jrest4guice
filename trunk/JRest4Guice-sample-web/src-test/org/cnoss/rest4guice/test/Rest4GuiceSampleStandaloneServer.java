package org.cnoss.rest4guice.test;

import java.io.IOException;

import org.jrest4guice.JRestGrizzlyResourceAdapter;
import org.jrest4guice.JRestGuiceModuleProvider;
import org.jrest4guice.core.guice.GuiceContext;
import org.jrest4guice.core.persist.jpa.JpaGuiceModuleProvider;
import org.jrest4guice.core.transaction.TransactionGuiceModuleProvider;

import com.sun.grizzly.http.SelectorThread;
import com.sun.grizzly.standalone.StaticStreamAlgorithm;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
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

				GuiceContext
						.getInstance()
						.addModuleProvider(
								new JRestGuiceModuleProvider(
										"org.jrest4guice.sample.resources"))
						.addModuleProvider(new TransactionGuiceModuleProvider())
						.addModuleProvider(new JpaGuiceModuleProvider()).init();
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
