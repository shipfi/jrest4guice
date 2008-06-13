package org.cnoss.cms.entity;

import org.cnoss.cms.entity.Content;
import org.cnoss.cms.entity.audit.AuditableEntity;

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

/**
 * 文章内容

 *
 */
@Entity
@Table(name = "T_CONTENT_TEXT")
public class ContentText extends AuditableEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -409760557896281460L;
    @Column(name = "TEXT", nullable = false)
    private String text;
    @JoinColumn(name = "CONTENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
