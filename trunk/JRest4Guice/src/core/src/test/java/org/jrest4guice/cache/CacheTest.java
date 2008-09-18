package org.jrest4guice.cache;

import java.util.Timer;
import java.util.TimerTask;

import org.jrest4guice.guice.GuiceContext;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class CacheTest {

	private static int index = 0;

	public static void main(String[] args) {
		// 初始化JRest4Guice
		GuiceContext.getInstance().useCache().init();
		final CacheProvider cacheProvider = CacheProviderRegister.getInstance()
				.getCacheProvider("memcached");
		cacheProvider.setCacheServers("192.168.16.123:11211");
		cacheProvider.setExpiryTime(1000 * 10);
		cacheProvider.put("name", "www.cnoss.org");
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Object name = cacheProvider.get("name");
				if (name == null)
					timer.cancel();
				else
					System.out.println(name + "   " + (++index));
			}
		}, 0, 1000);
	}
}
