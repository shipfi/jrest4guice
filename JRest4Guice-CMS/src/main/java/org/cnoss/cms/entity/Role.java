/*
 * @(#)Role.java   08/06/13
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

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity @Table(name = "T_ROLE")
public class Role extends AuditableEntity {

    /**
     *
     */
    private static final long serialVersionUID = 904477398643579974L;
    @Column(name = "DESCRIPTION")
    private String            description;
    @Column(name = "NAME", nullable = false)
    private String            name;
    private Map               permissionMap;
    @Column(name = "PERMISSIONS", nullable = false)
    private String            permissions;

    public Map getPermissionMap() {
        return permissionMap;
    }

    public void setPermissionMap(Map permissionMap) {
        this.permissionMap = permissionMap;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
