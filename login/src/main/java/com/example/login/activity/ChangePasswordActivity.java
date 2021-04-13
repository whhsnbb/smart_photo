package com.example.login.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.R;
import com.example.login.api.Login_Api;
import com.example.login.api.Login_ApiConfig;
import com.example.login.api.MyLoginCallback;
import com.example.login.response.LoginResponse;
import com.example.login.util.MobileUtil;
import com.example.login.util.MyCountDownTimer;
import com.example.login.util.StringUtils;
import com.google.gson.Gson;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edt_phone;

    private EditText edt_newPassword;

    private EditText edt_Yzm;

    private TextView tv_getYzm;

    private Button btn_setNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(this,tv_getYzm,60000,1000);
        Intent intent = getIntent();
        edt_phone.setText(intent.getStringExtra("phoneNumber"));
        tv_getYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edt_phone.getText().toString().trim();
                if (MobileUtil.checkPhone(phoneNumber)) {
                    Login_Api.config(Login_ApiConfig.GET_CODE_CHANGE_PASSWORD,phoneNumber, Login_Api.TYPE_GetYzm).postLoginRequest(ChangePasswordActivity.this, Login_Api.TYPE_GetYzm, new MyLoginCallback() {
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
                                        Toast.makeText(ChangePasswordActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ChangePasswordActivity.this,"获取验证码失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(ChangePasswordActivity.this,"请输入正确的11位手机号",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_setNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edt_phone.getText().toString().trim();
                String password = edt_newPassword.getText().toString().trim();
                String yzm = edt_Yzm.getText().toString().trim();
                changePassword(phone,password,yzm);
            }
        });
    }

    private void initView(){
        edt_phone = findViewById(R.id.edt_changePassword_phoneNumber);
        edt_newPassword = findViewById(R.id.edt_changePassword_password);
        edt_Yzm = findViewById(R.id.edt_changePassword_Yzm);
        tv_getYzm = findViewById(R.id.tv_changePassword_getYzm);
        btn_setNewPassword = findViewById(R.id.btn_changePassword);
    }

    private void changePassword(String phone,String password,String yzm){
        if(!MobileUtil.checkPhone(phone)){
            Toast.makeText(this,"请输入正确的11位手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtils.isEmpty(password) || !(password.length() >= 6 && password.length() <= 20)){
            Toast.makeText(this,"请输入6-18位密码",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtils.isEmpty(yzm)){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }
        Login_Api.config(Login_ApiConfig.POST_CODE_CHANGE_PASSWORD,phone,password,yzm).postUpdatePassword(new MyLoginCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("onSuccess", result);
                Gson gson = new Gson();
                LoginResponse loginResponse = gson
                        .fromJson(result, LoginResponse.class);
                if (loginResponse.getData()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChangePasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(ChangePasswordActivity.this, LoginByPasswordActivity.class);
                    startActivity(intent);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChangePasswordActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(final Exception e) {
                Log.e("onFailure", e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangePasswordActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}