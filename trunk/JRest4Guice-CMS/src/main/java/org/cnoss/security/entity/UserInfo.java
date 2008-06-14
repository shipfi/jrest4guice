/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cnoss.security.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 *  * 用户信息
 * @author Administrator
 */
@Entity
@Table(name = "t_user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    //主键
    private String id;
    @Column(name = "User_Name", nullable = false)
    //用户名
    private String userName;
    @Column(name = "Nick_Name", nullable = false)
    //昵称
    private String nickName;
    @Column(name = "Passwd", nullable = false)
    //密码
    private String passwd;
    @Column(name = "Re_Passwd", nullable = false)
    ////加密密码
    private String rePasswd;
    @Column(name = "Email", nullable = false)
    ////Email
    private String email;
    @Column(name = "Question", nullable = false)
    // //问题
    private String question;
    @Column(name = "Answer", nullable = false)
    //答案
    private String answer;
    @Column(name = "Reg_Time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    //注册时间
    private Date regTime;
    @Column(name = "Login_Time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    //登录时间
    private Date loginTime;
    @Column(name = "Login_IP", nullable = false)
    //登录IP
    private String loginIP;
    @Column(name = "Login_Times", nullable = false)
    //登录次数
    private int loginTimes;
    @Column(name = "Last_Login_Time")
    @Temporal(TemporalType.TIMESTAMP)
    //最后登录时间
    private Date lastLoginTime;
    //最后登录IP
    @Column(name = "Last_Login_IP")
    private String lastLoginIP;
    @Column(name = "Stay_Time")
    //在线时间
    private Long stayTime;
    ////有头像标志
    @Column(name = "Have_Pic")
    private Boolean havePic;
    @Column(name = "Pic_File_Name")
    //头像文件名
    private String picFileName;
    //来自
    @Column(name = "User_From")
    private String userFrom;
    //时区
    @Column(name = "Time_Zone")
    private String timeZone;
    // 出生年
    @Column(name = "Birth_Year")
    private Integer birthYear;
    ////出生月
    @Column(name = "Birth_Month")
    private Short birthMonth;
    @Column(name = "Birth_Day")
    //出生日
    private Short birthDay;
    @Column(name = "Accept_Friend")
    //接受好友请求
    private Boolean acceptFriend;
    //是否通过验证
    @Column(name = "Validated")
    private Boolean validated;
     //用户组ID
    @Column(name = "Group_ID")
    private Integer groupID;
    @Column(name = "Validate_Code")
    ////校验码，防止用户重复登录
    private String validateCode;
    @Column(name = "Hidden_Login")
    //隐身登录标志
    private Boolean hiddenLogin;

    public UserInfo() {
    }

    public UserInfo(String id) {
        this.id = id;
    }

    public UserInfo(String id, String userName, String nickName, String passwd, String rePasswd, String email, String question, String answer, Date regTime, Date loginTime, String loginIP, int loginTimes) {
        this.id = id;
        this.userName = userName;
        this.nickName = nickName;
        this.passwd = passwd;
        this.rePasswd = rePasswd;
        this.email = email;
        this.question = question;
        this.answer = answer;
        this.regTime = regTime;
        this.loginTime = loginTime;
        this.loginIP = loginIP;
        this.loginTimes = loginTimes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getRePasswd() {
        return rePasswd;
    }

    public void setRePasswd(String rePasswd) {
        this.rePasswd = rePasswd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getRegTime() {
        return regTime;
    }

    public Boolean getAcceptFriend() {
        return acceptFriend;
    }

    public void setAcceptFriend(Boolean acceptFriend) {
        this.acceptFriend = acceptFriend;
    }

    public Short getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Short birthDay) {
        this.birthDay = birthDay;
    }

    public Short getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(Short birthMonth) {
        this.birthMonth = birthMonth;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public Boolean getHavePic() {
        return havePic;
    }

    public void setHavePic(Boolean havePic) {
        this.havePic = havePic;
    }

    public Boolean getHiddenLogin() {
        return hiddenLogin;
    }

    public void setHiddenLogin(Boolean hiddenLogin) {
        this.hiddenLogin = hiddenLogin;
    }

    public String getPicFileName() {
        return picFileName;
    }

    public void setPicFileName(String picFileName) {
        this.picFileName = picFileName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public int getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(int loginTimes) {
        this.loginTimes = loginTimes;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public Long getStayTime() {
        return stayTime;
    }

    public void setStayTime(Long stayTime) {
        this.stayTime = stayTime;
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
        if (!(object instanceof UserInfo)) {
            return false;
        }
        UserInfo other = (UserInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.cnoss.community.entity.UserInfo[id=" + id + "]";
    }
}
