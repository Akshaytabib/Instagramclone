package com.example.pushnotification.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AddImage implements Parcelable {

    String addimageId,addimageurl,addimageFeed;

    public AddImage() { }

    public AddImage(String addimageurl, String addimageFeed) {
        this.addimageurl = addimageurl;
        this.addimageFeed = addimageFeed;
    }

    public AddImage(String addimageId, String addimageurl, String addimageFeed) {
        this.addimageId = addimageId;
        this.addimageurl = addimageurl;
        this.addimageFeed = addimageFeed;
    }

    protected AddImage(Parcel in) {
        addimageId = in.readString();
        addimageurl = in.readString();
        addimageFeed = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addimageId);
        dest.writeString(addimageurl);
        dest.writeString(addimageFeed);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddImage> CREATOR = new Creator<AddImage>() {
        @Override
        public AddImage createFromParcel(Parcel in) {
            return new AddImage(in);
        }

        @Override
        public AddImage[] newArray(int size) {
            return new AddImage[size];
        }
    };

    public String getAddimageId() {
        return addimageId;
    }

    public void setAddimageId(String addimageId) {
        this.addimageId = addimageId;
    }

    public String getAddimageurl() {
        return addimageurl;
    }

    public void setAddimageurl(String addimageurl) {
        this.addimageurl = addimageurl;
    }

    public String getAddimageFeed() {
        return addimageFeed;
    }

    public void setAddimageFeed(String addimageFeed) {
        this.addimageFeed = addimageFeed;
    }
}
