package org.cnoss.cms.entity;

import org.cnoss.cms.entity.audit.AuditableEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "T_CONTENT_COMMENT")
public class ContentComment extends AuditableEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -7403287834838003728L;
    @Column(name = "USERNAME", nullable = false)
    private String username;
    @Column(name = "EMAIL", nullable = false)
    private String email;
    @Column(name = "WEBSITE")
    private String website;
    @Column(name = "IP", nullable = false)
    private String ip;
    @Column(name = "UP")
    private Long up;
    @Column(name = "DOWN")
    private Long down;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "TEXT", nullable = false)
    private String text;
    @Column(name = "DATE_CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @JoinColumn(name = "CONTENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getDown() {
        return down;
    }

    public void setDown(Long down) {
        this.down = down;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUp() {
        return up;
    }

    public void setUp(Long up) {
        this.up = up;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
