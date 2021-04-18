package com.architech.architech.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

public class FloorPlanRecyclerObject implements Parcelable {
    String title;
    String owner;
    String width;
    String length;
    String noOfCars;
    String bathrooms;
    String bedrooms;
    String id;
    String ownerName;
    ArrayList<String> paths;

    public FloorPlanRecyclerObject(String title, String owner, String width, String length, String noOfCars, String bathrooms,
                                   String bedrooms,String id, ArrayList<String> paths,String ownerName) {
        this.title = title;
        this.owner = owner;
        this.width = width;
        this.length = length;
        this.noOfCars = noOfCars;
        this.bathrooms = bathrooms;
        this.bedrooms = bedrooms;
        this.id=id;
        this.paths = paths;
        this.ownerName = ownerName;
    }


    public static final Creator<FloorPlanRecyclerObject> CREATOR = new Creator<FloorPlanRecyclerObject>() {
        @Override
        public FloorPlanRecyclerObject createFromParcel(Parcel in) {
            return new FloorPlanRecyclerObject(in);
        }

        @Override
        public FloorPlanRecyclerObject[] newArray(int size) {
            return new FloorPlanRecyclerObject[size];
        }
    };

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getNoOfCars() {
        return noOfCars;
    }

    public void setNoOfCars(String noOfCars) {
        this.noOfCars = noOfCars;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<String> paths) {
        this.paths = paths;
    }

    public void print(){
        Log.d("floorplan",title);
        for(int i=0;i<this.paths.size();i++){
            Log.d("paths",this.paths.get(i));
        }
    }

    public String getSize()
    {
        return length+"x"+width;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected FloorPlanRecyclerObject(Parcel in) {
        title = in.readString();
        owner = in.readString();
        width = in.readString();
        length = in.readString();
        noOfCars = in.readString();
        bathrooms = in.readString();
        bedrooms = in.readString();
        id=in.readString();
        ownerName=in.readString();
        paths = in.createStringArrayList();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(owner);
        dest.writeString(width);
        dest.writeString(length);
        dest.writeString(noOfCars);
        dest.writeString(bathrooms);
        dest.writeString(bedrooms);
        dest.writeString(id);
        dest.writeString(ownerName);
        dest.writeStringList(paths);
    }
}
