package com.architech.architech.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.architech.architech.model.Customer;
import com.architech.architech.model.FloorPlan;

import java.util.ArrayList;

public class MainPageCustomer extends AppCompatActivity {

    BottomNavigationView bottomNav;
    TextView title;
    LinearLayout backButton;

    Customer myProfile;


    //ArrayList<FloorPlanRecyclerObject> listOfFloorPlans;


    ArrayList<FloorPlan> listOfFloorPlansFromDB;
//    ArrayList<Image> listOfImages;
//    ArrayList<FloorPlanRecyclerObject> listOfFloorPlans;


    String uid;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_customer);


        backButton= findViewById(R.id.backButtonMainPageCustomer);



        title= findViewById(R.id.titleMainPageCustomer);
        bottomNav= findViewById(R.id.bottomNavbarCustomer);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);


        mAuth = FirebaseAuth.getInstance();//getting authentication manager instance
        uid = mAuth.getCurrentUser().getUid();//get firebase user id of current user
        Log.i("USERID",uid);


        //populate data
        populateMyProfile(); // does what it says on the tin
        populateData(); // populates floor plans and images, then on success initializes first fragment only

        Fragment initialFragment;
        if(getIntent().getStringExtra("FRAGMENT")!=null){
            if(getIntent().getStringExtra("FRAGMENT").equals("meetingcancelledbycontractor")){
                bottomNav.setSelectedItemId(R.id.homeNavIcon);
                title.setText("Meetings"); //initially
                initialFragment=new MeetingCustomerFragment();
            }
            else{

                title.setText("Home"); //initially
                initialFragment=new HomeCustomerFragment();
            }
        }
        else{

            title.setText("Home"); //initially
            initialFragment=new HomeCustomerFragment();

        }


        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerCustomer,initialFragment).commitAllowingStateLoss();



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNav.setSelectedItemId(R.id.homeNavIcon);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.homeNavIcon);
        title.setText("Home");
    }

    private void populateMyProfile() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
//        create listener that will listen for any changes in current user table
        ValueEventListener myProfileListener;
        myProfileListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    myProfile = snapshot.getValue(Customer.class);
//                    myProfile = new Customer(myProfileContact);
                    /*if (!myProfile.getImageUrl().isEmpty()) {
                        Picasso.get().load(myProfile.getImageUrl()).into(searchBarProfileIcon);//to set image from firebase storage with picasso
                    }*/

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
//        add listener to that database
        database.addValueEventListener(myProfileListener);
    }

    private void populateData() {
        listOfFloorPlansFromDB=new ArrayList<>();
    }


    //we are creating a nav listener here that is passed to the nav bar above
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener= new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment= new HomeCustomerFragment();

            switch (item.getItemId()){
                case R.id.homeNavIcon:
//
                    selectedFragment= new HomeCustomerFragment();
//                    selectedFragment.setArguments(bundleForHome);
                    hideBackButton();
                    title.setText("Home");
                    break;

                case R.id.exploreNavIcon:
                    selectedFragment= new ExploreCustomerFragment();
                    title.setText("Explore");
                    showBackButton();
                    break;

                case R.id.favouriteNavIcon:
                    Bundle bundleForFav = new Bundle();
                    bundleForFav.putParcelable("MYPROFILE", myProfile);
//                    bundleForFav.putParcelableArrayList("FLOORPLANS", new ArrayList<>(listOfFloorPlans));
                    selectedFragment= new FavCustomerFragment();
                    selectedFragment.setArguments(bundleForFav);
                    showBackButton();
                    title.setText("Favourites");
                    break;


                case R.id.profileNavIcon:
                    Bundle bundleForProfile = new Bundle();
                    bundleForProfile.putParcelable("MYPROFILE", myProfile);
//                    bundleForProfile.putParcelableArrayList("FLOORPLANS", listOfFloorPlans);
                    selectedFragment= new ProfileCustomerFragment();
                    selectedFragment.setArguments(bundleForProfile);
                    showBackButton();
                    title.setText("Profile");
                    break;

                    /*case R.id.meetingNavIcon:
                    Bundle bundleForMeeting = new Bundle();
                    bundleForMeeting.putParcelable("MYPROFILE", myProfile);
                    selectedFragment= new MeetingCustomerFragment();
                    selectedFragment.setArguments(bundleForMeeting);

                    title.setText("Meetings");
                    break;
*/

                case R.id.addNavIcon:
                    Intent intent= new Intent(MainPageCustomer.this, AddFloorPlan.class);
                    intent.putExtra("MYPROFILE",myProfile);
                    startActivity(intent);
                    break;


            }


            //this line is used to replace the fragment into the framelayout in the xml with the selected fragment
            if(selectedFragment!=null)
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerCustomer,selectedFragment).commit();
            return true;
        }

    };


    void switchFragmentOnTileClick(int tileNumber)
    {
        //EXPLORE
        if(tileNumber==1) {
            bottomNav.setSelectedItemId(R.id.exploreNavIcon);
        }

        //3D or ADD FLOOR PLAN ACTIVITY
        else if(tileNumber==2) {
            Toast.makeText(this, "Add image/details", Toast.LENGTH_SHORT).show();
            bottomNav.setSelectedItemId(R.id.addNavIcon);
        }
        else if(tileNumber==3) {
            bottomNav.setSelectedItemId(R.id.favouriteNavIcon);
        }

        //PROFILE
        else{
            bottomNav.setSelectedItemId(R.id.profileNavIcon);
        }



    }

    void showBackButton(){
        backButton.setVisibility(View.VISIBLE);

        float scale = getResources().getDisplayMetrics().density;
        int vertPixels = (int) (16*scale + 0.5f);

        title.setPadding(0, vertPixels,0,vertPixels);
    }


    void hideBackButton(){
        backButton.setVisibility(View.GONE);

        float scale = getResources().getDisplayMetrics().density;
        int leftPixels = (int) (20*scale + 0.5f);
        int vertPixels = (int) (16*scale + 0.5f);

        title.setPadding(leftPixels, vertPixels,0,vertPixels);
    }


}
