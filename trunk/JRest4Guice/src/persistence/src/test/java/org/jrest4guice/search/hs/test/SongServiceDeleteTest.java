package org.jrest4guice.search.hs.test;

import java.util.List;

import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.persistence.PersistenceGuiceContext;
import org.jrest4guice.search.hs.entity.Song;
import org.jrest4guice.search.hs.service.SongService;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class SongServiceDeleteTest {

	private static SongService service;

	@BeforeClass
	public static void setUp() throws Exception {
		// 初始化JRest4Guice
		PersistenceGuiceContext.getInstance().useHibernate().useHibernateSearch().init();
		// 获取服务
		service = GuiceContext.getInstance().getBean(SongService.class);
	}

	@Test
	public void testSearch() {
		List<Song> songs = service.search("北京欢迎你", null);
		System.out.println("count-1:" + songs.size());

		service.deleteSongs("北京欢迎你", "2");

		songs = service.search("北京欢迎你", null);
		System.out.println("count-2:" + songs.size());
	}
}
