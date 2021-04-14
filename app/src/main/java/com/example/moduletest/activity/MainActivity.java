package com.example.moduletest.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.common.Bean.UserInfo;
import com.example.common.Utils.UserAccountUtils;
import com.example.moduletest.R;
import com.example.moduletest.userinfo.InfoService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Route(path = "/Main/MainActivity")
public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://192.168.1.205/smart_photo/";

    private File iconFile;

    private TextView username;

    private CircleImageView usericon;

    private UserInfo userInfo;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        loadUserInfo();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Init(){
        NavController navController = Navigation.findNavController(this,R.id.main_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.main_bar);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
        // 状态栏透明|字体为黑
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        //绑定id
        usericon = findViewById(R.id.user_icon);
        username = findViewById(R.id.user_name);
        RelativeLayout relativeLayout = findViewById(R.id.user_all);
        
        
        //申请权限
        RequestPremission();


        // TODO: 2021/4/6 这里设置点击事件 
        

    }

    //拉取用户信息
    private void loadUserInfo(){

        //请求用户信息
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InfoService infoService = retrofit.create(InfoService.class);

        // TODO: 2021/4/6 Token未获取 
        Call<UserInfo> call = infoService.getUserInfo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTgxMzUwMTAsInVzZXJJZCI6NX0.Vp1aYbzxRtjxN7" +
                "dttGpuY7enPAoOsfN3gmZpenWgfgM1618048610154610374");

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, retrofit2.Response<UserInfo> response) {
                userInfo = response.body();
                if(userInfo != null && userInfo.getStatus() == 1){
                }else {
                    loadUserIcon();
                }
            }
            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                t.getMessage();
            }
        });

    }
    //下载用户头像
    private void loadUserIcon(){
        File file = new File(getExternalFilesDir(null),"/user/userIcon");
        if(!file.exists()){
            file.mkdirs();
        }
        iconFile = new File(file,"user_icon_clip_" +
                userInfo.getData().getPhone() + ".jpg");

        //下载用户头像
        if(iconFile.exists()){

            Glide.with(this)
                    .load(iconFile)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                    .into(usericon);

        }else{
            Log.e("TAG",userInfo.getData().getPortrait());
            // TODO: 2021/4/6 这里Url有点问题，Retrofit有问题 
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://xinil.oss-cn-shanghai.aliyuncs.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            InfoService infoService = retrofit.create(InfoService.class);

            Call<ResponseBody> userIcon = infoService.getUserIcon("smart_photo/default.png");

            userIcon.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                    InputStream inputStream;
                    byte[] bytes = new byte[1024];
                    FileOutputStream fileOutputStream;
                    int len;
                    try {

                        assert response.body() != null;
                        inputStream = response.body().byteStream();
                        fileOutputStream = new FileOutputStream(iconFile);
                        while((len = inputStream.read(bytes)) != -1){
                            fileOutputStream.write(bytes,0,len);
                            fileOutputStream.flush();
                        }
                        inputStream.close();
                        fileOutputStream.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.getMessage();
                }
            });

            Glide.with(this)
                    .load(iconFile)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                    .into(usericon);

        }
        username.setText(userInfo.getData().getUsername());
    }

    private void RequestPremission(){
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    return;
                }else{
                    return;
                }
        }
    }

}