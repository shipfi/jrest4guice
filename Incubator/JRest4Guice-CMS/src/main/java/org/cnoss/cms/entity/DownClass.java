/*
 * @(#)DownClass.java   08/06/13
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

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 下载分类
 *
 */
@Entity @Table(name = "T_CLASS")
public class DownClass extends AuditableEntity {

    /**
     *
     */
    private static final long serialVersionUID = 2817818360831263272L;
    @Column(name = "BRANCH_ID", nullable = false)
    private long              branchId;
    @Column(name = "CHILD_NUM")
    private Long              childNum;
    @Column(name = "COLUMN_ID")
    private Long              columnId;
    @Column(name = "DEPTH", nullable = false)
    private long              depth;
    @Column(name = "DESCRIPTION")
    private String            description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classId")
    private List<Download>    downloadList;
    @Column(name = "FOLDERS", nullable = false)
    private String            folders;
    @Column(name = "NAME", nullable = false)
    private String            name;
    @Column(name = "ORDERING", nullable = false)
    private long              ordering;
    @Column(name = "PARENT_ENUM")
    private String            parentEnum;
    @Column(name = "PARENT_ID", nullable = false)
    private long              parentId;

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    public Long getChildNum() {
        return childNum;
    }

    public void setChildNum(Long childNum) {
        this.childNum = childNum;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public long getDepth() {
        return depth;
    }

    public void setDepth(long depth) {
        this.depth = depth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Download> getDownloadList() {
        return downloadList;
    }

    public void setDownloadList(List<Download> downloadList) {
        this.downloadList = downloadList;
    }

    public String getFolders() {
        return folders;
    }

    public void setFolders(String folders) {
        this.folders = folders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOrdering() {
        return ordering;
    }

    public void setOrdering(long ordering) {
        this.ordering = ordering;
    }

    public String getParentEnum() {
        return parentEnum;
    }

    public void setParentEnum(String parentEnum) {
        this.parentEnum = parentEnum;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
}
