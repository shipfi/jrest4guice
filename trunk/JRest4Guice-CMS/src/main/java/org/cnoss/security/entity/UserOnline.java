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
 * 在线用户表
 * @author Administrator
 */
@Entity
@Table(name = "t_user_online")
public class UserOnline implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    // //主键
    private String id;
    @Column(name = "User_ID", nullable = false)
    ////用户ID
    private String userID;
    @Column(name = "User_Name", nullable = false)
    //用户名
    private String userName;
    @Column(name = "Nick_Name", nullable = false)
    // //用户昵称
    private String nickName;
    @Column(name = "Online_Time")
    //在线时间
    private Long onlineTime;
    ////所在位置
    @Column(name = "At_Place")
    private String atPlace;
    @Column(name = "UserGroupID")
    ////用户所属用户组
    private Integer userGroupID;
    @Column(name = "Validate_Code")
    ////校验码，防止用户重复登录
    private String validateCode;
    @Column(name = "Hidden_User")
    ////隐身用户标志
    private Boolean hiddenUser;

    public UserOnline() {
    }

    public UserOnline(String id) {
        this.id = id;
    }

    public UserOnline(String id, String userID, String userName, String nickName) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.nickName = nickName;
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

    public Long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getAtPlace() {
        return atPlace;
    }

    public void setAtPlace(String atPlace) {
        this.atPlace = atPlace;
    }

    public Integer getUserGroupID() {
        return userGroupID;
    }

    public void setUserGroupID(Integer userGroupID) {
        this.userGroupID = userGroupID;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public Boolean getHiddenUser() {
        return hiddenUser;
    }

    public void setHiddenUser(Boolean hiddenUser) {
        this.hiddenUser = hiddenUser;
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
        if (!(object instanceof UserOnline)) {
            return false;
        }
        UserOnline other = (UserOnline) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.cnoss.community.entity.UserOnline[id=" + id + "]";
    }
}
