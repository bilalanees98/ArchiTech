package com.architech.architech.model;


import android.os.Parcel;
import android.os.Parcelable;

//CURRENTLY NO FINAL JUST PROTOTYPING
public class FloorPlan implements Parcelable {
    String title;
    String owner;//is firebase id of user
    String width;
    String length;
    String noOfCars;
    String bathrooms;
    String bedrooms;
    String id;
    String ownerName;//is actual name of user e.g hyder

    String croppedWidth;
    String croppedLength;
    String imageUrl;
    String percentageCoveredArea;
    String costEstimate;

    public FloorPlan() { }

    public FloorPlan(String title, String owner, String width, String length, String noOfCars, String bathrooms,
                     String bedrooms, String id, String ownerName, String croppedWidth, String croppedLength,
                     String imageUrl, String percentageCoveredArea, String costEstimate) {
        this.title = title;
        this.owner = owner;
        this.width = width;
        this.length = length;
        this.noOfCars = noOfCars;
        this.bathrooms = bathrooms;
        this.bedrooms = bedrooms;
        this.id = id;
        this.ownerName = ownerName;
        this.croppedWidth = croppedWidth;
        this.croppedLength = croppedLength;
        this.imageUrl = imageUrl;
        this.percentageCoveredArea = percentageCoveredArea;
        this.costEstimate = costEstimate;
    }

    protected FloorPlan(Parcel in) {
        title = in.readString();
        owner = in.readString();
        width = in.readString();
        length = in.readString();
        noOfCars = in.readString();
        bathrooms = in.readString();
        bedrooms = in.readString();
        id = in.readString();
        ownerName = in.readString();
        croppedWidth = in.readString();
        croppedLength = in.readString();
        imageUrl = in.readString();
        percentageCoveredArea = in.readString();
        costEstimate = in.readString();
    }

    public static final Creator<FloorPlan> CREATOR = new Creator<FloorPlan>() {
        @Override
        public FloorPlan createFromParcel(Parcel in) {
            return new FloorPlan(in);
        }

        @Override
        public FloorPlan[] newArray(int size) {
            return new FloorPlan[size];
        }
    };

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCroppedWidth() {
        return croppedWidth;
    }

    public void setCroppedWidth(String croppedWidth) {
        this.croppedWidth = croppedWidth;
    }

    public String getCroppedLength() {
        return croppedLength;
    }

    public void setCroppedLength(String croppedLength) {
        this.croppedLength = croppedLength;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPercentageCoveredArea() {
        return percentageCoveredArea;
    }

    public void setPercentageCoveredArea(String percentageCoveredArea) {
        this.percentageCoveredArea = percentageCoveredArea;
    }

    public String getCostEstimate() {
        return costEstimate;
    }

    public void setCostEstimate(String costEstimate) {
        this.costEstimate = costEstimate;
    }

    public String getSize()
    {
        return length+"x"+width;
    }

    public void display()
    {
        System.out.println("Title:"+ title);
        System.out.println("Size"+ width + " x " + length);
        System.out.println("by: "+ owner);

    }

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeString(croppedWidth);
        dest.writeString(croppedLength);
        dest.writeString(imageUrl);
        dest.writeString(percentageCoveredArea);
        dest.writeString(costEstimate);
    }
}
