package org.cnoss.cms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

import org.cnoss.cms.entity.audit.AuditableEntity;


@Entity
@Table(name = "T_SPECIAL")
public class Special extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3978455301692037608L;
	 @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @Column(name = "IMAGE")
    private String image;
    @Column(name = "GOOD")
    private Long good;
    @Column(name = "BAD")
    private Long bad;
    @OneToMany(mappedBy = "specialId")
    private List<SpecialArticle> articles;
	
	

	

	// 添加专题相关文章
	public void addArticle(SpecialArticle specialArticle) {
		specialArticle.setSpecial(this);
		articles.add(specialArticle);
	}
	
}
