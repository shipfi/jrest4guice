/*
 * @(#)Special.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.entity;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.audit.AuditableEntity;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity @Table(name = "T_SPECIAL")
public class Special extends AuditableEntity {

    /**
     *
     */
    private static final long    serialVersionUID = -3978455301692037608L;
    @OneToMany(mappedBy = "specialId")
    private List<SpecialArticle> articles;
    @Column(name = "BAD")
    private Long                 bad;
    @Column(name = "DESCRIPTION", nullable = false)
    private String               description;
    @Column(name = "GOOD")
    private Long                 good;
    @Column(name = "IMAGE")
    private String               image;
    @Column(name = "NAME", nullable = false)
    private String               name;

    // 添加专题相关文章
    public void addArticle(SpecialArticle specialArticle) {
        specialArticle.setSpecial(this);
        articles.add(specialArticle);
    }
}
