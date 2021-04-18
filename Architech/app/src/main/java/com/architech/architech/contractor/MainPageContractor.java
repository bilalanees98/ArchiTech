package com.architech.architech.contractor;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.architech.architech.R;
import com.architech.architech.model.Contractor;
import com.architech.architech.model.Customer;

public class MainPageContractor extends AppCompatActivity {

    BottomNavigationView bottomNav;
    Contractor myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_contractor);
        bottomNav= findViewById(R.id.bottomNavbarContractor);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerContractor, new MeetingContractorFragment()).commit();

        populateMyProfile();


    }
    private void populateMyProfile() {
        myProfile= new Contractor("","","","","","","");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Contractors").child(uid);
//        create listener that will listen for any changes in current user table
        ValueEventListener myProfileListener;
        myProfileListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    myProfile = snapshot.getValue(Contractor.class);
                    /*Fragment initialFragment= new MeetingContractorFragment();
                    Bundle bundleForInitial= new Bundle();
                    bundleForInitial.putParcelable("MYPROFILE", myProfile);
                    initialFragment.setArguments(bundleForInitial);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerContractor, initialFragment).commit();
             */
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
//        add listener to that database
        database.addValueEventListener(myProfileListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //item is the item selected
                    Fragment selectedFragment= null;
                    switch (item.getItemId()){
                        case R.id.meetingNavIconContractor:
                            selectedFragment= new MeetingContractorFragment();
                            break;

                        case R.id.profileNavIconContractor:

                            Bundle bundleForProfile= new Bundle();
                            bundleForProfile.putParcelable("MYPROFILE", myProfile);
                            selectedFragment= new ProfileContractorFragment();
                            selectedFragment.setArguments(bundleForProfile);
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerContractor, selectedFragment).commit();
                    return true;
                }


            };
}
