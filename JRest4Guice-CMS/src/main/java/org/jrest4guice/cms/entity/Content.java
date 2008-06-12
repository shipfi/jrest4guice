package org.jrest4guice.cms.entity;

import java.util.Date;
import javax.persistence.*;

import org.jrest4guice.jpa.audit.AuditableEntity;
@Entity
@Table(name = "CMS_CONTENT", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
 @NamedQueries( {
		@NamedQuery(name = "Content.list[find]", query = "select e from Content e order by e.name"),
		@NamedQuery(name = "Content.list[count]", query = "select count(*) from Content"),
		@NamedQuery(name = "Content.byName[load]", query = "select e from Content e where e.name=?")})

public class Content extends AuditableEntity {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 6452059573824731578L;
	/**
     * 内容标题
     */
    @Column(name = "TITLE", nullable = false)
    private String title;
    /**
     *关键字
     */
    @Column(name = "KEYWORD")
    private String keyword;
    /**
     * /来源
     */
    @Column(name = "SOURCE")
    private String source;
    //内容数据类型 1=实体，2=链接
    @Column(name = "TYPE", nullable = false)
    private String type;


    private String entryUrl;
    /**
     * 审核标记
     */
    @Column(name = "AUDIT_FLAG", nullable = false)
    private String auditFlag;
    /**
     * 删除标记
     */
    @Column(name = "DELETE_FLAG", nullable = false)
    private String deleteFlag;
    /**
     * 开启标记
     */
    @Column(name = "ENABLE", nullable = false)
    private String enable;
    /**
     *内容正文在VFS中的存放路径
     */
    @Column(name = "CONTENT")
    private String content;
    /**
     * 审核不通过意见 ()
     */
    @Column(name = "OPINION")
    private String opinion;
  
    /**
     * 内容摘要
     */
    @Column(name = "SUMMARY")
    private String summary;

    /**
     *发布时间
     */
    @Column(name = "PUBLISH_DATE")
    @Temporal(TemporalType.DATE)
    private Date publishDate;
    /**
     * 失效时间
     */
    @Column(name = "AVAILABLE_DATE")
    @Temporal(TemporalType.DATE)
    private Date availableDate;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEntryUrl() {
		return entryUrl;
	}
	public void setEntryUrl(String entryUrl) {
		this.entryUrl = entryUrl;
	}
	public String getAuditFlag() {
		return auditFlag;
	}
	public void setAuditFlag(String auditFlag) {
		this.auditFlag = auditFlag;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public Date getAvailableDate() {
		return availableDate;
	}
	public void setAvailableDate(Date availableDate) {
		this.availableDate = availableDate;
	}
    
}
