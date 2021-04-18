package com.architech.architech.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Contractor implements Parcelable {
    String name;
    String phone;
    String email;
    String uid;
    String rateOne;
    String rateTwo;
    String rateThree;

    Contractor(){}

    public Contractor(String name, String phone, String email,  String uid, String rateOne, String rateTwo, String rateThree) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.uid = uid;
        this.rateOne = rateOne;
        this.rateTwo = rateTwo;
        this.rateThree = rateThree;
    }



    public static final Creator<Contractor> CREATOR = new Creator<Contractor>() {
        @Override
        public Contractor createFromParcel(Parcel in) {
            return new Contractor(in);
        }

        @Override
        public Contractor[] newArray(int size) {
            return new Contractor[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRateOne() {
        return rateOne;
    }

    public void setRateOne(String rateOne) {
        this.rateOne = rateOne;
    }

    public String getRateTwo() {
        return rateTwo;
    }

    public void setRateTwo(String rateTwo) {
        this.rateTwo = rateTwo;
    }

    public String getRateThree() {
        return rateThree;
    }

    public void setRateThree(String rateThree) {
        this.rateThree = rateThree;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Contractor(Parcel in) {
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        uid = in.readString();
        rateOne = in.readString();
        rateTwo = in.readString();
        rateThree = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(uid);
        dest.writeString(rateOne);
        dest.writeString(rateTwo);
        dest.writeString(rateThree);
    }
}

