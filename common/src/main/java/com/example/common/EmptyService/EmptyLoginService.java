package com.example.common.EmptyService;

import com.example.common.Service.LoginService;

public class EmptyLoginService implements LoginService {

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public String getAccount() {
        return null;
    }
}