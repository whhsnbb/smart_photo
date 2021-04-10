package com.example.moduletest.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.moduletest.Data.SpacePhoto;
import com.example.moduletest.R;


import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private List<List<SpacePhoto>> mlist;
    Context mcontext;
    Activity mMctivity;

    public PhotosAdapter(List<List<SpacePhoto>> list,Context context,Activity activity){
        mlist = list;
        mcontext = context;
        mMctivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<SpacePhoto> list = mlist.get(position);
        holder.textView.setText(list.get(0).getmData());
        ItemAdapter itemAdapter = new ItemAdapter(list,mcontext,mMctivity,mlist);
        holder.recyclerView.setAdapter(itemAdapter);

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

//        ImageView imageView1;
        RecyclerView recyclerView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            imageView1 = itemView.findViewById(R.id.img_1);
            textView = itemView.findViewById(R.id.date);
            recyclerView = itemView.findViewById(R.id.list_item);
            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
        }
    }
}
