package com.example.common.Utils;

import com.example.common.Bean.UserInfo;
import com.example.common.Bean.UserToken;

public class UserAccountUtils {

    public static UserInfo userInfo;
    public static UserToken userToken;

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        UserAccountUtils.userInfo = userInfo;
    }

    public static UserToken getUserToken() {
        return userToken;
    }

    public static void setUserToken(UserToken userToken) {
        UserAccountUtils.userToken = userToken;
    }
}
