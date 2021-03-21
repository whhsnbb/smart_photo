package com.example.login.util;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.login.R;

public class MyCountDownTimer extends CountDownTimer {
    private Activity mActivity;
    private TextView mTv;

    public MyCountDownTimer(Activity activity,TextView tv, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mActivity = activity;
        this.mTv = tv;
    }

    //计时过程
    @Override
    public void onTick(long l) {
        //防止计时过程中重复点击
        mTv.setClickable(false);
        mTv.setText("重新获取("+l/1000+"秒)");
        mTv.setTextColor(mActivity.getResources().getColor(R.color.colorGetNewYzm));
    }

    //计时完毕的方法
    @Override
    public void onFinish() {
        //重新给Button设置文字
        mTv.setText("重新获取");
        //设置可点击
        mTv.setTextColor(mActivity.getResources().getColor(R.color.colorGetYzm));
        mTv.setClickable(true);
    }
}

