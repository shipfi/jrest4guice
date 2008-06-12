package org.jrest4guice.cms.entity;
import org.jrest4guice.jpa.audit.AuditableEntity;
import javax.persistence.*;
@Entity
@Table(name = "CMS_LINK", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
 @NamedQueries( {
		@NamedQuery(name = "Link.list[find]", query = "select e from Link e order by e.name"),
		@NamedQuery(name = "Link.list[count]", query = "select count(*) from Link"),
		@NamedQuery(name = "Link.byName[load]", query = "select e from Link e where e.name=?")})
public class Link extends AuditableEntity{
	  /**
	 * 
	 */
	private static final long serialVersionUID = -2181592999417694968L;
	/**
	 * 
	 */
	
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
