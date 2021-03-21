package com.example.login.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.login.R;
import com.example.login.api.Api;
import com.example.login.api.ApiConfig;
import com.example.login.api.MyLoginCallback;
import com.example.login.response.LoginResponse;
import com.example.login.util.SpaceFilter;
import com.google.gson.Gson;

public class SetNewPasswordActivity extends AppCompatActivity {

    private EditText edt_setPassword;

    private Button btn_setNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_new_password);
        initView();
        btn_setNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = edt_setPassword.getText().toString().trim();
                if(newPassword.length() >= 6 && newPassword.length() <= 20){
                    Api.config(ApiConfig.SET_NEW_WORD,newPassword,Api.TYPE_SetNewPassword).postNewPassword(SetNewPasswordActivity.this, new MyLoginCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            final LoginResponse loginResponse = gson.fromJson(result,LoginResponse.class);
                            if(loginResponse.getData()){
                                /*
                                * 跳转到主app逻辑
                                *
                                * */
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SetNewPasswordActivity.this, loginResponse.getMsg(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(final Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SetNewPasswordActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }else{
                    Toast.makeText(SetNewPasswordActivity.this,"请输入6-20位密码",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView(){
        edt_setPassword = findViewById(R.id.edt_set_pwd);
        edt_setPassword.setFilters(new InputFilter[]{new SpaceFilter()});
        btn_setNewPassword = findViewById(R.id.btn_setNewPassword);
    }
}