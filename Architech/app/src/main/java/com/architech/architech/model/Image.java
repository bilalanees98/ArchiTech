package com.architech.architech.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    String name;
    String floorPlanId;
    String imageWidth;
    String imageLength;

    public Image(String name, String floorPlanId,String imageWidth, String imageLength) {
        this.name = name;
        this.floorPlanId = floorPlanId;
        this.imageWidth = imageWidth;
        this.imageLength = imageLength;

    }
    public Image() {
        this.name=null;
        this.floorPlanId=null;
        this.imageWidth=null;
        this.imageLength=null;
    }

    protected Image(Parcel in) {
        name = in.readString();
        floorPlanId = in.readString();
        imageWidth = in.readString();
        imageLength = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageLength() {
        return imageLength;
    }

    public void setImageLength(String imageLength) {
        this.imageLength = imageLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloorPlanId() {
        return floorPlanId;
    }

    public void setFloorPlanId(String floorPlanId) {
        this.floorPlanId = floorPlanId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(floorPlanId);
        dest.writeString(imageWidth);
        dest.writeString(imageLength);
    }
}

