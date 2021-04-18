package com.architech.architech.customer;

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
import com.onesignal.OneSignal;
import com.architech.architech.R;
import com.architech.architech.model.Customer;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupCustomer extends AppCompatActivity {
//    private static final String ONESIGNAL_APP_ID = "bbcc3a17-6447-447d-8718-0a485dd91063";
    TextView loginText;
    Button signupButton;
    EditText name_input,email_input,password_input,phone_input;
    CircleImageView imageIcon;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    Uri imageUri;//for storing selected profile image
    String downloadUrl;//for storing download url of uploaded image - to store in Customer class
    int GALLERY_PICK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_customer);

        loginText= findViewById(R.id.loginTextOnSignupCustomer);
        signupButton=findViewById(R.id.signupCustomerButton);

        name_input=findViewById(R.id.name_input_customer_signup);
        email_input=findViewById(R.id.email_input_customer_signup);
        password_input=findViewById(R.id.password_input_customer_signup);
        phone_input=findViewById(R.id.phone_input_customer_signup);
        imageIcon= findViewById(R.id.imageSignupCustomer);
        progressBar= findViewById(R.id.progressBarSignupCustomer);

        //Getting instance of Firebase here
        mAuth = FirebaseAuth.getInstance();


        //going to login page
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SignupCustomer.this, LoginCustomer.class);
                startActivity(intent);
            }
        });


        //Signup button clicked
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name= name_input.getText().toString().trim();
                final String email=email_input.getText().toString().trim();
                final String password=password_input.getText().toString().trim();
                final String phone= phone_input.getText().toString().trim();
                //email is empty
                if(name.isEmpty()){
                    name_input.setError("Provide Email");
                    name_input.requestFocus();
                }
                else if(email.isEmpty()){
                    email_input.setError("Provide Email");
                    email_input.requestFocus();
                }
                else if(phone.isEmpty()){
                    phone_input.setError("Provide Phone");
                    phone_input.requestFocus();
                }
                //password is empty
                else if(password.isEmpty()){
                    password_input.setError("Provide Password");
                    password_input.requestFocus();
                }
                else {
                    signUp();
                }
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


    void signUp()
    {
        hideKeyboard(SignupCustomer.this);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email_input.getText().toString(),password_input.getText().toString())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String uid=mAuth.getCurrentUser().getUid();
                        Customer newCustomer=new Customer(
                                name_input.getText().toString(),
                                phone_input.getText().toString(),
                                email_input.getText().toString(),
                                uid

                        );
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                                .child("Users")
                                .child(uid);
                        database.setValue(newCustomer).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                OneSignal.setAppId(ONESIGNAL_APP_ID);
//                                OneSignal.sendTag("User_id",email_input.getText().toString());
                                progressBar.setVisibility(View.VISIBLE);
                                Intent toHome=new Intent(SignupCustomer.this,MainPageCustomer.class);
                                toHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(toHome);
                                finish();
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupCustomer.this,"Could not sign up",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
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
