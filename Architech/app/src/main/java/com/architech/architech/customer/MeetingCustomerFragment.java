package com.architech.architech.customer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.architech.architech.R;
import com.architech.architech.model.Customer;
import com.architech.architech.model.Favourite;
import com.architech.architech.model.FloorPlanRecyclerObject;
import com.architech.architech.model.Meeting;

import java.util.ArrayList;

public class MeetingCustomerFragment extends Fragment {


    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapterMeetingCustomer pageAdapter;
    TabItem confirmedTab, requestsTab;

    DatabaseReference database;

    Customer myProfile;
    ArrayList<Meeting> listOfRequestedMeetings;
    ArrayList<Meeting> listOfConfirmedMeetings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_meeting_customer, container, false);
        tabLayout= view.findViewById(R.id.tabLayoutMeetingCustomer);
        viewPager= view.findViewById(R.id.viewPagerMeetingCustomer);
        confirmedTab= view.findViewById(R.id.confirmedTabCustomer);
        requestsTab= view.findViewById(R.id.requestsTabCustomer);

//        myProfile= getArguments().getParcelable("MYPROFILE");

        System.out.println("HEYENHEYEHEYEHEYHEYEH");
        listOfRequestedMeetings = new ArrayList<>();
        listOfConfirmedMeetings = new ArrayList<>();

        //We are setting adapter to our custom adapter.. our custom adapter has ability to return our Fragments we made for inside tabs
        pageAdapter= new PageAdapterMeetingCustomer(getChildFragmentManager(), tabLayout.getTabCount(),
                listOfRequestedMeetings, listOfConfirmedMeetings);
        viewPager.setAdapter(pageAdapter);

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    100);
        }

//        database = FirebaseDatabase.getInstance().getReference().child("Meetings");
//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    listOfRequestedMeetings.clear();
//                    listOfConfirmedMeetings.clear();
//                    for (DataSnapshot s : snapshot.getChildren()) {
//                        Meeting newMeeting = s.getValue(Meeting.class);
//
//                        Log.d("MeetingGotten", "onDataChange: " + newMeeting.getMeetingId());
//                        if (newMeeting.getStatus().equals("pending")) {
//                            listOfRequestedMeetings.add(newMeeting);
//                        } else if (newMeeting.getStatus().equals("confirmed")) {
//                            listOfConfirmedMeetings.add(newMeeting);
//                        }
//                    }
//                    pageAdapter.setListOfConfirmedMeetings(listOfConfirmedMeetings);
//                    pageAdapter.setListOfRequestedMeetings(listOfRequestedMeetings);
//                    pageAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


//        //We are setting adapter to our custom adapter.. our custom adapter has ability to return our Fragments we made for inside tabs
//        pageAdapter= new PageAdapterMeetingCustomer(getChildFragmentManager(), tabLayout.getTabCount(),
//                myProfile, listOfRequestedMeetings, listOfConfirmedMeetings);
//        viewPager.setAdapter(pageAdapter);


        //we set our current item on selected
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                Log.i("TAB_MOVE","on tab selected called");
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                Log.i("TAB_MOVE","on tab unselected called");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                Log.i("TAB_MOVE","on tab reselected called");
            }
        });


        //we are passing our tab layout to listen to change
        //this is done to sync tabs and viewpager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;

    }


}
