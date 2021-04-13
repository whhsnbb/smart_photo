package com.example.mine.api;

import com.example.common.Bean.UserInfo;

import okhttp3.ResponseBody;
import retrofit2.Response;

public interface MyUserIconCallback {
    void onSuccess(Response<ResponseBody> response);

    void onFailure(Throwable t);
}
