/*
 * @(#)Link.java   08/06/13
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

public class Link extends AuditableEntity {

    /**
     *
     */
    private static final long serialVersionUID = 4757810319979911843L;
    private int               colummId;
    private String            description;
    private int               displayOrder;
    private int               isLogo;
    private String            name;
    private String            url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColummId() {
        return colummId;
    }

    public void setColummId(int colummId) {
        this.colummId = colummId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getIsLogo() {
        return isLogo;
    }

    public void setIsLogo(int isLogo) {
        this.isLogo = isLogo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
