package org.cms.entity;


import java.util.Set;

import org.jrest4guice.jpa.audit.AuditableEntity;



public class Special extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3978455301692037608L;
	private String name;
	private String description;
	private String image;
	private int good;
	private int bad;
	
	public int getBad() {
		return bad;
	}
	public void setBad(int bad) {
		this.bad = bad;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getGood() {
		return good;
	}
	public void setGood(int good) {
		this.good = good;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	// special articles
	private Set articles;
	public Set getArticles() {
		return articles;
	}
	public void setArticles(Set articles) {
		this.articles = articles;
	}

	// 添加专题相关文章
	public void addArticle(SpecialArticle specialArticle) {
		specialArticle.setSpecial(this);
		articles.add(specialArticle);
	}
	
}
