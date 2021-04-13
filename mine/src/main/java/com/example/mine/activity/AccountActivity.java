package com.example.mine.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.mine.view.ItemView;
import com.example.mine.R;

import java.io.File;
import java.util.Arrays;

public class AccountActivity extends AppCompatActivity {

    private File iconFile;

    private SharedPreferences sp;

    private Button btn_account_backToHome;

    private RelativeLayout layout_portrait;

    private ImageView img_portrait;

    private ItemView item_username;

    private ItemView item_introduce;

    private ItemView item_gender;

    private ItemView item_birthday;

    private ItemView item_phone;

    private ItemView item_changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initView();
        setItemClickListener();
    }

    private void initView(){
        btn_account_backToHome = findViewById(R.id.btn_account_toHome);
        layout_portrait = findViewById(R.id.item_portrait);
        item_username = findViewById(R.id.item_account_username);
        item_introduce = findViewById(R.id.item_account_introduce);
        item_gender = findViewById(R.id.item_account_gender);
        item_birthday = findViewById(R.id.item_account_birthday);
        item_phone = findViewById(R.id.item_account_phone);
        item_changePassword = findViewById(R.id.item_account_changePassword);
        img_portrait =findViewById(R.id.img_portrait);
        sp = getSharedPreferences("sp_getUserInfo",MODE_PRIVATE);
    }

    private void setItemClickListener(){

        btn_account_backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layout_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 调获取图片库
                *
                * */
            }
        });

        item_username.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                View view = LayoutInflater.from(AccountActivity.this).inflate(R.layout.layout_input_userinfo,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setTitle("修改昵称")
                        .setView(view)
                        .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*
                                * Retrofit请求
                                * */
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create()
                        .show();
            }
        });

        item_introduce.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                View view = LayoutInflater.from(AccountActivity.this).inflate(R.layout.layout_input_userinfo,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setTitle("修改个性签名")
                        .setView(view)
                        .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*
                                 * Retrofit请求
                                 * */
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create()
                        .show();
            }
        });

        item_gender.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                /*
                *
                *
                * */
            }
        });
        item_changePassword.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {

            }
        });
    }

    @Override
    protected void onResume() {
        File file = new File(getExternalFilesDir(null),"/user/userIcon");
        if(!file.exists()){
            file.mkdirs();
        }
        iconFile = new File(file,"user_icon_clip_" +
                sp.getString("phone","") + ".jpg");
        Glide.with(this)
                .load(iconFile)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                .into(img_portrait);
        item_username.setContent(sp.getString("username",""));
        item_introduce.setContent(sp.getString("introduce",""));
        item_gender.setContent(sp.getString("gender",""));
        item_birthday.setContent(sp.getString("birthday",""));
        item_phone.setContent(sp.getString("phone","").substring(0,3)+"****"+sp.getString("phone","").substring(7,11));
        super.onResume();
    }
}