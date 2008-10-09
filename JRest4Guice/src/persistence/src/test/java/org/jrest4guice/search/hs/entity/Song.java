package org.jrest4guice.search.hs.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.ShardKey;
import org.hibernate.search.annotations.Store;
import org.jrest4guice.persistence.EntityAble;

@Entity
@Table(name="song")
@Indexed(index="song")
@NamedQueries({
	@NamedQuery(name="Song.list[find]",query="from Song s order by s.time desc"),
	@NamedQuery(name="Song.list[count]",query="select count(*) from Song"),
	@NamedQuery(name="Song.byCityId[load]",query="from Song s where s.artistCityId =?")
})
public class Song implements EntityAble<String>,Serializable{
	
	private static final long serialVersionUID = -1475639504643543299L;

	@Id
	@DocumentId
	@GeneratedValue(generator="system_uuid")
	@GenericGenerator(name="system_uuid",strategy="uuid")
	private String id;
	
	@Column(name="artist",length=36)
	private String artist;
	
	@Column(name="name",length=36,nullable=false)
	@Field(index=Index.TOKENIZED,store=Store.NO)
	private String name;
	
	@Lob
	@Column(name="lyric",length=2000)
	@Field(index=Index.TOKENIZED,store=Store.NO)
	private String lyric;
	
	@Column(name="time",nullable=false)
	@Field(index=Index.UN_TOKENIZED,store=Store.NO)
	private Date time;

	@Column(name="artistCityId",nullable=false)
	@Field(index=Index.UN_TOKENIZED,store=Store.YES)
	@ShardKey
	private Integer artistCityId;
	
	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLyric() {
		return lyric;
	}

	public void setLyric(String lyric) {
		this.lyric = lyric;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id=id;
	}

	public Integer getArtistCityId() {
		return artistCityId;
	}

	public void setArtistCityId(Integer artistCityId) {
		this.artistCityId = artistCityId;
	}
	
}
