package com.architech.architech.contractor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.architech.architech.R;
import com.architech.architech.customer.MainPageCustomer;
import com.architech.architech.model.Contractor;
import com.architech.architech.model.Customer;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupContractor extends AppCompatActivity {

    TextView loginText;
    Button signupButton;
    EditText name, phone, email, password, rateOne, rateTwo, rateThree;
    CircleImageView imageIcon;
    ProgressBar progressBar;


    private FirebaseAuth mAuth;


    Uri imageUri;//for storing selected profile image
    String downloadUrl;//for storing download url of uploaded image - to store in Customer class
    int GALLERY_PICK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_contractor);

        progressBar= findViewById(R.id.progressBarSignupContractor);
        loginText= findViewById(R.id.loginTextOnSignupContractor);
        signupButton= findViewById(R.id.signupContractorButton);

        //Connecting Edit texts
        name=findViewById(R.id.nameContractorSignup);
        phone=findViewById(R.id.phoneContractorSignup);

        rateOne= findViewById(R.id.rateOneContractorSignup);
        rateTwo= findViewById(R.id.rateTwoContractorSignup);
        rateThree= findViewById(R.id.rateThreeContractorSignup);

        email=findViewById(R.id.emailContractorSignup);
        password= findViewById(R.id.passwordContractorSignup);


        imageIcon= findViewById(R.id.imageSignupContractor);
        //Getting instance of Firebase here
        mAuth = FirebaseAuth.getInstance();



        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nameString= name.getText().toString().trim();
                final String emailString=email.getText().toString().trim();
                final String passwordString=password.getText().toString().trim();
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

                else if(emailString.isEmpty()){
                    email.setError("Provide Email");
                    email.requestFocus();
                }
                //password is empty
                else if(passwordString.isEmpty()){
                    password.setError("Provide Password");
                    password.requestFocus();
                }
                else {
                    signUp();
                }




            }
        });


        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupContractor.this, LoginContractor.class);
                startActivity(intent);
            }
        });



        imageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent = new Intent();
                galleryintent.setType("image/*");
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryintent, "Select Image"),GALLERY_PICK);
            }
        });
    }

    void signUp(){
        hideKeyboard(SignupContractor.this);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String uid=mAuth.getCurrentUser().getUid();
                        Contractor newContractor=new Contractor(
                                name.getText().toString().trim(),
                                phone.getText().toString().trim(),
                                email.getText().toString().trim(),
                                uid,
                                rateOne.getText().toString().trim(),
                                rateTwo.getText().toString().trim(),
                                rateThree.getText().toString().trim()
                        );

                        //TODO: image stuff
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                                .child("Contractors")
                                .child(uid);
                        database.setValue(newContractor).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBar.setVisibility(View.GONE);
                                Intent intent=new Intent(SignupContractor.this, MainPageContractor.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }
                        });

                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignupContractor.this,"Could not signup",Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            assert data != null;
            imageUri = data.getData();//for storing in firebase
            imageIcon.setImageURI(imageUri);

        }
    }


}
