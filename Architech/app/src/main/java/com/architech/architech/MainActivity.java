package com.architech.architech;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.architech.architech.contractor.LoginContractor;
import com.architech.architech.customer.LoginCustomer;

public class MainActivity extends AppCompatActivity {

    Button customerButton, contractorButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customerButton= findViewById(R.id.customerButton);
        contractorButton= findViewById(R.id.contractorButton);


        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(com.architech.architech.MainActivity.this, LoginCustomer.class);
                startActivity(intent);
            }
        });


        contractorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(com.architech.architech.MainActivity.this, LoginContractor.class);
                startActivity(intent);
            }
        });



    }


}
