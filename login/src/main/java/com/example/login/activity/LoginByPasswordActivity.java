package com.example.login.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import com.example.login.util.StringUtils;
import com.google.gson.Gson;

public class LoginByPasswordActivity extends AppCompatActivity {

    private EditText edt_phoneNumber;

    private EditText edt_password;

    private TextView tv_forgetPassword;

    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_password);
        initView();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edt_phoneNumber.getText().toString().trim();
                String yzm = edt_password.getText().toString().trim();
                login(phoneNumber,yzm);
            }
        });
        tv_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginByPasswordActivity.this, ChangePasswordActivity.class);
                intent.putExtra("phoneNumber",edt_phoneNumber.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    private void initView(){
        edt_password = findViewById(R.id.edt_password);
        edt_phoneNumber =findViewById(R.id.edt_loginByPassword_phoneNumber);
        btn_login = findViewById(R.id.btn_loginByPassword);
        tv_forgetPassword = findViewById(R.id.tv_forgetPassword);
        String str="<font color='#7D7D7D'>忘记了?</font><font color='#0099FF'>找回密码</font>";
        tv_forgetPassword.setText(Html.fromHtml(str));
        String phone = getIntent().getStringExtra("phone");
        edt_phoneNumber.setText(phone);
    }

    private void login(String phoneNumber,String password){
        if(!MobileUtil.checkPhone(phoneNumber)){
            Toast.makeText(this,"请输入正确的11位手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtils.isEmpty(password)){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
        Login_Api.config(Login_ApiConfig.LOGIN_PASSWORD,phoneNumber,password, Login_Api.TYPE_Password).postLoginRequest(LoginByPasswordActivity.this, Login_Api.TYPE_Password, new MyLoginCallback() {
            @Override
            public void onSuccess(final String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        LoginResponse loginResponse = gson
                                .fromJson(result, LoginResponse.class);
                        if(loginResponse.getData()) {
                            Toast.makeText(LoginByPasswordActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            /*
                            *
                            *
                            * */
                        }else {
                            Toast.makeText(LoginByPasswordActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginByPasswordActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}