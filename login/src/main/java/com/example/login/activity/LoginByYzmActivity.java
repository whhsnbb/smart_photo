package com.example.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.login.R;
import com.example.login.response.DataResponse;
import com.example.login.response.LoginResponse;
import com.example.login.util.MobileUtil;
import com.example.login.util.MyCountDownTimer;
import com.example.login.util.StringUtils;

import com.example.login.api.login_Api;
import com.example.login.api.login_ApiConfig;
import com.example.login.api.MyLoginCallback;
import com.google.gson.Gson;

@Route(path = "/Login/LoginActivity")
public class LoginByYzmActivity extends AppCompatActivity {

    private EditText edt_phoneNumber;
    private EditText edt_yzm;
    private Button btn_login;
    private TextView tv_getYzm;
    private TextView tv_navigateToHome_Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_yzm);
        initView();
        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(this,tv_getYzm,60000,1000);
        //设置Button点击事件触发倒计时
        tv_getYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = edt_phoneNumber.getText().toString().trim();
                if (MobileUtil.checkPhone(phoneNumber)) {
                    login_Api.config(login_ApiConfig.GET_CODE, phoneNumber, login_Api.TYPE_GetYzm).postLoginRequest(LoginByYzmActivity.this, login_Api.TYPE_GetYzm, new MyLoginCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            LoginResponse loginResponse = gson
                                    .fromJson(result, LoginResponse.class);
                            if(loginResponse.getData()) {
                                myCountDownTimer.start();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginByYzmActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(final Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginByYzmActivity.this,"获取验证码失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }else{
                    Toast.makeText(LoginByYzmActivity.this,"手机号码输入错误",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edt_phoneNumber.getText().toString().trim();
                String yzm = edt_yzm.getText().toString().trim();
                login(phoneNumber,yzm);
            }
        });

        tv_navigateToHome_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edt_phoneNumber.getText().toString().trim();
                Intent intent = new Intent(LoginByYzmActivity.this, LoginByPasswordActivity.class);
                intent.putExtra("phone",phoneNumber);
                startActivity(intent);
            }
        });

    }
    private void initView(){
        edt_phoneNumber = findViewById(R.id.edt_loginByYzm_phoneNumber);
        edt_yzm = findViewById(R.id.edt_yzm);
        btn_login = findViewById(R.id.btn_loginByYzm);
        tv_getYzm = findViewById(R.id.tv_getYzm);
        tv_navigateToHome_Password = findViewById(R.id.tv_navigateTo_password);
    }

    private void login(final String phoneNumber, String yzm){
        if(!MobileUtil.checkPhone(phoneNumber)){
            Toast.makeText(this,"请输入正确的11位手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtils.isEmpty(yzm)){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }
        login_Api.config(login_ApiConfig.LOGIN_YZM,phoneNumber,yzm, login_Api.TYPE_PostYzm).postLoginRequest(LoginByYzmActivity.this, login_Api.TYPE_PostYzm, new MyLoginCallback() {
            @Override
            public void onSuccess(final String result) {
                Log.e("onSuccess", result);
                Gson gson = new Gson();
                DataResponse dataResponse = gson.fromJson(result, DataResponse.class);
                if (dataResponse.getData().getData()) {
                    if (!dataResponse.getData().getExist()) {
                        Intent intent = new Intent(LoginByYzmActivity.this, SetNewPasswordActivity.class);
                        startActivity(intent);
                    } else {
                        /*
                         * 跳转到主app的逻辑
                         * */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginByYzmActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }


            @Override
            public void onFailure(final Exception e) {
                Log.e("onFailure", e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginByYzmActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}