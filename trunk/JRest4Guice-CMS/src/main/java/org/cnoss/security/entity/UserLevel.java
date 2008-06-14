/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cnoss.security.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * 用户等级
 */
@Entity
@Table(name = "t_user_level")
public class UserLevel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    //ID
    private String id;
    @Column(name = "Level_Name", nullable = false)
    //级别名称
    private String levelName;
    @Column(name = "Level_Value", nullable = false)
    //该级别所需相应值
    private int levelValue;
    @Column(name = "Level_Type")
    //级别类型
    private int levelType;

    public UserLevel() {
    }

    public UserLevel(String id) {
        this.id = id;
    }

    public UserLevel(String id, String levelName, int levelValue) {
        this.id = id;
        this.levelName = levelName;
        this.levelValue = levelValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getLevelValue() {
        return levelValue;
    }

    public void setLevelValue(int levelValue) {
        this.levelValue = levelValue;
    }

    public int getLevelType() {
        return levelType;
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
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
        if (!(object instanceof UserLevel)) {
            return false;
        }
        UserLevel other = (UserLevel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.cnoss.community.entity.UserLevel[id=" + id + "]";
    }
}
