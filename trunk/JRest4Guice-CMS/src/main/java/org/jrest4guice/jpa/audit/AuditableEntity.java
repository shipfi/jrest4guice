package org.jrest4guice.jpa.audit;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jrest4guice.jpa.IdEntity;
/**
 * AuditableEntity类.
 * 
 * 实现Auditable 接口,并用@EntityListeners定义Event Listener,自动填入最后的修改人和修改时间.
 * 同时继承于IdEntity,拥有默认的ID设置.
 * 
 */
@MappedSuperclass
@EntityListeners(AuditableEntityListener.class)
public abstract class AuditableEntity extends IdEntity implements Auditable {

	private String lastModifiedBy;

	private Date lastModifiedTime;

	private String createdBy;

	private Date createdTime;

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "CREATED_TIME")
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "LAST_MODIFIED_BY")
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_TIME")
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
}

