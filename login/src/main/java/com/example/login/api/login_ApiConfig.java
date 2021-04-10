package com.example.login.api;

public class login_ApiConfig {
    public static String BASE_URL = "http:/10.0.2.2:80";
    public static String GET_CODE = "/smart_photo/user/login/getCode";  //获取验证码（登录时）;
    public static String LOGIN_YZM = "/smart_photo/user/login/judgeCode";  //手机号验证码登录;
    public static String LOGIN_PASSWORD = "/smart_photo/user/login/password";  //手机号密码登录;
    public static String SET_NEW_WORD = "/smart_photo/user/inform/updatepwd";  //初始登录设置新密码;
    public static String GET_CODE_CHANGE_PASSWORD = "/smart_photo/user/updatepassword/getCode";  //修改密码时发送验证码;
    public static String POST_CODE_CHANGE_PASSWORD = "/smart_photo/user/updatepassword/judgeCode";  //修改密码时校验验证码;
}
