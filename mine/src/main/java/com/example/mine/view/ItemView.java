package com.example.mine.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.mine.R;

public class ItemView extends LinearLayout{

    private TextView content;

    private TextView title;

    private ImageView img_right;

    private RelativeLayout rootView;    //整体item的view

    public ItemView(Context context) {
        this(context,null);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.item_mine,null);
        addView(view);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        img_right = findViewById(R.id.right_arrow);
        title =findViewById(R.id.tv_title);
        content = findViewById(R.id.tv_content);
        rootView= findViewById(R.id.root_item);
        title.setText(ta.getString(R.styleable.ItemView_tv_title));//设置左侧标题文字
        content.setText(ta.getString(R.styleable.ItemView_tv_content));//设置右侧文字描述
        img_right.setVisibility(ta.getBoolean(R.styleable.ItemView_show_right_arrow,true) ? View.VISIBLE : View.INVISIBLE);
        rootView.setClickable(ta.getBoolean(R.styleable.ItemView_clickable,true));
        if(rootView.isClickable()) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick();
                }
            });
        }

        ta.recycle();
    }

    //设置左侧标题文字
    public void setTitle(String value) {
        title.setText(value);
    }

    //设置右侧描述文字
    public void setContent(String value) {
        content.setText(value);
    }

    public String getContent() {
        return content.getText().toString();
    }

    public String getTitle() {
        return title.getText().toString();
    }

    public void setShowRightArrow(boolean value) {
        img_right.setVisibility(value ? View.VISIBLE : View.INVISIBLE);//设置右侧箭头图标是否显示
    }


    public interface itemClickListener{
        void itemClick();
    }

    private itemClickListener listener;

    //向外暴漏接口
    public void setItemClickListener(itemClickListener listener){
        this.listener=listener;
    }

}
