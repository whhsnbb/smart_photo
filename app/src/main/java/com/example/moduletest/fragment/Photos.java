package com.example.moduletest.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moduletest.Adapter.PhotosAdapter;
import com.example.moduletest.Data.SpacePhoto;
import com.example.moduletest.R;
import com.example.moduletest.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class Photos extends Fragment {

    private RecyclerView photos_rv;

    private Map<String,List<SpacePhoto>> all_photo = new LinkedHashMap<>();
    private List<List<SpacePhoto>> all_list = new ArrayList<>();
    private int lastPosition = 0; //位置
    private int lastOffset = 0;  // 偏移量


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.photos_fragment,container,false);
        initView(view);
        return view;
    }

    private void initView(View view){
        photos_rv = view.findViewById(R.id.photos_rv);
        init();
        initListener();
    }

    private void init(){

    }

    private void initListener(){

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();


        searchAllPicture();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());


        photos_rv.setLayoutManager(linearLayoutManager);
        PhotosAdapter adapter = new PhotosAdapter(all_list,getContext(),getActivity());
        photos_rv.setAdapter(adapter);

        photos_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                getPositionAndOffset();
            }
        });
        linearLayoutManager.scrollToPositionWithOffset(lastPosition,lastOffset);
    }

    public void getPositionAndOffset(){
        LinearLayoutManager layoutManager = (LinearLayoutManager)photos_rv.getLayoutManager();
        //获取可视的第一个view
        View topView = layoutManager.getChildAt(0);
        if(topView != null) {
            //获取与该view的顶部的偏移量
            lastOffset = topView.getTop();
            //得到该View的数组位置
            lastPosition = layoutManager.getPosition(topView);
        }
    }



    private void searchAllPicture(){

        all_list.clear();
        all_photo.clear();

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projImage = {MediaStore.Images.Media._ID,
                                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,};

        Cursor cursor = getActivity().getContentResolver().query(mImageUri,projImage,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "+
                        MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png","image/jpg"},
                MediaStore.Images.Media.DATE_TAKEN+" desc");


        if(cursor != null){

            while(cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                SimpleDateFormat s = new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
                String time = s.format(new Date(Long.valueOf(date)));
                if(all_photo.containsKey(time.substring(0,10))){
                    all_photo.get(time.substring(0,10)).add(new SpacePhoto(path,time.substring(0,10)));
                }else{
                    all_photo.put(time.substring(0,10),new ArrayList<>());
                    cursor.moveToPrevious();
                }
            }
        }
        for(String s : all_photo.keySet()){
            all_list.add(all_photo.get(s));
        }

    }



}
