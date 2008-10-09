package org.jrest4guice.search.hs.test;

import java.util.Date;

import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.persistence.PersistenceGuiceContext;
import org.jrest4guice.search.hs.entity.Song;
import org.jrest4guice.search.hs.service.SongService;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 测试并发写操作
 */
public class SongServiceCreateTest1 {

	private static SongService service;

	public static void main(String[] args) {
		// 初始化JRest4Guice
		PersistenceGuiceContext.getInstance().useHibernate().useHibernateSearch().init();
		// 获取服务
		service = GuiceContext.getInstance().getBean(SongService.class);

		testCreate(0, 10000);
	}

	private static void testCreate(final int start, final int times) {
		for (int i = start; i < times; i++) {
			Song song = new Song();
			song.setArtist("群星_" + i);
			song.setName("北京欢迎你");
			song.setLyric("北京欢迎你，welcome to beijing,第29届奥林匹克运动会");
			song.setTime(new Date());
			song.setArtistCityId(i % 8);
			service.addSong(song);
			System.out.println(song.getId()+"      "+i);
		}
	}
}
