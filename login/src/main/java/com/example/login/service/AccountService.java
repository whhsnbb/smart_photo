package com.example.login.service;

import com.example.common.Service.LoginService;
import com.example.login.util.login_AccountUtils;

public class AccountService implements LoginService {
    @Override
    public boolean isLogin() {
        return login_AccountUtils.userInfo != null;
    }

    @Override
    public String getAccount() {
        return login_AccountUtils.userInfo.getAccount();
    }
}
