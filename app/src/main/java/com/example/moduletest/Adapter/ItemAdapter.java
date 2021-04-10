package com.example.moduletest.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moduletest.Data.PhotoList;
import com.example.moduletest.Data.SpacePhoto;
import com.example.moduletest.R;
import com.example.moduletest.activity.PhotoShowActivity;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<SpacePhoto> mlist;
    private Context mcontext;
    private Activity mActivity;
    private List<List<SpacePhoto>> all_list = new ArrayList<>();
    int i = 0,j = 0,k = 0,t = 0;


    public ItemAdapter(List<SpacePhoto> list,Context context,Activity activity,List<List<SpacePhoto>> alllist){
        mcontext = context;
        mlist = list;
        mActivity = activity;
        all_list = alllist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.photos_item,parent,false);
        ViewHolder holder = new ViewHolder(view);


        holder.imageView1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                k = 0;
                int position = holder.getAdapterPosition();
                List<String> list = new ArrayList<>();
                PhotoList photoList = new PhotoList();
                String s = mlist.get(position).getmUrl();
                for(List<SpacePhoto> alist: all_list){
                    for(SpacePhoto spacePhoto : alist){
                        if(s.equals(spacePhoto.getmUrl())){
                            t = k;
                        }
                        list.add(spacePhoto.getmUrl());
                        k++;
                    }
                }
                photoList.setList(list);
                photoList.setPosition(t);
                Intent intent = new Intent(mcontext, PhotoShowActivity.class);
                intent.putExtra("photoList",photoList);
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.activity_enter,0);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SpacePhoto spacePhoto = mlist.get(position);
        Glide.with(mcontext)
                .load(spacePhoto.getmUrl())
                .placeholder(R.drawable.loading)
                .into(holder.imageView1);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.img_1);
        }
    }

}
