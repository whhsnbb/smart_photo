package com.example.login.service;

import com.example.common.Service.LoginService;
import com.example.login.util.AccountUtils;

public class AccountService implements LoginService {
    @Override
    public boolean isLogin() {
        return AccountUtils.userInfo != null;
    }

    @Override
    public String getAccount() {
        return AccountUtils.userInfo.getAccount();
    }
}
