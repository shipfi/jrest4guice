package org.jrest4guice.search.hs.service;

import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.FullTextSession;
import org.jrest4guice.persistence.BaseEntityManager;
import org.jrest4guice.search.hs.entity.Song;
import org.jrest4guice.transaction.annotations.Transactional;
import org.jrest4guice.transaction.annotations.TransactionalType;

import com.google.inject.Inject;

@Transactional
@SuppressWarnings("unchecked")
public class SongService {
	@Inject
	private BaseEntityManager<String, Song> entityManager;

	@Inject
	private FullTextSession fullTextSession;

	public void init() {
		for (int i = 3; i < 7; i++) {
			Song song = new Song();
			song.setArtist("群星");
			song.setName("北京欢迎你");
			song.setLyric("北京欢迎你，welcome to beijing,第29届奥林匹克运动会");
			song.setTime(new Date());
			song.setArtistCityId(i % 8);
			this.entityManager.create(song);
		}
	}
	
	public void deleteAll(){
		List<Song> songs = this.entityManager.listAll();
		for(Song song:songs)
			this.entityManager.delete(song);
//		this.entityManager.executeUpdate("delete from Song");
	}
	
	public String addSong(Song song){
		this.entityManager.create(song);
		return song.getId();
	}

	public boolean deleteSong(String id){
		return this.entityManager.deleteById(id);
	}
	
	public void updateSong(Song song){
		this.entityManager.update(song);
	}
	
	public void deleteSongs(String condition,String artistCityId){
		List<Song> songs = this.search(condition, artistCityId);
		if(songs != null){
			for(Song song:songs)
				this.deleteSong(song.getId());
		}
	}

	@Transactional(type = TransactionalType.READOLNY)
	public List<Song> search(String condition,String artistCityId) {
		try {
			BooleanQuery booleanQuery = new BooleanQuery();
			MultiFieldQueryParser parser = new MultiFieldQueryParser(
					new String[] { "name", "lyric" }, new StandardAnalyzer());
			Query query;
			query = parser.parse(condition);
			booleanQuery.add(query, BooleanClause.Occur.MUST);
			if(artistCityId != null){
				TermQuery tq = new TermQuery(new Term("artistCityId", artistCityId));
				booleanQuery.add(tq, BooleanClause.Occur.MUST);
			}
			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(
					booleanQuery, Song.class);
			return hibQuery.list();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
