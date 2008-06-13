package org.cnoss.cms.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.cnoss.cms.entity.audit.AuditableEntity;

@Entity
@Table(name = "T_SPECIAL_ARTICLE")
public class SpecialArticle extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8076617607513340330L;
    @Column(name = "SPECIALID", nullable = false)
    private long specialid;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "URL")
    private String url;
    @Column(name = "DATE_CREATED", nullable = false)
    private String dateCreated;
    @JoinColumn(name = "SPECIAL_ID", referencedColumnName = "ID")
    @ManyToOne
	private Special special;
	public Special getSpecial() {
		return special;
	}
	public void setSpecial(Special special) {
		this.special = special;
	}
	
	

}
