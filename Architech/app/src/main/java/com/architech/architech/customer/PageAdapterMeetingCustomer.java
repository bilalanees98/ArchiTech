package com.architech.architech.customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.architech.architech.model.Customer;
import com.architech.architech.model.Meeting;

import java.util.ArrayList;

public class PageAdapterMeetingCustomer extends FragmentPagerAdapter {


    int numOfTabs;

    Customer myProfile;
    ArrayList<Meeting> listOfRequestedMeetings;
    ArrayList<Meeting> listOfConfirmedMeetings;

    public PageAdapterMeetingCustomer(@NonNull FragmentManager fm, int behavior,
                                      ArrayList<Meeting> listOfRequestedMeetings,
                                      ArrayList<Meeting> listOfConfirmedMeetings) {
        super(fm, behavior);
        this.numOfTabs=behavior;
        this.listOfConfirmedMeetings = listOfConfirmedMeetings;
        this.listOfRequestedMeetings = listOfRequestedMeetings;
    }

    public void setListOfRequestedMeetings(ArrayList<Meeting> listOfRequestedMeetings) {
        this.listOfRequestedMeetings = listOfRequestedMeetings;
    }

    public void setListOfConfirmedMeetings(ArrayList<Meeting> listOfConfirmedMeetings) {
        this.listOfConfirmedMeetings = listOfConfirmedMeetings;
    }

//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                Bundle bundleForMeetingC = new Bundle();
//                bundleForMeetingC.putParcelableArrayList("CONFIRMEDMEETINGS", listOfConfirmedMeetings);
                MeetingConfirmedCustomerFragment meetingConfirmedCustomerFragment = new MeetingConfirmedCustomerFragment();
//                meetingConfirmedCustomerFragment.setArguments(bundleForMeetingC);
                return meetingConfirmedCustomerFragment;
            case 1:

//                Bundle bundleForMeetingR = new Bundle();
//                bundleForMeetingR.putParcelableArrayList("REQUESTEDMEETINGS", listOfRequestedMeetings);
                MeetingRequestedCustomerFragment meetingRequestedCustomerFragment = new MeetingRequestedCustomerFragment();
//                meetingRequestedCustomerFragment.setArguments(bundleForMeetingR);
                return meetingRequestedCustomerFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
