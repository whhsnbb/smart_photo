package com.example.login.response;

public class LoginResponse {

    /**
     * status :     响应码   0 -- 成功   1 -- 成功
     * msg :    提示信息
     * data :   是否成功登录
     * exist:   该账号是否已经存在于数据库
     * token:   识别账号的token
     */

    private int state;
    private String msg;
    private boolean data;
    private boolean exist;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean getData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

}