package org.jrest4guice.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.jrest4guice.jpa.audit.AuditableEntity;
@Entity
@Table(name = "CMS_ATTACHMENT", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
 @NamedQueries( {
		@NamedQuery(name = "Attachment.list[find]", query = "select e from Attachment e order by e.name"),
		@NamedQuery(name = "Attachment.list[count]", query = "select count(*) from Attachment"),
		@NamedQuery(name = "Attachment.byName[load]", query = "select e from Attachment e where e.name=?")})

public class Attachment extends AuditableEntity {
	   /**
	 * 
	 */
	private static final long serialVersionUID = 2445195963911061323L;
	@Column(name = "NAME", nullable = false)
	   private String name;
	   @Column(name = "URL", nullable = false)
	   private String url;
	   @Column(name = "DESCRIPTION")
	   private String description;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
