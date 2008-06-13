package org.cnoss.cms.entity;


import org.cnoss.cms.entity.audit.AuditableEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "T_COLUMN")
public class TColumn extends  AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1234905539044660999L;
	   @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "PATH", nullable = false)
    private String path;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
