package com.example.login.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Login_Api {
    public static int TYPE_PostYzm = 1;
    public static int TYPE_GetYzm = 2;
    public static int TYPE_Password = 3;
    public static int TYPE_SetNewPassword = 4;
    private static OkHttpClient client;
    private static String requestUrl;
    public static Login_Api api = new Login_Api();
    public static String mPhoneNumber;
    public static String mYzm;
    public static String mPassword;
    public static String newPassword;

    public Login_Api(){

    }

    public static Login_Api config(String url, String text, int type) {
        client = new OkHttpClient.Builder()
                .build();
        requestUrl = Login_ApiConfig.BASE_URL + url;
        if(type == TYPE_GetYzm) {
            mPhoneNumber = text;
        }else if(type == TYPE_SetNewPassword){
            newPassword = text;
        }
        return api;
    }

    public static Login_Api config(String url, String phoneNumber, String text, int type) {
        client = new OkHttpClient.Builder()
                .build();
        requestUrl = Login_ApiConfig.BASE_URL + url;
        mPhoneNumber = phoneNumber;
        if(type == Login_Api.TYPE_Password){
            mPassword = text;
        }else if(type == Login_Api.TYPE_PostYzm){
            mYzm = text;
        }
        return api;
    }

    public static Login_Api config(String url, String phoneNumber, String newPassword, String yzm) {
        client = new OkHttpClient.Builder()
                .build();
        requestUrl = Login_ApiConfig.BASE_URL + url;
        mPhoneNumber = phoneNumber;
        mPassword = newPassword;
        mYzm = yzm;
        return api;
    }

    public void postLoginRequest(final Context context, final int type, final MyLoginCallback callback){
        RequestBody requestBody = null;
        if(type == TYPE_GetYzm) {
            requestBody = new FormBody.Builder()
                    .add("phone", mPhoneNumber)
                    .build();
        }else if(type == TYPE_PostYzm){
            requestBody = new FormBody.Builder()
                    .add("phone", mPhoneNumber)
                    .add("code",mYzm)
                    .build();
        }else if(type == TYPE_Password){
            requestBody = new FormBody.Builder()
                    .add("phone", mPhoneNumber)
                    .add("password",mPassword)
                    .build();
        }
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .build();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String token = response.header("token","");
                SharedPreferences sp = context.getSharedPreferences("sp_smartPhoto",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("token",token);
                editor.apply();
                final String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

    public void postNewPassword(final Context context, final MyLoginCallback callback){
        SharedPreferences sp = context.getSharedPreferences("sp_smartPhoto", MODE_PRIVATE);
        String token = sp.getString("token", "");
        RequestBody requestBody = new FormBody.Builder()
                .add("password",newPassword)
                .build();
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("token",token)
                .post(requestBody)
                .build();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

    public void postUpdatePassword(final MyLoginCallback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("phone",mPhoneNumber)
                .add("code",mYzm)
                .add("password",newPassword)
                .build();
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .build();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

}
