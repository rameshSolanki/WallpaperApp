package com.fridayapp.wallpaperapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WallpaperModel implements Parcelable {
    @SerializedName("tbl_image_id")
    private int tbl_image_id;
    @SerializedName("page_no")
    private String page_no;
    @SerializedName("image_location")
    private String image_location;


    public WallpaperModel(int tbl_image_id, String page_no, String image_location) {
        this.tbl_image_id = tbl_image_id;
        this.page_no = page_no;
        this.image_location = image_location;
    }

    protected WallpaperModel(Parcel in) {
        tbl_image_id = in.readInt();
        page_no = in.readString();
        image_location = in.readString();
    }

    public static final Creator<WallpaperModel> CREATOR = new Creator<WallpaperModel>() {
        @Override
        public WallpaperModel createFromParcel(Parcel in) {
            return new WallpaperModel(in);
        }

        @Override
        public WallpaperModel[] newArray(int size) {
            return new WallpaperModel[size];
        }
    };

    public int getTbl_image_id() {
        return tbl_image_id;
    }

    public String getPage_no() {
        return page_no;
    }

    public String getImage_location() {
        return image_location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(tbl_image_id);
        parcel.writeString(page_no);
        parcel.writeString(image_location);


    }
}
