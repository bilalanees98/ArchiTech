package com.architech.architech;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.DefaultCompany.MeshTutorial.UnityPlayerActivity;
import com.architech.architech.customer.LoginCustomer;
import com.architech.architech.model.FloorPlan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.unity3d.player.UnityPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class UnityActivity extends UnityPlayerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unity);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        FloorPlan floorPlan = getIntent().getParcelableExtra("FLOORPLAN");
        mUnityPlayer = new UnityPlayer(this );
        Log.i("UNIty",floorPlan.getCroppedWidth());
        Log.i("UNIty",floorPlan.getCroppedLength());
        UnityPlayer.UnitySendMessage("Floor","setFloorplanName",floorPlan.getId());
        UnityPlayer.UnitySendMessage("Floor","setFloorplanWidth",floorPlan.getCroppedWidth());
        UnityPlayer.UnitySendMessage("Floor","setFloorplanLength",floorPlan.getCroppedLength());

        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();


    }

    @Override
    public void onBackPressed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UnityActivity.this,
                        "on Back Pressed", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        finish();

    }

    @Override
    public void onDestroy() {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }

}