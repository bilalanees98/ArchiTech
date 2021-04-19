package com.architech.architech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IPActivity extends AppCompatActivity {

    EditText ipAddress, portNo;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_p);

        ipAddress = findViewById(R.id.ipAddress_ipActivity);
        portNo = findViewById(R.id.portNo_ipActivity);
        submit = findViewById(R.id.submitBtn_ipActivity);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkConfigurations.setIpAddress(ipAddress.getText().toString().trim());
                NetworkConfigurations.setPortNumber(portNo.getText().toString().trim());
                Log.i("IP_ACTIVITY",NetworkConfigurations.getIpAddress());
                Log.i("IP_ACTIVITY",NetworkConfigurations.getPortNumber());
                Intent i = new Intent(IPActivity.this,SplashScreen.class);
                startActivity(i);
                finish();
            }
        });

    }
}