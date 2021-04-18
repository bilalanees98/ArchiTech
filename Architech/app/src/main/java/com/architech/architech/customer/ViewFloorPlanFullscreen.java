package com.architech.architech.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.architech.architech.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;


//USELESS AS OF NOW
public class ViewFloorPlanFullscreen extends AppCompatActivity {

    ArrayList<String> imageUrls;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_floor_plan_fullscreen);
        imageUrls=new ArrayList<>();
        imageUrls= getIntent().getStringArrayListExtra("URLS");

        Picasso.get().load(imageUrls.get(0)).into(img);

    }
}