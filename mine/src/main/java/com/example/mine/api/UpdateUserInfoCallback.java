package com.example.mine.api;

import com.example.mine.response.UpdateUserInfoResponse;

import retrofit2.Response;

public interface UpdateUserInfoCallback {
    void onSuccess(Response<UpdateUserInfoResponse> response);

    void onFailure(Throwable t);
}
