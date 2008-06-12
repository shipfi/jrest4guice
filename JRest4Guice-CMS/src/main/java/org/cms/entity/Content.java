package org.cms.entity;

import java.util.Date;
import java.util.Set;

import org.cms.entity.Category;
import org.cms.entity.ContentComment;
import org.jrest4guice.jpa.audit.AuditableEntity;



/**
 * 文章基本信息

 *
 */
public class Content extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7850395481514381002L;
	private String title;
	private String titleAlias;
	private Long categoryId;
	private String text;
	private String textAlias;
	private String tags;
	private int state;
	private int ordering;
	private int hits;
	private String author;
	private int isVouch;
	private Date dateCreated;
	private int isSelf;
	private String source;
	private String uuid;
	private String editor;
			
	// text size
	private int textSize;
	public int getTextSize() {
		return textSize;
	}
	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
	
	// content's category
	private Category category ;
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	// set comments to set
	private Set comments;
	public Set getComments() {
		return comments;
	}
	public void setComments(Set comments) {
		this.comments = comments;
	}
	// 添加评论
	public void addComment(ContentComment contentComment) {
		contentComment.setContent(this);
		comments.add(contentComment);
	}
	
	private Set texts;
	public Set getTexts() {
		return texts;
	}
	public void setTexts(Set texts) {
		this.texts = texts;
	}
	// 添加文章内容
	public void addText(ContentText contentText){
		contentText.setContent(this);
		texts.add(contentText);
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public int getIsVouch() {
		return isVouch;
	}
	public void setIsVouch(int isVouch) {
		this.isVouch = isVouch;
	}
	public int getOrdering() {
		return ordering;
	}
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTextAlias() {
		return textAlias;
	}
	public void setTextAlias(String textAlias) {
		this.textAlias = textAlias;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleAlias() {
		return titleAlias;
	}
	public void setTitleAlias(String titleAlias) {
		this.titleAlias = titleAlias;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getIsSelf() {
		return isSelf;
	}
	public void setIsSelf(int isSelf) {
		this.isSelf = isSelf;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}	
}
