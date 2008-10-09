package org.jrest4guice.search.hs.test;

import java.util.List;

import junit.framework.Assert;

import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.guice.PersistenceGuiceContext;
import org.jrest4guice.search.hs.entity.Song;
import org.jrest4guice.search.hs.service.SongService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class SongServiceSearchTest {

	private static SongService service;

	@BeforeClass
	public static void setUp() throws Exception {
		// 初始化JRest4Guice
		PersistenceGuiceContext.getInstance().useHibernate().useHibernateSearch().init();
		// 获取服务
		service = GuiceContext.getInstance().getBean(SongService.class);
		service.init();
	}

	@Test
	public void testSearch() {
		System.out.println("查询城市ID为5的城市");
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		List<Song> songs = service.search("北京欢迎你", "5");
		Assert.assertTrue(songs.size()==1);
		for (Song song : songs) {
			System.out.println(song.getArtist() + ":" + song.getName() + ":"
					+ song.getLyric());
		}

		System.out.println("\n查询所有城市");
		System.out.println("＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		songs = service.search("北京欢迎你", null);
		Assert.assertTrue("歌曲的数量不为4",songs.size()==4);
		for (Song song : songs) {
			System.out.println(song.getArtist() + ":" + song.getName() + ":"
					+ song.getLyric());
		}
	}
	
	@AfterClass
	public static void clear(){
		service.deleteAll();
	}
}
