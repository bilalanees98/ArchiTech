package com.architech.architech;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Checking extends AppCompatActivity {

    ImageView imv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);

        imv= findViewById(R.id.checkingImageView);

        int image1res= getResources().getIdentifier("@drawable/traditional_house_design_250x250", null, this.getPackageName());
        imv.setImageResource(image1res);


    }
}
