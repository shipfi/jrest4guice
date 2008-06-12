package org.cms.entity;



import org.cms.entity.Content;
import org.jrest4guice.jpa.audit.AuditableEntity;



/**
 * 文章内容

 *
 */
public class ContentText extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -409760557896281460L;
	private Long contentId;
	private String title;
	private String text;
	
	private int currentpage;
	
	private Content content;
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}

	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}
	
}
