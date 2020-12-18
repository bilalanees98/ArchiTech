package com.hyderabbasnaqvi.architech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ModelDisplay extends AppCompatActivity {
    ImageView predictionImg;
    Button logOut;
    FirebaseAuth mFirebaseAuth;
    ImageView info;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_display);
        logOut=findViewById(R.id.logout);

        predictionImg = findViewById(R.id.predictionImg);
        String imgString = getIntent().getStringExtra("PRED_IMAGE");
        final ArrayList<String> roomCounts = getIntent().getStringArrayListExtra("ROOM_COUNT");
        final ArrayList<String> roomTypes = getIntent().getStringArrayListExtra("ROOM_TYPES");
        final String costEstimate = getIntent().getStringExtra("COST_ESTIMATE");
        final String pca = getIntent().getStringExtra("PCA");

        Log.i("INFO_INTENT",roomCounts.toString());
        Log.i("INFO_INTENT",roomTypes.toString());
        Log.i("INFO_INTENT",pca);
        Log.i("INFO_INTENT",costEstimate);

        byte[] decodedString = Base64.decode(imgString, Base64.DEFAULT);
        Bitmap decodedBytes = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        predictionImg.setImageBitmap(decodedBytes);
        info=findViewById(R.id.infoButton);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoBottomSheetDialogue infoBottomSheetDialogue= new InfoBottomSheetDialogue(pca,roomCounts, roomTypes,costEstimate);
                infoBottomSheetDialogue.show(getSupportFragmentManager(), "bottomSheetInfo");
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent back=new Intent(ModelDisplay.this,Login.class);
                startActivity(back);

            }
        });
    }
}