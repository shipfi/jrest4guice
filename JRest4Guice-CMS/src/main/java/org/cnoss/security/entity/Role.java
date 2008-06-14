

package org.cnoss.security.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * 角色
 */
@Entity
@Table(name = "t_role")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
     //主键
    private Integer id;
    @Column(name = "Role_Name", nullable = false)
     //角色名称
    private String roleName;
    @Column(name = "Type_ID", nullable = false)
    //角色类型
    private int typeID;
    @Lob
    @Column(name = "Permissions")
    //权限列表，用“,”分开
    private String permissions;
     @ManyToMany(mappedBy = "roles")
    private List<Group> groups;
  
    public Role() {
    }

    public Role(Integer id) {
        this.id = id;
    }

    public Role(Integer id, String roleName, int typeID) {
        this.id = id;
        this.roleName = roleName;
        this.typeID = typeID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
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
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.cnoss.community.entity.Role[id=" + id + "]";
    }

}
