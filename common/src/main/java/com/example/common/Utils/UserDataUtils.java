package com.example.common.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDataUtils {
    public static String getToken(Context context){
        SharedPreferences sp = context.getSharedPreferences("sp_smartPhoto",Context.MODE_PRIVATE);
        String token = sp.getString("token","");
        return token;
    }
}
