
package org.cnoss.security.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 *
好友表
 * @author Administrator
 */
@Entity
@Table(name = "t_friend")
public class Friend implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    //主键
    private String id;
    @Column(name = "USER_ID", nullable = false)
    //用户ID
    private String userID;
    @Column(name = "User_Name", nullable = false)
    //用户名
    private String userName;
    @Column(name = "Friend_ID", nullable = false)
    //好友ID
    private String friendID;
    @Column(name = "Friend_Name", nullable = false)
    //好友名
    private String friendName;
    @Lob
    @Column(name = "Friend_Comment")
    //说明
    private String friendComment;
    @Column(name = "Is_Black")
    ////是否黑名单
    private Boolean isBlack;

    public Friend() {
    }

    public Friend(String id) {
        this.id = id;
    }

    public Friend(String id, String userID, String userName, String friendID, String friendName) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.friendID = friendID;
        this.friendName = friendName;
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

    public String getFriendID() {
        return friendID;
    }

    public void setFriendID(String friendID) {
        this.friendID = friendID;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendComment() {
        return friendComment;
    }

    public void setFriendComment(String friendComment) {
        this.friendComment = friendComment;
    }

    public Boolean getIsBlack() {
        return isBlack;
    }

    public void setIsBlack(Boolean isBlack) {
        this.isBlack = isBlack;
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
        if (!(object instanceof Friend)) {
            return false;
        }
        Friend other = (Friend) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.cnoss.community.entity.Friend[id=" + id + "]";
    }
}
