package com.example.mine.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.common.Bean.UserInfo;
import com.example.mine.api.Mine_ApiConfig;
import com.example.mine.api.MyUserIconCallback;
import com.example.mine.api.MyUserInfoCallback;
import com.example.mine.response.Get_userData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    public static void getUserInfo(Context context, MyUserInfoCallback callback){
        SharedPreferences sp = context.getSharedPreferences("sp_smartPhoto",Context.MODE_PRIVATE);
        String token = sp.getString("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTgyMzQ2NTgsInVzZXJJZCI6NX0._T-24UJZj1SJuzEgSTexhFTTeHvBKWwEwXCu07a7IJA1618148258312448309·");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Mine_ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Get_userData request = retrofit.create(Get_userData.class);

        Call<UserInfo> call = request.getUser(token);

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                callback.onFailure(t);
            }
        });
    }

    public static void getUserIcon(Context context, MyUserIconCallback callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Mine_ApiConfig.ICON_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Get_userData request = retrofit.create(Get_userData.class);

        Call<ResponseBody> call = request.getUserIcon("smart_photo/default.png");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                callback.onFailure(t);
            }
        });
    }
}
