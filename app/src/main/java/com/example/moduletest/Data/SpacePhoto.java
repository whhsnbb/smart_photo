package com.example.moduletest.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class SpacePhoto implements Parcelable {

    private String mUrl;
    private String mDate;

    public SpacePhoto(String url){
        mUrl = url;
    }
    public SpacePhoto(String url,String data){
        mUrl = url;
        mDate = data;
    }


    protected SpacePhoto(Parcel in) {
        mUrl = in.readString();
        mDate = in.readString();
    }



    public String getmUrl() {
        return mUrl;
    }

    public String getmData() {
        return mDate;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void setmData(String mData) {
        this.mDate = mData;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(mUrl);
        dest.writeString(mDate);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SpacePhoto> CREATOR = new Creator<SpacePhoto>() {
        @Override
        public SpacePhoto createFromParcel(Parcel in) {
            return new SpacePhoto(in);
        }

        @Override
        public SpacePhoto[] newArray(int size) {
            return new SpacePhoto[size];
        }
    };
}
