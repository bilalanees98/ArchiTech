package com.architech.architech.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Meeting implements Parcelable {
    String meetingId;
    String customerId;
    String contractorId;
    String timestamp;
    String status;
    String contractorName;
    String contractorNumber;
    String customerNumber;
    String customerName;
    String customerEmail;
    String contractorEmail;

    public Meeting(){}
    public Meeting(String meetingId, String customerId, String contractorId, String timestamp, String status, String contractorName
    ,String contractorNumber, String customerNumber, String customerName,String customerEmail,String contractorEmail) {
        this.meetingId = meetingId;
        this.customerId = customerId;
        this.contractorId = contractorId;
        this.timestamp = timestamp;
        this.status = status;
        this.contractorName = contractorName;
        this.contractorNumber = contractorNumber;
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.customerEmail=customerEmail;
        this.contractorEmail=contractorEmail;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getContractorEmail() {
        return contractorEmail;
    }

    public void setContractorEmail(String contractorEmail) {
        this.contractorEmail = contractorEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContractorNumber() {
        return contractorNumber;
    }

    public void setContractorNumber(String contractorNumber) {
        this.contractorNumber = contractorNumber;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getContractorId() {
        return contractorId;
    }

    public void setContractorId(String contractorId) {
        this.contractorId = contractorId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    protected Meeting(Parcel in) {
        meetingId = in.readString();
        customerId = in.readString();
        contractorId = in.readString();
        timestamp = in.readString();
        status = in.readString();
        contractorName = in.readString();
        contractorNumber = in.readString();
        customerNumber = in.readString();
        customerName = in.readString();
        customerEmail = in.readString();
        contractorEmail = in.readString();
    }

    public static final Creator<Meeting> CREATOR = new Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(meetingId);
        dest.writeString(customerId);
        dest.writeString(contractorId);
        dest.writeString(timestamp);
        dest.writeString(status);
        dest.writeString(contractorName);
        dest.writeString(contractorNumber);
        dest.writeString(customerNumber);
        dest.writeString(customerName);
        dest.writeString(customerEmail);
        dest.writeString(contractorEmail);
    }
}
