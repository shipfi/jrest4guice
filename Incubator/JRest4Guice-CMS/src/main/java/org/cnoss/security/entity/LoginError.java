package org.cnoss.security.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * 登录异常记录表
 * @author Administrator
 */
@Entity
@Table(name = "t_login_Error")
public class LoginError implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    //主键
    private String id;
    @Column(name = "User_ID", nullable = false)
    //用户ID
    private String userID;
    @Column(name = "Error_Times", nullable = false)
    //错误次数
    private int errorTimes;
    @Column(name = "Login_Time")
    //登录时间
    private Long loginTime;

    public LoginError() {
    }

    public LoginError(String id) {
        this.id = id;
    }

    public LoginError(String id, String userID, int errorTimes) {
        this.id = id;
        this.userID = userID;
        this.errorTimes = errorTimes;
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

    public int getErrorTimes() {
        return errorTimes;
    }

    public void setErrorTimes(int errorTimes) {
        this.errorTimes = errorTimes;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
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
        if (!(object instanceof LoginError)) {
            return false;
        }
        LoginError other = (LoginError) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.cnoss.community.entity.LoginError[id=" + id + "]";
    }
}
