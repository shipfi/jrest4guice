package org.cnoss.cms.entity;

import java.util.Date;
import java.util.Set;

import org.cnoss.cms.entity.Category;
import org.cnoss.cms.entity.ContentComment;
import org.cnoss.cms.entity.audit.AuditableEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 文章基本信息

 *
 */
@Entity
@Table(name = "T_CONTENT")
public class Content extends AuditableEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -7850395481514381002L;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "TITLE_ALIAS", nullable = false)
    private String titleAlias;
    @Column(name = "TEXT_ALIAS", nullable = false)
    private String textAlias;
    @Column(name = "TAGS", nullable = false)
    private String tags;
    @Column(name = "STATE", nullable = false)
    private long state;
    @Column(name = "ORDERING")
    private Long ordering;
    @Column(name = "HITS")
    private Long hits;
    @Column(name = "AUTHOR", nullable = false)
    private String author;
    @Column(name = "IS_SELF")
    private Long isSelf;
    @Column(name = "SOURCE")
    private String source;
    @Column(name = "IS_VOUCH")
    private Long isVouch;
    @Column(name = "DATE_CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "UUID", nullable = false)
    private String uuid;
    @Column(name = "EDITOR", nullable = false)
    private String editor;
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")
    @ManyToOne
    private Category category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contentId")
    private List<ContentComment> comments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contentId")
    private List<ContentText> texts;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // 添加评论
    public void addComment(ContentComment contentComment) {
        contentComment.setContent(this);
        comments.add(contentComment);
    }


    // 添加文章内容
    public void addText(ContentText contentText) {
        contentText.setContent(this);
        texts.add(contentText);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<ContentComment> getComments() {
        return comments;
    }

    public void setComments(List<ContentComment> comments) {
        this.comments = comments;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public Long getIsSelf() {
        return isSelf;
    }

    public void setIsSelf(Long isSelf) {
        this.isSelf = isSelf;
    }

    public Long getIsVouch() {
        return isVouch;
    }

    public void setIsVouch(Long isVouch) {
        this.isVouch = isVouch;
    }

    public Long getOrdering() {
        return ordering;
    }

    public void setOrdering(Long ordering) {
        this.ordering = ordering;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTextAlias() {
        return textAlias;
    }

    public void setTextAlias(String textAlias) {
        this.textAlias = textAlias;
    }

    public List<ContentText> getTexts() {
        return texts;
    }

    public void setTexts(List<ContentText> texts) {
        this.texts = texts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleAlias() {
        return titleAlias;
    }

    public void setTitleAlias(String titleAlias) {
        this.titleAlias = titleAlias;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
