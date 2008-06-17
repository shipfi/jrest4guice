/*
 * @(#)Download.java   08/06/13
 * 
 * Copyright (c) 2008 conss 开发团队 
 *
 * @author 胡晓豪
 *
 *
 */



package org.cnoss.cms.entity;

//~--- non-JDK imports --------------------------------------------------------

import org.cnoss.cms.entity.DownClass;
import org.cnoss.cms.entity.audit.AuditableEntity;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Collection;
import java.util.Date;
import java.util.Date;
import java.util.Set;

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

@Entity @Table(name = "T_DOWNLOAD")
public class Download extends AuditableEntity {

    /**
     *
     */
    private static final long serialVersionUID = -8722574204403598710L;
    @Column(name = "DATE_CREATED", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;
    @Column(name = "DEMO_URLS")
    private String            demoUrls;
    @Column(name = "DESCRIPTION")
    private String            description;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID")
    @ManyToOne

    // 所属下载类别
    private DownClass                   downClass;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "downloadId")
    private Collection<DownloadComment> downloadComments;
    @Column(name = "FILENAME", nullable = false)
    private String                      filename;
    @Column(name = "FILESIZE", nullable = false)
    private String                      filesize;
    @Column(name = "FILETYPE", nullable = false)
    private String                      filetype;
    @Column(name = "FOLDER", nullable = false)
    private String                      folder;
    @Column(name = "HITS", nullable = false)
    private long                        hits;
    @Column(name = "IS_DEMO", nullable = false)
    private long                        isDemo;
    @Column(name = "IS_VOUCH", nullable = false)
    private long                        isVouch;
    @Column(name = "LANGUAGE")
    private Long                        language;
    @Column(name = "OS")
    private String                      os;
    @Column(name = "STAR")
    private Long                        star;
    @Column(name = "STATE", nullable = false)
    private long                        state;
    @Column(name = "TAGS")
    private String                      tags;
    @Column(name = "TIMES", nullable = false)
    private long                        times;
    @Column(name = "TITLE", nullable = false)
    private String                      title;
    @Column(name = "UPLOADER", nullable = false)
    private String                      uploader;
    @Column(name = "UUID", nullable = false)
    private String                      uuid;

    public DownClass getDownClass() {
        return downClass;
    }

    public void setDownClass(DownClass downClass) {
        this.downClass = downClass;
    }

    // 下载评论 SET

    public void addDownloadComment(DownloadComment downloadComment) {
        downloadComment.setDownload(this);
        downloadComments.add(downloadComment);
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDemoUrls() {
        return demoUrls;
    }

    public void setDemoUrls(String demoUrls) {
        this.demoUrls = demoUrls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<DownloadComment> getDownloadComments() {
        return downloadComments;
    }

    public void setDownloadComments(Collection<DownloadComment> downloadComments) {
        this.downloadComments = downloadComments;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public long getIsDemo() {
        return isDemo;
    }

    public void setIsDemo(long isDemo) {
        this.isDemo = isDemo;
    }

    public long getIsVouch() {
        return isVouch;
    }

    public void setIsVouch(long isVouch) {
        this.isVouch = isVouch;
    }

    public Long getLanguage() {
        return language;
    }

    public void setLanguage(Long language) {
        this.language = language;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Long getStar() {
        return star;
    }

    public void setStar(Long star) {
        this.star = star;
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

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
