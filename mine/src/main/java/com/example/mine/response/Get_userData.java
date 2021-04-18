package com.example.mine.response;

import com.example.common.Bean.UserInfo;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface Get_userData {

    @POST("user/inform/get")
    Call<UserInfo> getUser(@Header("token") String token);

    @GET()
    Call<ResponseBody> getUserIcon(@Url String url);

    @FormUrlEncoded
    @POST("user/inform/update")
    Call<UpdateUserInfoResponse> updateUsername(@Header("token") String token, @Field("username") String username);

    @FormUrlEncoded
    @POST("user/inform/update")
    Call<UpdateUserInfoResponse> updateUserGender(@Header("token") String token, @Field("gender") String gender);

    @FormUrlEncoded
    @POST("user/inform/update")
    Call<UpdateUserInfoResponse> updateUserIntroduce(@Header("token") String token, @Field("introduce") String introduce);

    @FormUrlEncoded
    @POST("user/inform/update")
    Call<UpdateUserInfoResponse> updateUserBirthDay(@Header("token") String token, @Field("birthday") String birthday);

    @Multipart
    @POST("user/inform/update")
    Call<UpdateUserInfoResponse> updateUserPortrait(@Header("token") String token, @Part MultipartBody.Part file);

}
