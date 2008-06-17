package org.cnoss.security.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * 
权限信息
 * @author Administrator
 */
@Entity
@Table(name = "t_permission")
public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    //主键
    private Long id;
    @Column(name = "Permission_Name", nullable = false)
    //权限名称
    private String permissionName;
    @Column(name = "Permission_Resource")
     //权限资源（URI）
    private String permissionResource;
    @Column(name = "Action")
    ////动作，Action
    private String action;
    ////权限类型
    @Column(name = "Type_ID", nullable = false)
    private int typeID;

    public Permission() {
    }

    public Permission(Long id) {
        this.id = id;
    }

    public Permission(Long id, String permissionName, int typeID) {
        this.id = id;
        this.permissionName = permissionName;
        this.typeID = typeID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionResource() {
        return permissionResource;
    }

    public void setPermissionResource(String permissionResource) {
        this.permissionResource = permissionResource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permission)) {
            return false;
        }
        Permission other = (Permission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.cnoss.community.entity.Permission[id=" + id + "]";
    }

}
