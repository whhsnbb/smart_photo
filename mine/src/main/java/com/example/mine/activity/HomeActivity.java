package com.example.mine.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.common.Bean.UserInfo;
import com.example.mine.api.MyUserIconCallback;
import com.example.mine.api.MyUserInfoCallback;
import com.example.mine.utils.InternetUtils;
import com.example.mine.utils.RetrofitUtils;
import com.example.mine.view.ItemView;
import com.example.mine.R;
import com.example.mine.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.example.common.Utils.UserAccountUtils.userInfo;

public class HomeActivity extends AppCompatActivity {

    private File iconFile;

    private SharedPreferences sp;

    private Button btn_goBack;

    private RelativeLayout item_account;

    private ImageView img_header;

    private TextView tv_home_username;

    private TextView tv_home_introduce;

    private ItemView item_backUp;

    private ItemView item_setting_backUp;

    private ItemView item_encryption;

    private ItemView item_recycler;

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
        if (InternetUtils.isNetworkAvailable(this)) {
            RetrofitUtils.getUserInfo(this, new MyUserInfoCallback() {
                @Override
                public void onSuccess(Response<UserInfo> response) {
                    String userName = response.body().getData().getUsername();
                    String introduce = response.body().getData().getIntroduce();
                    SharedPreferences.Editor editor = sp.edit();
                    userInfo = response.body();
                    editor.putString("username", userInfo.getData().getUsername());
                    editor.putString("introduce", userInfo.getData().getIntroduce());
                    editor.putString("gender", userInfo.getData().getGender());
                    editor.putString("birthday", userInfo.getData().getBirth());
                    editor.putString("phone", userInfo.getData().getPhone());
                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (userInfo != null && userInfo.getStatus() == 1) {

                            } else {
                                tv_home_username.setText(userName);
                                tv_home_introduce.setText(introduce);
                                loadUserIcon();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e("HomeActivity", t.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HomeActivity.this,"八嘎，没有网络哎",Toast.LENGTH_SHORT).show();
                        }
                    });
                    tv_home_username.setText(sp.getString("username", ""));
                    tv_home_introduce.setText(sp.getString("introduce", ""));
                    loadUserIcon();
                }
            });
        }else{
            Toast.makeText(this,"没有网络哎",Toast.LENGTH_SHORT).show();
            tv_home_username.setText(sp.getString("username", ""));
            tv_home_introduce.setText(sp.getString("introduce", ""));
            loadUserIcon();

        }
        super.onResume();
    }

    private void loadUserIcon(){
        File file = new File(getExternalFilesDir(null),"/user/userIcon");
        if(!file.exists()){
            file.mkdirs();
        }
        iconFile = new File(file,"user_icon_clip_" +
                sp.getString("phone","") + ".jpg");

        //下载用户头像
        if(iconFile.exists()){

            Glide.with(this)
                    .load(iconFile)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                    .into(img_header);
        }else{
            RetrofitUtils.getUserIcon(this, new MyUserIconCallback() {
                @Override
                public void onSuccess(Response<ResponseBody> response) {
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

                    Glide.with(HomeActivity.this)
                            .load(iconFile)
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                            .into(img_header);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
    }

    private void initView(){
        Utils.setLucid(HomeActivity.this);
        btn_goBack = findViewById(R.id.btn_home_toApp);
        item_account = findViewById(R.id.item_account);
        img_header = findViewById(R.id.img_home_header);
        tv_home_username = findViewById(R.id.tv_home_username);
        tv_home_introduce = findViewById(R.id.tv_home_introduce);
        item_backUp = findViewById(R.id.item_backUp);
        item_setting_backUp = findViewById(R.id.item_setting_backUp);
        item_encryption = findViewById(R.id.item_encryption);
        item_recycler = findViewById(R.id.item_recycler);
        item_renew =findViewById(R.id.item_renew);
        btn_exit = findViewById(R.id.btn_exit);
        sp = getSharedPreferences("sp_getUserInfo",MODE_PRIVATE);
    }

    private void itemSetClickListener(){
        btn_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        item_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.navigateTo(HomeActivity.this, AccountActivity.class);
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