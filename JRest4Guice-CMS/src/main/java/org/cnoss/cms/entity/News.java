package org.cnoss.cms.entity;

import java.util.Date;

import org.cnoss.cms.entity.audit.AuditableEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 新闻实体类
 * 
 */
@Entity
@Table(name = "T_NEWS")
public class News extends AuditableEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 6255479910828967446L;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "CONTENT", nullable = false)
    private String content;
    @Column(name = "USERNAME", nullable = false)
    private String username;
    @Column(name = "STATE")
    private Long state;
    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
