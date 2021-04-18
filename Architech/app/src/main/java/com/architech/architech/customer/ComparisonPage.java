package com.architech.architech.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.architech.architech.R;
import com.architech.architech.model.FloorPlan;
import com.squareup.picasso.Picasso;

public class ComparisonPage extends AppCompatActivity {

    FloorPlan floorPlanOne, floorPlanTwo;
    ImageView imageFP1, imageFP2;
    TextView titleFP1, titleFP2;
    TextView ownerFP1, ownerFP2;
    TextView sizeFP1, sizeFP2;
    TextView bedroomFP1, bedroomFP2;
    TextView bathroomFP1, bathroomFP2;
    TextView costFP1, costFP2;

    Button swap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison_page);

        Intent intent= getIntent();
        floorPlanOne=intent.getParcelableExtra("FLOORPLANONE");
        floorPlanTwo=intent.getParcelableExtra("FLOORPLANTWO");

        //SETTING UI ELEMENT IDs
        imageFP1= findViewById(R.id.comparisonOneImage);
        titleFP1= findViewById(R.id.comparisonOneTitle);
        ownerFP1= findViewById(R.id.comparisonOneOwnerName);
        sizeFP1= findViewById(R.id.comparisonOneSize);
        bedroomFP1= findViewById(R.id.comparisonOneBedrooms);
        bathroomFP1= findViewById(R.id.comparisonOneBathrooms);
        costFP1= findViewById(R.id.comparisonOneCostEstimates);

        imageFP2= findViewById(R.id.comparisonTwoImage);
        titleFP2= findViewById(R.id.comparisonTwoTitle);
        ownerFP2= findViewById(R.id.comparisonTwoOwnerName);
        sizeFP2= findViewById(R.id.comparisonTwoSize);
        bedroomFP2= findViewById(R.id.comparisonTwoBedrooms);
        bathroomFP2= findViewById(R.id.comparisonTwoBathrooms);
        costFP2= findViewById(R.id.comparisonTwoCostEstimates);

        swap= findViewById(R.id.swapButtonComparison);

        //SETTING VALUES
        Picasso.get()
                .load(floorPlanOne.getImageUrl()).fit().centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(imageFP1);
        titleFP1.setText(floorPlanOne.getTitle());
        ownerFP1.setText(floorPlanOne.getOwnerName());
        sizeFP1.setText(floorPlanOne.getSize());
        bedroomFP1.setText(floorPlanOne.getBedrooms());
        bathroomFP1.setText(floorPlanOne.getBathrooms());
        costFP1.setText(floorPlanOne.getCostEstimate());

        Picasso.get()
                .load(floorPlanTwo.getImageUrl()).fit().centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(imageFP2);
        titleFP2.setText(floorPlanTwo.getTitle());
        ownerFP2.setText(floorPlanTwo.getOwnerName());
        sizeFP2.setText(floorPlanTwo.getSize());
        bedroomFP2.setText(floorPlanTwo.getBedrooms());
        bathroomFP2.setText(floorPlanTwo.getBathrooms());
        costFP2.setText(floorPlanTwo.getCostEstimate());



        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ComparisonPage.this, "Pick another floor plan", Toast.LENGTH_SHORT).show();
                Intent goToComparisonList= new Intent(ComparisonPage.this, ComparisonListPage.class);
                goToComparisonList.putExtra("FLOORPLAN", floorPlanOne);
                startActivity(goToComparisonList);

            }
        });





    }
}