package com.example.mine.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

public final class Utils {

    private Utils(){
    }

    public static void navigateTo(Context mContext, Class cls) {
        Intent in = new Intent(mContext, cls);
        mContext.startActivity(in);
    }

    public static void  setLucid(Activity mContext){
        // 状态栏透明|字体为黑
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = mContext.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            mContext.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            mContext.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
