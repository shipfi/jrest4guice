package org.cms.entity;

import org.jrest4guice.jpa.audit.AuditableEntity;

// 网站对象

public class Website extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2769236467341279739L;
	// 名称
	private String name;
	// 域名
	private String domain;
	// 管理员邮箱
	private String masterEmail;
	// keywords
	private String keywords;
	// description
	private String description;
	
	// 版权
	private String copyright;

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getMasterEmail() {
		return masterEmail;
	}

	public void setMasterEmail(String masterEmail) {
		this.masterEmail = masterEmail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
}
