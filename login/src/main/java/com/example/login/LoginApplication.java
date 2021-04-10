package com.example.login;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.example.common.BaseApplication;

public class LoginApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }

    @Override
    public void init(Application application) {

    }

    @Override
    public void initover(Application application) {

    }
}
