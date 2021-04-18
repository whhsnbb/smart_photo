package com.nullpointerexception.edit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.nullpointerexception.edit.cropper.CropImageView;

@Route(path = "/edit/EditActivity")
public class EditActivity extends AppCompatActivity
        implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{


    @Autowired(name = "path")
    String path;



    private static final int MAX_VALUE = 255;
    private static final int MID_VALUE = 127;


    private CropImageView cropImageView;
    private float mHue, mSaturation, mLum;
    private Bitmap mBitmap;
    private LinearLayout sethue,setsat,setlum,tocut,yes;
    private SeekBar seekBar_hue,seekBar_sat,seekBar_lum;
    private View none;

    private Animation in_animation;
    private Animation out_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ARouter.getInstance().inject(this);
        Init();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mBitmap = BitmapFactory.decodeFile(path);
                seekBar_lum.setMax(MAX_VALUE);
                seekBar_hue.setMax(MAX_VALUE);
                seekBar_sat.setMax(MAX_VALUE);

                seekBar_sat.setProgress(MID_VALUE);
                seekBar_lum.setProgress(MID_VALUE);
                seekBar_hue.setProgress(MID_VALUE);
                Thread.interrupted();
            }
        }).start();





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


        Log.e("TAG",path);


        cropImageView = findViewById(R.id.Img);

        Glide.with(this)
                .load(path)
                .into(cropImageView);




        none = findViewById(R.id.none);

        sethue = findViewById(R.id.set_hue);
        setsat = findViewById(R.id.set_sat);
        setlum = findViewById(R.id.set_lum);
        tocut = findViewById(R.id.to_cut);
        yes = findViewById(R.id.yes);
        seekBar_hue = findViewById(R.id.hue);
        seekBar_sat = findViewById(R.id.sat);
        seekBar_lum = findViewById(R.id.lum);

        sethue.setOnClickListener(this);
        setsat.setOnClickListener(this);
        setlum.setOnClickListener(this);
        tocut.setOnClickListener(this);
        yes.setOnClickListener(this);
        cropImageView.setOnClickListener(this);

        seekBar_hue.setOnSeekBarChangeListener(this);
        seekBar_sat.setOnSeekBarChangeListener(this);
        seekBar_lum.setOnSeekBarChangeListener(this);
        in_animation = AnimationUtils.loadAnimation(this,R.anim.seekbar_into_anim);
        out_animation = AnimationUtils.loadAnimation(this,R.anim.seekbar_out_anim);
    }


    @Override
    public void onClick(View v) {


        int id = v.getId();
        if (id == R.id.set_hue) {
            none.setVisibility(View.GONE);
            seekBar_hue.setVisibility(View.VISIBLE);
            seekBar_sat.setVisibility(View.GONE);
            seekBar_lum.setVisibility(View.GONE);
            seekBar_hue.startAnimation(in_animation);
        } else if (id == R.id.set_sat) {
            none.setVisibility(View.GONE);
            seekBar_hue.setVisibility(View.GONE);
            seekBar_sat.setVisibility(View.VISIBLE);
            seekBar_lum.setVisibility(View.GONE);
            seekBar_sat.startAnimation(in_animation);
        } else if (id == R.id.set_lum) {
            none.setVisibility(View.GONE);
            seekBar_hue.setVisibility(View.GONE);
            seekBar_sat.setVisibility(View.GONE);
            seekBar_lum.setVisibility(View.VISIBLE);
            seekBar_lum.startAnimation(in_animation);
        } else if (id == R.id.to_cut) {
            tocut.setVisibility(View.GONE);
            yes.setVisibility(View.VISIBLE);

            cropImageView.toCut();
            cropImageView.invalidate();

            if (seekBar_sat.getVisibility() == View.VISIBLE) {
                seekBar_sat.startAnimation(out_animation);
            }
            if (seekBar_lum.getVisibility() == View.VISIBLE) {
                seekBar_lum.startAnimation(out_animation);
            }
            if (seekBar_hue.getVisibility() == View.VISIBLE) {
                seekBar_hue.startAnimation(out_animation);
            }
            none.setVisibility(View.VISIBLE);
            seekBar_hue.setVisibility(View.GONE);
            seekBar_sat.setVisibility(View.GONE);
            seekBar_lum.setVisibility(View.GONE);
        } else if (id == R.id.Img) {
            if (seekBar_sat.getVisibility() == View.VISIBLE) {
                seekBar_sat.startAnimation(out_animation);
            }
            if (seekBar_lum.getVisibility() == View.VISIBLE) {
                seekBar_lum.startAnimation(out_animation);
            }
            if (seekBar_hue.getVisibility() == View.VISIBLE) {
                seekBar_hue.startAnimation(out_animation);
            }
            none.setVisibility(View.VISIBLE);
            seekBar_hue.setVisibility(View.GONE);
            seekBar_sat.setVisibility(View.GONE);
            seekBar_lum.setVisibility(View.GONE);
        } else if (id == R.id.yes) {
            yes.setVisibility(View.GONE);
            tocut.setVisibility(View.VISIBLE);

            mBitmap = cropImageView.getCroppedImage();
            cropImageView.setImageBitmap(mBitmap);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        int id = seekBar.getId();
        if (id == R.id.hue) {
            mHue = (progress - MID_VALUE) * 1.0f / MID_VALUE * 18;
        } else if (id == R.id.sat) {
            mSaturation = progress * 1.0f / MID_VALUE;
        } else if (id == R.id.lum) {
            mLum = progress * 1.0f / MID_VALUE;
        }

        cropImageView.setImageBitmap(ImageHelper.handleImageEffect(mBitmap, mHue, mSaturation, mLum));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}