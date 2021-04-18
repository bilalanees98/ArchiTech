package com.architech.architech.contractor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapterMeetingContractor extends FragmentPagerAdapter {

    int numberOfTabs;

    public PageAdapterMeetingContractor(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numberOfTabs=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MeetingConfirmedContractorFragment();
            case 1:
                return new MeetingRequestedContractorFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
