package com.example.mine.response;

import com.example.common.Bean.UserInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Get_userData {

    @POST("user/inform/get")
    Call<UserInfo> getUser(@Header("token")String token);

    @GET("{url}")
    Call<ResponseBody> getUserIcon(@Path("url") String url);

}
