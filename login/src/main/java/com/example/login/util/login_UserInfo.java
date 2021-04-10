package com.example.login.util;

public class login_UserInfo {
    private String account;
    private String password;

    public login_UserInfo() {
    }

    public login_UserInfo(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
