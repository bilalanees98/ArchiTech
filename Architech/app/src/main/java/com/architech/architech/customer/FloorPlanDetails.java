package com.architech.architech.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.architech.architech.NetworkConfigurations;
import com.architech.architech.SplashScreen;
import com.architech.architech.UnityActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.architech.architech.R;
import com.architech.architech.model.Favourite;
import com.architech.architech.model.FloorPlan;
import com.architech.architech.model.FloorPlanRecyclerObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FloorPlanDetails extends AppCompatActivity {


    RelativeLayout page;
    FloorPlan floorPlan;
    TextView title, size, bedrooms, bathrooms, cars, owner;
    boolean favPresentCheck=false;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    ImageView imageView, addToFavourites;
    LinearLayout backButton, viewIn3D, compareBtn;

    String uid="";
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan_details);
        Intent intent= getIntent();
        floorPlan = intent.getParcelableExtra("FLOORPLAN");

        Log.i("FLOORPLANDETAILS",NetworkConfigurations.getIpAddress());

        mAuth = FirebaseAuth.getInstance();//getting authentication manager instance

        addToFavourites=findViewById(R.id.add_to_favourites_floorplan_detail);

        viewIn3D = findViewById(R.id.view_in_3d_floorplan_detail);
        compareBtn = findViewById(R.id.compare_floorplan_detail);

//        imageCounter= findViewById(R.id.imageCounter);
        title= findViewById(R.id.titleFloorPlanDetail);
        size= findViewById(R.id.sizeFloorPlanDetail);
        bedrooms= findViewById(R.id.bedroomsFloorPlanDetail);
        bathrooms= findViewById(R.id.bathroomsFloorPlanDetail);
        cars= findViewById(R.id.carCapacityFloorPlanDetail);
        owner= findViewById(R.id.nameOfOwnerFloorPlanDetail);
        progressBar= findViewById(R.id.progressBarAddToFavs);
        imageView = findViewById(R.id.imageViewFloorPlanDetail);
        backButton= findViewById(R.id.backButtonFloorPlanDetails);

        String floorPlanId=floorPlan.getId();
        if(mAuth.getCurrentUser()!=null){
            uid = mAuth.getCurrentUser().getUid();
        }


        title.setText(floorPlan.getTitle());
        size.setText(String.format("Size: %s", floorPlan.getSize()));
        bedrooms.setText(String.format("Bedrooms: %s", floorPlan.getBedrooms()));
        bathrooms.setText(String.format("Bathrooms: %s", floorPlan.getBathrooms()));
        cars.setText(String.format("Max Cars Capacity: %s", floorPlan.getNoOfCars()));
        owner.setText(String.format("by %s", floorPlan.getOwnerName()));

        Picasso.get().load(floorPlan.getImageUrl()).into(imageView);
//        to not show certain elements if the user is a guest
        if(floorPlan.getOwnerName().equals(NetworkConfigurations.getGuestUser())){
            owner.setText("");
            addToFavourites.setVisibility(View.GONE);
            compareBtn.setVisibility(View.GONE);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(floorPlan.getOwnerName().equals(NetworkConfigurations.getGuestUser())){
                    progressBar.setVisibility(View.VISIBLE);
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Floorplans");
                    database.child(floorPlan.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);

                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                finish();
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(FloorPlanDetails.this,"Could not remove temporary floorplan",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
                else{
                    finish();
                }

            }
        });

        //setting red heart if already a favourite
        populateFavPresentCheck();




        addToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(favPresentCheck){
                    //remove a favourite and remove fill from the color
                    progressBar.setVisibility(View.VISIBLE);

                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Favourites");
                    String key = database.push().getKey();
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                favPresentCheck = false;
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    Favourite tempFav = s.getValue(Favourite.class);
                                    if (tempFav.getFloorPlanId().equals(floorPlanId)
                                            && tempFav.getUserId().equals(uid)) {
                                        s.getRef().removeValue();
                                        addToFavourites.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });


                }
                else{
                    progressBar.setVisibility(View.VISIBLE);

                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Favourites");
                    String key = database.push().getKey();
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                favPresentCheck = false;
                                for(DataSnapshot s: snapshot.getChildren()){
                                    Favourite tempFav = s.getValue(Favourite.class);
                                    if(tempFav.getFloorPlanId().equals(floorPlanId)
                                            && tempFav.getUserId().equals(uid)){
                                        favPresentCheck = true;
                                    }
                                }
                                if(favPresentCheck){
                                    progressBar.setVisibility(View.GONE);
//                                    Toast.makeText(FloorPlanDetails.this,"Floorplan already added to favourites"
//                                            , Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Favourite newFavourite=new Favourite(uid,floorPlanId);
                                    database.child(key).setValue(newFavourite).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(FloorPlanDetails.this,"Floorplan added to favourites",Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            addToFavourites.setImageResource(R.drawable.ic_baseline_favorite_24);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(FloorPlanDetails.this,"Floorplan could no be added to favourites!",Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }
                            else{
                                Favourite newFavourite=new Favourite(uid,floorPlanId);
                                database.child(key).setValue(newFavourite).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(FloorPlanDetails.this,"Floorplan added to favourites",Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(FloorPlanDetails.this,"Floorplan could no be added to favourites!",Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
//                            progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }


            }
        });

        compareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Intent goToContractorsPage = new Intent(FloorPlanDetails.this, ContractorsPageCustomer.class);

                Intent goToContractorsPage = new Intent(FloorPlanDetails.this, ComparisonListPage.class);
                goToContractorsPage.putExtra("FLOORPLAN",floorPlan);
                startActivity(goToContractorsPage);


            }
        });

        viewIn3D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent unityIntent = new Intent(FloorPlanDetails.this, UnityActivity.class);
                unityIntent.putExtra("FLOORPLAN",floorPlan);
                unityIntent.putExtra("IP",NetworkConfigurations.getIpAddress());
                unityIntent.putExtra("PORT",NetworkConfigurations.getPortNumber());
                startActivity(unityIntent);

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(floorPlan.getOwnerName().equals(NetworkConfigurations.getGuestUser())){
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("Floorplans");
        database.child(floorPlan.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    FloorPlanDetails.super.onBackPressed();
                }
                else{
                    Toast.makeText(FloorPlanDetails.this,"Could not remove temporary floorplan",Toast.LENGTH_SHORT).show();
                    FloorPlanDetails.super.onBackPressed();
                }
            }
        });
        }
        else{
            super.onBackPressed();
        }
    }


    public void populateFavPresentCheck(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Favourites");
        String key = database.push().getKey();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for(DataSnapshot s: snapshot.getChildren()){
                        Favourite tempFav = s.getValue(Favourite.class);
                        if(tempFav.getFloorPlanId().equals(floorPlan.getId())
                                && tempFav.getUserId().equals(uid)){
                            favPresentCheck = true;
                            addToFavourites.setImageResource(R.drawable.ic_baseline_favorite_24);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
