package com.example.mine.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mine.ItemView;
import com.example.mine.R;
import com.example.mine.utils.Utils;

public class HomeActivity extends AppCompatActivity {

    private Button btn_goBack;

    private RelativeLayout item_account;

    private ItemView item_backUp;

    private ItemView item_setting_backUp;

    private ItemView item_encryption;

    private ItemView item_recycler;

    private ItemView item_aboutUs;

    private ItemView item_renew;

    private Button btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        itemSetClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView(){
        Utils.setLucid(HomeActivity.this);
        btn_goBack = findViewById(R.id.btn_toApp);
        item_account = findViewById(R.id.item_account);
        item_backUp = findViewById(R.id.item_backUp);
        item_setting_backUp = findViewById(R.id.item_setting_backUp);
        item_encryption = findViewById(R.id.item_encryption);
        item_recycler = findViewById(R.id.item_recycler);
        item_aboutUs = findViewById(R.id.item_aboutUs);
        item_renew =findViewById(R.id.item_renew);
        btn_exit = findViewById(R.id.btn_exit);
    }

    private void itemSetClickListener(){
        btn_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                *跳回主app逻辑
                *
                * */
            }
        });

        item_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.navigateTo(HomeActivity.this,AccountAvtivity.class);
            }
        });

        item_backUp.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Utils.navigateTo(HomeActivity.this,BackUpActivity.class);
            }
        });

        item_setting_backUp.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Utils.navigateTo(HomeActivity.this,SettingActivity.class);
            }
        });

        item_encryption.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Utils.navigateTo(HomeActivity.this,EncryptionActivity.class);
            }
        });

        item_recycler.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Utils.navigateTo(HomeActivity.this,RecyclerActivity.class);
            }
        });

        item_aboutUs.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Utils.navigateTo(HomeActivity.this,AboutUsactivity.class);
            }
        });

        item_renew.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(HomeActivity.this,"当前为最新版本:" + text,Toast.LENGTH_SHORT).show();
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*跳转到login界面逻辑
                *
                *
                * */
            }
        });
    }


}