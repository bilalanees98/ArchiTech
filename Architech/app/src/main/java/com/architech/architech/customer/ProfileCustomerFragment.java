package com.architech.architech.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.architech.architech.MainActivity;
import com.architech.architech.R;
import com.architech.architech.model.Customer;
import com.architech.architech.model.Favourite;
import com.architech.architech.model.FloorPlan;
import com.architech.architech.model.FloorPlanRecyclerObject;
import com.architech.architech.model.Image;

import java.util.ArrayList;

public class ProfileCustomerFragment extends Fragment {

    Customer myProfile;
    ArrayList<FloorPlan> listOfFloorPlans;

    Button logoutButton;
    ProgressBar progressBar;
    TextView name, phone, email;
    RecyclerView recyclerView;
    ProfileCustomerFragmentRvAdapter rvAdapter;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //getting from main page activity
        String uid;
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        listOfFloorPlans = new ArrayList<>();
//        listOfFloorPlans= getArguments().getParcelableArrayList("FLOORPLANS");
        View view= inflater.inflate(R.layout.fragment_profile_customer,container,false);


        name=  view.findViewById(R.id.nameCustomerProfileFrag);
        phone=  view.findViewById(R.id.phoneNoCustomerProfileFrag);
        email=  view.findViewById(R.id.emailCustomerProfileFrag);
        progressBar= view.findViewById(R.id.progressBarProfileCustomerFragment);
        logoutButton= view.findViewById(R.id.logoutButtonCustomer);

        buildRecyclerView(view); //does what it says
        getMyProfile(uid);



        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();

                Intent goToMain = new Intent(getActivity(), LoginCustomer.class);
                goToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToMain);
            }
        });

        return view;

    }

    private void buildRecyclerView(View view) {
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerViewProfileCustomer);
        RecyclerView.LayoutManager lm= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        rvAdapter=new ProfileCustomerFragmentRvAdapter(listOfFloorPlans, getActivity());
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void getMyProfile(String uid){
        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    myProfile=snapshot.getValue(Customer.class);
                    name.setText(myProfile.getName());
                    phone.setText(myProfile.getPhone());
                    email.setText(myProfile.getEmail());
                    setMyFloorPlans(); //out of all floor plans, filters for my profile
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void setMyFloorPlans() {
                    DatabaseReference dbFp = FirebaseDatabase.getInstance().getReference().child("Floorplans");
                    Query queryFp = dbFp.orderByChild("owner").equalTo(myProfile.getUid());
                    queryFp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                listOfFloorPlans.clear();
                                for(DataSnapshot s: snapshot.getChildren()){
                                    FloorPlan newFp = s.getValue(FloorPlan.class);
                                    listOfFloorPlans.add(new FloorPlan(
                                            newFp.getTitle(),
                                            newFp.getOwner(),
                                            newFp.getWidth(),
                                            newFp.getLength(),
                                            newFp.getNoOfCars(),
                                            newFp.getBathrooms(),
                                            newFp.getBedrooms(),
                                            newFp.getId(),
                                            newFp.getOwnerName(),
                                            newFp.getCroppedWidth(),
                                            newFp.getCroppedLength(),
                                            newFp.getImageUrl(),
                                            newFp.getPercentageCoveredArea(),
                                            newFp.getCostEstimate()
                                    ));
                                    rvAdapter.notifyDataSetChanged();
                                }
                                progressBar.setVisibility(View.GONE);
                                rvAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
}






