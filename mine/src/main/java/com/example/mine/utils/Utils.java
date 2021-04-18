package com.example.mine.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.mine.activity.AccountActivity;
import com.example.mine.api.UpdateUserInfoCallback;
import com.example.mine.response.UpdateUserInfoResponse;
import com.example.mine.view.ItemView;

import retrofit2.Response;

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

    public static void showDialog(ItemView view, AccountActivity accountActivity, AlertDialog.Builder builder, String[] nation, int which){
        SharedPreferences sp = accountActivity.getSharedPreferences("sp_getUserInfo",Context.MODE_PRIVATE);
        builder.setTitle("性别").setSingleChoiceItems(nation, which, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RetrofitUtils.updateUserInfo(accountActivity, nation[which], RetrofitUtils.update_gender, new UpdateUserInfoCallback() {
                    @Override
                    public void onSuccess(Response<UpdateUserInfoResponse> response) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("gender",nation[which]);
                        editor.apply();
                        accountActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.setContent(sp.getString("gender",""));
                                Toast.makeText(accountActivity,"修改成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        accountActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(accountActivity,"修改失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.dismiss();                                                      //点击以后关闭dialog
            }
        }).setCancelable(true).show();//点击空白处可关闭dialog
    }
}
