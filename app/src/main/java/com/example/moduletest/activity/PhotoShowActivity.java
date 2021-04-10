package com.example.moduletest.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.moduletest.Data.PhotoList;
import com.example.moduletest.R;
import com.example.moduletest.wight.PhotoViewPager;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoShowActivity extends AppCompatActivity {

    private PhotoView photoView;
    private boolean isEdit = false;
    private LinearLayout linearLayout;
    private TextView edit,delete;
    private List list;
    private PhotoViewPager photoViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_show);
        Init();

        photoViewPager.getCurrentItem();
    }

    private void Init(){
        // 状态栏透明|字体为白
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        PhotoList photoList = (PhotoList) getIntent().getSerializableExtra("photoList");
        list = photoList.getList();
        int p = photoList.getPosition();

        linearLayout = findViewById(R.id.to_edit);
        FrameLayout frameLayout = findViewById(R.id.fl);
        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);
        photoViewPager = findViewById(R.id.viewpager);

        photoViewPager.setAdapter(new PagerAdapter() {
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                photoView = new PhotoView(container.getContext());
                photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        isEdit = !isEdit;
                        showEdit();
                    }
                });
                Glide.with(container.getContext())
                        .load(list.get(position))
                        .into(photoView);

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isEdit = !isEdit;
                        showEdit();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(PhotoShowActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("确定要删除这张照片吗？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                                String filePath = (String) photoList.getList().get(position-1);
                                String where = MediaStore.Images.Media.DATA + "='" + filePath + "'";
                                int count = getContentResolver().delete(
                                        uri,where,null);
                                photoList.getList().remove(position-1);
                                notifyDataSetChanged();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });

                container.addView(photoView);
                return photoView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }
        });
        photoViewPager.setCurrentItem(p);
    }

    private void showEdit(){
        if(!isEdit){
            linearLayout.setVisibility(View.GONE);
            Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.bottom_down);
            linearLayout.startAnimation(animation2);

        }else{
            linearLayout.setVisibility(View.VISIBLE);
            Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.bottom_up);
            linearLayout.startAnimation(animation1);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}