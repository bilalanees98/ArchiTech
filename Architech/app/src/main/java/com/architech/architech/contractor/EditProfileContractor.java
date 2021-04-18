package com.architech.architech.contractor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.architech.architech.Notifier;
import com.architech.architech.R;
import com.architech.architech.model.Contractor;
import com.architech.architech.model.Customer;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileContractor extends AppCompatActivity {

    ProgressBar progressBar;
    EditText name, phone, rateOne, rateTwo, rateThree;
    Button saveButton;
    CircleImageView imageIcon;
    FirebaseAuth mAuth;
    Contractor myProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_contractor);
        myProfile= getIntent().getParcelableExtra("MYPROFILE");


        mAuth=FirebaseAuth.getInstance();


        name= findViewById(R.id.nameEditProfileContractor);
        phone= findViewById(R.id.phoneEditProfileContractor);
        rateOne= findViewById(R.id.rateOneEditProfileContractor);
        rateTwo= findViewById(R.id.rateTwoEditProfileContractor);
        rateThree= findViewById(R.id.rateThreeEditProfileContractor);
        saveButton= findViewById(R.id.saveButtonEditProfileContractor);
        imageIcon= findViewById(R.id.imageEditProfileContractor);
        progressBar= findViewById(R.id.progressBarEditContractor);

        name.setText(myProfile.getName());
        phone.setText(myProfile.getPhone());
        rateOne.setText(myProfile.getRateOne());
        rateTwo.setText(myProfile.getRateTwo());
        rateThree.setText(myProfile.getRateThree());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final String nameString= name.getText().toString().trim();
                final String phoneString= phone.getText().toString().trim();
                final String rateOneString= rateOne.getText().toString().trim();
                final String rateTwoString= rateTwo.getText().toString().trim();
                final String rateThreeString= rateThree.getText().toString().trim();


                //email is empty
                if(nameString.isEmpty()){
                    name.setError("Provide Email");
                    name.requestFocus();
                }
                else if(phoneString.isEmpty()){
                    phone.setError("Provide Phone");
                    phone.requestFocus();
                }
                else if(rateOneString.isEmpty()){
                    rateOne.setError("Provide Rate for Highest Quality Construction");
                    rateOne.requestFocus();
                }
                else if(rateTwoString.isEmpty()){
                    rateTwo.setError("Provide Rate for Medium Quality Construction");
                    rateTwo.requestFocus();
                }
                else if(rateThreeString.isEmpty()){
                    rateThree.setError("Provide Rate for Lowest Quality Construction");
                    rateThree.requestFocus();
                }

                else {
                    editProfile();
                }


            }
        });
    }

    private void editProfile() {

        hideKeyboard(EditProfileContractor.this);
        progressBar.setVisibility(View.VISIBLE);
        String uid=mAuth.getCurrentUser().getUid();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("name",name.getText().toString().trim());
        hashMap.put("phone",phone.getText().toString().trim());
        hashMap.put("rateOne",rateOne.getText().toString().trim());
        hashMap.put("rateTwo",rateTwo.getText().toString().trim());
        hashMap.put("rateThree",rateThree.getText().toString().trim());
        FirebaseDatabase.getInstance().getReference().child("Contractors")
                .child(uid)
                .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditProfileContractor.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    Intent resultIntent= new Intent();
                    resultIntent.putExtra("MY_PROFILE_FROM_EDIT",
                            new Contractor(name.getText().toString().trim(),
                                    phone.getText().toString().trim(),
                                    myProfile.getEmail(),
                                    myProfile.getUid(),
                                    rateOne.getText().toString().trim(),
                                    rateTwo.getText().toString().trim(),
                                    rateThree.getText().toString().trim()));

                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
                else{
                    Toast.makeText(EditProfileContractor.this, "could not update profile", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }

    void hideKeyboard(Context c)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(c);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}