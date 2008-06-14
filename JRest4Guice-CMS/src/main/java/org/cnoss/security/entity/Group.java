package org.cnoss.security.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @author Administrator
 */
@Entity
@Table(name = "t_Group")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    //主键
    private Integer id;
    @Column(name = "Group_Name", nullable = false)
    //用户组名称
    private String groupName;
    @Column(name = "Group_Desc")
    ////描述
    private String groupDesc;
    @Column(name = "Type_ID", nullable = false)
    ////类型
    private int typeID;
    @JoinTable(name = "t_group_role", joinColumns = {@JoinColumn(name = "Group_ID", referencedColumnName = "ID")}, inverseJoinColumns = {@JoinColumn(name = "Role_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Role> roles;

    public Group() {
    }

    public Group(Integer id) {
        this.id = id;
    }

    public Group(Integer id, String groupName, int typeID) {
        this.id = id;
        this.groupName = groupName;
        this.typeID = typeID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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
        if (!(object instanceof Group)) {
            return false;
        }
        Group other = (Group) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.cnoss.community.entity.UserGroup[id=" + id + "]";
    }
}
