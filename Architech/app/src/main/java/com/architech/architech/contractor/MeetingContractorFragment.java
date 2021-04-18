package com.architech.architech.contractor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.architech.architech.R;
import com.architech.architech.model.Contractor;

import org.jetbrains.annotations.Contract;

public class MeetingContractorFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapterMeetingContractor pageAdapter;
    TabItem confirmedTab, requestsTab;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_meeting_contractor, container, false);
        tabLayout= view.findViewById(R.id.tabLayoutMeetingContractor);
        viewPager= view.findViewById(R.id.viewPagerMeetingContractor);
        confirmedTab= view.findViewById(R.id.confirmedTabContractor);
        requestsTab= view.findViewById(R.id.requestsTabContractor);


        //We are setting adapter to our custom adapter.. our custom adapter has ability to return our Fragments we made for inside tabs
        pageAdapter= new PageAdapterMeetingContractor(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    100);
        }

        //we set our current item on selected
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //we are passing our tab layout to listen to change
        //this is done to sync tabs and viewpager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));




        return view;


    }
}
