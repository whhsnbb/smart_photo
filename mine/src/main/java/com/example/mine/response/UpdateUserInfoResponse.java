package com.example.mine.response;

public class UpdateUserInfoResponse {
    private int state;
    private String msg;
    private boolean data;

    public void setData(boolean data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

    public boolean getData(){
        return data;
    }
}
