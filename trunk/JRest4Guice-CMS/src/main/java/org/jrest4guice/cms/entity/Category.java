package org.jrest4guice.cms.entity;

import javax.persistence.*;

import org.jrest4guice.jpa.audit.AuditableEntity;
/**
 * 栏目
 * @author Administrator
 *
 */
@Entity
@Table(name = "cms_category", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
 @NamedQueries( {
		@NamedQuery(name = "Category.list[find]", query = "select e from Category e order by e.name"),
		@NamedQuery(name = "Category.list[count]", query = "select count(*) from Category"),
		@NamedQuery(name = "Category.byName[load]", query = "select e from Category e where e.name=?")})

public class Category extends AuditableEntity{
	  /**
	 * 
	 */
	private static final long serialVersionUID = -1561486884491744819L;
	/**
     * 栏目名称
     */
    @Column(name = "NAME", nullable = false)
    private String name;
    /**
     * 栏目描述
     */
    @Column(name = "DESCRIPTION")
    private String description;
  

     //审核标记 Y已审核 N未审核
    @Column(name = "AUDIT_FLAG", nullable = false)
    private String auditFlag;

  
    /**
     * 开启标记 Y已启用 N未启用
     */
    @Column(name = "ENABLE", nullable = false)
    private String enable; //
    /**
     * 删除标记   Y已删除 N未删除
     */
    @Column(name = "DELETE_FLAG", nullable = false)
    private String deleteFlag;
  
     /**
     * 栏目类型标识，1=实体栏目，2=链接栏目
      *链接栏目主要为一个链接。主要方便审核及管理
     */
    @Column(name = "TYPE", nullable = false)
    private String type;

    //链接:当为链接栏目时启用，这里设计考虑到性能问题，统一用单表设计
    @Column(name = "URL", nullable = false)
    private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getAuditFlag() {
		return auditFlag;
	}

	public void setAuditFlag(String auditFlag) {
		this.auditFlag = auditFlag;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}
