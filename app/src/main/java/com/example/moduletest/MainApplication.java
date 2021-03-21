package com.example.moduletest;

import android.app.Application;
import android.os.Build;

import com.alibaba.android.arouter.BuildConfig;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.common.BaseApplication;

public class MainApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

       /* if (isdebug()){
            ARouter.openLog();
            ARouter.openDebug();
        }*/
        ARouter.openDebug();

        ARouter.init(this);
    }

    private boolean isdebug(){
        return BuildConfig.DEBUG;
    }

    @Override
    public void init(Application application) {

    }

    @Override
    public void initover(Application application) {

    }
}
