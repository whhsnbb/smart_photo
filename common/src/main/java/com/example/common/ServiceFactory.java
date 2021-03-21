package com.example.common;

import com.example.common.EmptyService.EmptyLoginService;
import com.example.common.Service.LoginService;

public class ServiceFactory {

    private LoginService loginService;

    public ServiceFactory() {
    }

    public static ServiceFactory getInstance(){
        return Inner.serviceFactory;
    }

    private static class Inner{
        private static ServiceFactory serviceFactory = new ServiceFactory();
    }

    public LoginService getLoginService() {
        if (loginService == null){
            return new EmptyLoginService();
        }else{
            return loginService;
        }
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }



}
