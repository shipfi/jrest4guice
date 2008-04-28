package org.jrest.test;

import java.io.IOException;

import org.jrest.core.guice.GuiceContext;
import org.jrest.core.persist.jpa.JpaGuiceModuleProvider;
import org.jrest.core.transaction.TransactionGuiceModuleProvider;
import org.jrest.dao.DaoModuleProvider;
import org.jrest.rest.JRestGuiceModuleProvider;
import org.jrest.rest.JRestGrizzlyResourceAdapter;

import com.sun.grizzly.http.SelectorThread;
import com.sun.grizzly.standalone.StaticStreamAlgorithm;

public class JRestSampleStandaloneServer {

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
										"org.jrest.sample.resources"))
						.addModuleProvider(new TransactionGuiceModuleProvider())
						.addModuleProvider(new JpaGuiceModuleProvider())
						.addModuleProvider(
								new DaoModuleProvider(
										new String[] { "org.jrest.sample.dao" }))
						.init();
				System.out.println("完成初始化Guice上下文");
			}
		};
		selectorThread.setAlgorithmClassName(StaticStreamAlgorithm.class
				.getName());
		selectorThread.setPort(80);
		String folder = "E:\\Cnoss-Google\\JRest4Guice\\JRest4Guice-sample-web\\WebContent";
		SelectorThread.setWebAppRootPath(folder);
		JRestGrizzlyResourceAdapter adapter = new JRestGrizzlyResourceAdapter(folder);
		adapter.setRootFolder(folder);
		selectorThread.setAdapter(adapter);
		selectorThread.setDisplayConfiguration(false);
		selectorThread.listen();
	}
}
