package com.architech.architech.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.architech.architech.R;
import com.architech.architech.model.Contractor;
import com.architech.architech.model.FloorPlan;
import com.architech.architech.model.FloorPlanRecyclerObject;
import com.architech.architech.model.Image;

import java.util.ArrayList;

public class ContractorsPageCustomer extends AppCompatActivity {

    ArrayList<Contractor> listOfContractors;
    FloorPlanRecyclerObject floorPlan;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractors_page_customer);

        floorPlan=getIntent().getParcelableExtra("FLOORPLAN");
        listOfContractors=new ArrayList<>();

        RecyclerView rv=findViewById(R.id.recyclerViewContractorPageCustomer);
        RecyclerView.LayoutManager lm= new LinearLayoutManager(ContractorsPageCustomer.this);
        rv.setLayoutManager(lm);
        ContractorsPageCustomerRvAdapter adapter=new ContractorsPageCustomerRvAdapter(listOfContractors,floorPlan, ContractorsPageCustomer.this);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());


        database = FirebaseDatabase.getInstance().getReference().child("Contractors");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for(DataSnapshot s: snapshot.getChildren()){
                        listOfContractors.add(s.getValue(Contractor.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}