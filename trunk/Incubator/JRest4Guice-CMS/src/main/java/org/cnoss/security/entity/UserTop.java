
package org.cnoss.security.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用于积分系统（）
 * 用户统计信息表
 * @author Administrator
 */
@Entity
@Table(name = "t_user_top")
public class UserTop implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    //主键
    private String id;
    @Column(name = "User_ID", nullable = false)
    ////用户ID
    private String userID;
    @Column(name = "User_Name", nullable = false)
    ////用户名
    private String userName;
    @Column(name = "Nick_Name", nullable = false)
    //用户昵称
    private String nickName;
    @Column(name = "Value_Type", nullable = false)
    //统计种类
    private short valueType;
    @Column(name = "User_Value")
    //用户值
    private Integer userValue;
    @Column(name = "Value_Inc")
    //值增长
    private Integer valueInc;

    public UserTop() {
    }

    public UserTop(String id) {
        this.id = id;
    }

    public UserTop(String id, String userID, String userName, String nickName, short valueType) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.nickName = nickName;
        this.valueType = valueType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public short getValueType() {
        return valueType;
    }

    public void setValueType(short valueType) {
        this.valueType = valueType;
    }

    public Integer getUserValue() {
        return userValue;
    }

    public void setUserValue(Integer userValue) {
        this.userValue = userValue;
    }

    public Integer getValueInc() {
        return valueInc;
    }

    public void setValueInc(Integer valueInc) {
        this.valueInc = valueInc;
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
        if (!(object instanceof UserTop)) {
            return false;
        }
        UserTop other = (UserTop) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.cnoss.community.entity.UserTop[id=" + id + "]";
    }
}
