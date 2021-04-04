package com.example.moduletest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.common.Bean.UserInfo;
import com.example.common.Utils.UserAccountUtils;
import com.example.moduletest.userinfo.InfoService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
    
    private static final String BASE_URL = "http://192.168.1.219/smart_photo/";
    
    private BottomNavigationView bottomNavigationView;
    
    private File iconFile;

    private TextView username;

    private CircleImageView usericon;

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        loadUserInfo(BASE_URL);

    }

    private void Init(){
        NavController navController = Navigation.findNavController(this,R.id.main_fragment);
        bottomNavigationView = findViewById(R.id.main_bar);
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

        //设置点击事件



        //创建用户头像文件

        

    }

    //拉取用户信息
    private void loadUserInfo(String baseurl){

        //请求用户信息
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InfoService infoService = retrofit.create(InfoService.class);

        Call<UserInfo> call = infoService.getUserInfo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTc1Mjk4NTEsInVzZXJJZCI6MzF9." +
                "jj18TYbK9qaSEnseSuPxuqyWRHgKIy7W85w92ZN-tQk1617443451444110019");

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, retrofit2.Response<UserInfo> response) {

                Log.e("TAG",response.body().toString());

                userInfo = response.body();
                if(userInfo != null && userInfo.getStatus() == 1){
                    Log.e("TAG","false_1");
                }else {
                    Log.e("TAG","hhh");
                    Log.e("TAG",userInfo.getData().getPhone());
                    loadUserIcon();
                }
            }
            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.e("TAG",t.getMessage());
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

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://xinil.oss-cn-shanghai.aliyuncs.com/smart_photo/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            InfoService infoService = retrofit.create(InfoService.class);

            Call<ResponseBody> userIcon = infoService.getUserIcon();

            userIcon.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                    InputStream inputStream = null;
                    byte[] bytes = new byte[1024];
                    FileOutputStream fileOutputStream = null;
                    int len;
                    try {

                        if(response.body() == null){
                            Log.e("TAG","null");
                        }

                        inputStream = response.body().byteStream();
                        Log.e("TAG",response.body().byteStream().toString());
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
                    Log.e("TAG",t.getMessage());
                }
            });

//            Log.e("TAG",userInfo.getData().getPortrait());

            Glide.with(this)
                    .load(iconFile)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                    .into(usericon);

        }
        username.setText(userInfo.getData().getUsername());
    }

}