package com.architech.architech.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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
    Button addToFavourites,findContractor,viewIn3D, compareBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    ImageView imageView;

    String uid="";
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan_details);
        Intent intent= getIntent();
        floorPlan = intent.getParcelableExtra("FLOORPLAN");

        mAuth = FirebaseAuth.getInstance();//getting authentication manager instance

        addToFavourites=findViewById(R.id.add_to_favourites_floorplan_detail);
        findContractor=findViewById(R.id.find_contractor_floorplan_detail);
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
        if(floorPlan.getOwnerName().equals(SplashScreen.guestUser)){
            owner.setText("");
            addToFavourites.setVisibility(View.GONE);
            compareBtn.setVisibility(View.GONE);
        }

        System.out.print("hey");

        addToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Favourites");
                String key = database.push().getKey();
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Boolean favPresentCheck = false;
                            for(DataSnapshot s: snapshot.getChildren()){
                                Favourite tempFav = s.getValue(Favourite.class);
                                if(tempFav.getFloorPlanId().equals(floorPlanId)
                                && tempFav.getUserId().equals(uid)){
                                    favPresentCheck = true;
                                }
                            }
                            if(favPresentCheck){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(FloorPlanDetails.this,"Floorplan already added to favourites"
                                , Toast.LENGTH_SHORT).show();
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
        progressBar.setVisibility(View.VISIBLE);
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Floorplans");
//        database.child(floorPlan.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                progressBar.setVisibility(View.GONE);
//                FloorPlanDetails.super.onBackPressed();
////                if(task.isSuccessful()){
////                    progressBar.setVisibility(View.GONE);
////                    FloorPlanDetails.super.onBackPressed();
////                }
////                else{
////
////                }
//            }
//        });

//        super.onBackPressed();
    }
}
