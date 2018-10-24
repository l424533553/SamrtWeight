package com.luofx.entity;

/**
 * 说明：用户登录返回响应的对象
 * 作者：User_luo on 2018/4/25 09:29
 * 邮箱：424533553@qq.com
 */
public class LoginBean {

    private String loginName, loginPassword, username;
    int state;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


}
