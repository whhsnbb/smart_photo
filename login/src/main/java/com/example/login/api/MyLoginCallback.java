package com.example.login.api;

public interface MyLoginCallback {

    void onSuccess(String result);

    void onFailure(Exception e);
}
