package com.example.mine.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.common.Bean.UserInfo;
import com.example.mine.api.Mine_ApiConfig;
import com.example.mine.api.MyUserIconCallback;
import com.example.mine.api.MyUserInfoCallback;
import com.example.mine.api.UpdateUserInfoCallback;
import com.example.mine.response.Get_userData;
import com.example.mine.response.UpdateUserInfoResponse;

import java.io.File;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    public static int update_username = 0;

    public static int update_gender = 1;

    public static int update_introduce = 2;

    public static int update_birthDay = 3;

    private static String mToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTg3OTgxOTAsInVzZXJJZCI6NX0.OkGdjNA0hLjWk_5q3jbOqhCQxLPKogx_uy9yglBfmmU1618711790511348928";

    public static void getUserInfo(Context context, MyUserInfoCallback callback){
        SharedPreferences sp = context.getSharedPreferences("sp_smartPhoto",Context.MODE_PRIVATE);
        String token = sp.getString("token",mToken);
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

    public static void updateUserInfo(Context context, String text, int type, UpdateUserInfoCallback callback){
        SharedPreferences sp = context.getSharedPreferences("sp_smartPhoto",Context.MODE_PRIVATE);
        String token = sp.getString("token",mToken);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Mine_ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Get_userData request = retrofit.create(Get_userData.class);

        Call<UpdateUserInfoResponse> call = null;

        if(type == RetrofitUtils.update_username) {
            call = request.updateUsername(token, text);
        }else if(type == RetrofitUtils.update_introduce){
            call = request.updateUserIntroduce(token, text);
        }else if(type == RetrofitUtils.update_gender){
            call = request.updateUserGender(token, text);
        }else{
            call = request.updateUserBirthDay(token,text);
        }

        call.enqueue(new Callback<UpdateUserInfoResponse>() {
            @Override
            public void onResponse(Call<UpdateUserInfoResponse> call, Response<UpdateUserInfoResponse> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(Call<UpdateUserInfoResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                callback.onFailure(t);
            }
        });
    }

    public static void getUserIcon(Context context, String url, MyUserIconCallback callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Mine_ApiConfig.ICON_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Get_userData request = retrofit.create(Get_userData.class);

        Call<ResponseBody> call = request.getUserIcon(url);

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

    public static void updateUserIcon(Context context, File file, UpdateUserInfoCallback callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Mine_ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Get_userData request = retrofit.create(Get_userData.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("portrait", file.getName(), requestFile);

        Call<UpdateUserInfoResponse> call = request.updateUserPortrait(mToken,body);

        call.enqueue(new Callback<UpdateUserInfoResponse>() {
            @Override
            public void onResponse(Call<UpdateUserInfoResponse> call, Response<UpdateUserInfoResponse> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(Call<UpdateUserInfoResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                callback.onFailure(t);
            }
        });
    }
}
