package com.example.mine.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.mine.api.UpdateUserInfoCallback;
import com.example.mine.response.UpdateUserInfoResponse;
import com.example.mine.utils.RetrofitUtils;
import com.example.mine.utils.Utils;
import com.example.mine.view.ItemView;
import com.example.mine.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Response;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class AccountActivity extends AppCompatActivity {

    private TimePickerView pvTime; //时间选择器对象

    private File iconFile; //图库或者直接加载头像的文件路径

    private File headerFile;

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

    private Bitmap head;// 头像Bitmap

    private Uri imageUri;

    private static String path = Environment.getExternalStorageDirectory().toString();// sd路径

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
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setTitle("选择头像")
                .setItems(new String[]{"从图库选择","拍照"}, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                            intent1.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                            intent1.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent1, 1);
                        }else {
                            headerFile = new File(getExternalCacheDir(),"smart_photo_header.jpg");
                            try{
                                if(headerFile.exists()){
                                    headerFile.delete();
                                }
                                headerFile.createNewFile();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            if(Build.VERSION.SDK_INT >= 24){
                                imageUri = FileProvider.getUriForFile(AccountActivity.this,"com.example.mine.fileprovider",headerFile);
                            }else{
                                imageUri = Uri.fromFile(headerFile);
                            }
                            Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent2.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent2, 2);// 采用ForResult打开
                        }
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        item_username.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick() {
                View view = LayoutInflater.from(AccountActivity.this).inflate(R.layout.layout_input_userinfo,null);
                EditText edt_name = view.findViewById(R.id.edt_input_userInfo);
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setTitle("修改昵称")
                        .setView(view)
                        .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String text = edt_name.getText().toString();
                                RetrofitUtils.updateUserInfo(AccountActivity.this, text, RetrofitUtils.update_username, new UpdateUserInfoCallback() {
                                    @Override
                                    public void onSuccess(Response<UpdateUserInfoResponse> response) {
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("username",text);
                                        editor.apply();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                item_username.setContent(sp.getString("username",""));
                                                Toast.makeText(AccountActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(AccountActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
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
            public void itemClick() {
                View view = LayoutInflater.from(AccountActivity.this).inflate(R.layout.layout_input_userinfo,null);
                EditText edt_introduce = view.findViewById(R.id.edt_input_userInfo);
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setTitle("修改个性签名")
                        .setView(view)
                        .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String text = edt_introduce.getText().toString();
                                RetrofitUtils.updateUserInfo(AccountActivity.this, text, RetrofitUtils.update_introduce, new UpdateUserInfoCallback() {
                                    @Override
                                    public void onSuccess(Response<UpdateUserInfoResponse> response) {
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("introduce",text);
                                        editor.apply();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                item_introduce.setContent(sp.getString("introduce",""));
                                                Toast.makeText(AccountActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(AccountActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
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
            public void itemClick() {
                final String[] nation = new String[]{"男","女"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                if(item_gender.getContent().equals(nation[0])){
                    Utils.showDialog(item_gender, AccountActivity.this, builder, nation, 0);
                }else if(item_gender.getContent().equals(nation[1])){
                    Utils.showDialog(item_gender, AccountActivity.this, builder, nation, 1);
                }else{
                    Utils.showDialog(item_gender, AccountActivity.this, builder, nation, -1);
                }
            }
        });

        item_birthday.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick() {
                initTimePicker();
                pvTime.show();
            }
        });

        item_changePassword.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AccountActivity","onResume()");
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

    }

    //初始化时间选择器
    private void initTimePicker() {
        String birthday = item_birthday.getContent();
        int year = Integer.parseInt(birthday.substring(0,4));
        int month = Integer.parseInt(birthday.substring(5,7));
        Log.d("AccountActivity",month+"");
        int day = Integer.parseInt(birthday.substring(8,10));
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month - 1, day);
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);//起始时间
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 11, 31);//结束时间
        pvTime = new TimePickerView.Builder(AccountActivity.this,
                new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        //选中事件回调
                        String birthday = getTimes(date);
                        RetrofitUtils.updateUserInfo(AccountActivity.this, birthday, RetrofitUtils.update_birthDay, new UpdateUserInfoCallback() {
                            @Override
                            public void onSuccess(Response<UpdateUserInfoResponse> response) {
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("birthday",birthday);
                                editor.apply();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        item_birthday.setContent(sp.getString("birthday",""));
                                        Toast.makeText(AccountActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AccountActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();
    }

    //格式化时间
    private String getTimes(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK){
                    cropPhoto(imageUri);
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        File file = new File(getExternalFilesDir(null),"/user/userIcon");
                        iconFile = new File(file,"user_icon_clip_" +
                                sp.getString("phone","") + ".jpg");
                        setPicToView(head);
                        RetrofitUtils.updateUserIcon(AccountActivity.this, iconFile, new UpdateUserInfoCallback() {
                            @Override
                            public void onSuccess(Response<UpdateUserInfoResponse> response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AccountActivity.this,"呜呼，修改成功，无敌!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AccountActivity.this,"哎嘘，修改失败了！",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        img_portrait.setImageBitmap(head);// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param inputUri
     */
    public void cropPhoto(Uri inputUri) {
        if (inputUri == null) {
            Log.d("AccountActivity","The uri is not exist.");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
// aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
// outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        File file = new File(getExternalFilesDir(null),"/user/userIcon");
        if(!file.exists()){
            file.mkdirs();
        }
        iconFile = new File(file,"user_icon_clip_" +
                sp.getString("phone","") + ".jpg");
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(iconFile);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
// 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}