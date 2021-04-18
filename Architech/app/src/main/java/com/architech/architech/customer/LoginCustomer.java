package com.architech.architech.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.architech.architech.SplashScreen;
import com.architech.architech.UnityActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.architech.architech.Notifier;
import com.architech.architech.R;
import com.architech.architech.contractor.LoginContractor;
import com.architech.architech.contractor.SignupContractor;
import com.architech.architech.model.Customer;
import com.architech.architech.model.Meeting;
import com.unity3d.player.UnityPlayer;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginCustomer extends AppCompatActivity {

    TextView forgotPassword;
    TextView signUpText, skipBtn;
    Button loginButton;
    Button unityBtn;
    EditText email,password;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference database;
    private FirebaseAuth.AuthStateListener authStateListener;
    ArrayList<Customer> listOfCustomers;
    boolean dataFetched;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer);

        forgotPassword=findViewById(R.id.forgotPasswordText);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty()){
                    email.setError("Enter email to reset password");
                    email.requestFocus();
                }
                else{
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("FORGOTDEBUG", "Email sent.");
                                    }
                                }
                            });
                }
            }
        });


        unityBtn = findViewById(R.id.unityActivityBtn);
        unityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginCustomer.this, UnityActivity.class);
                startActivity(intent);
            }
        });

        signUpText = findViewById(R.id.signupTextOnLoginCustomer);
        skipBtn = findViewById(R.id.skip_button_login);
        loginButton= findViewById(R.id.loginButtonCustomer);
        progressBar= findViewById(R.id.progressBarLoginCustomer);
        email=findViewById(R.id.email_input_customer_login);
        password=findViewById(R.id.password_input_customer_login);
        firebaseAuth=FirebaseAuth.getInstance();

        listOfCustomers= new ArrayList<>();
        authStateListener=new FirebaseAuth.AuthStateListener(){
            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();



            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseUser!=null){
                    //Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
//                    Intent i=new Intent(LoginActivity.this,ContactListActivity.class);
//                    startActivity(i);
                }
                else{
//                Toast.makeText(Login.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };


        dataFetched= true;
        database = FirebaseDatabase.getInstance().getReference().child("Users");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        Customer newCustomer= s.getValue(Customer.class);
                        listOfCustomers.add(newCustomer);
                    }

                }
                else
                {
                    dataFetched=false;
                    Toast.makeText(LoginCustomer.this, "Data could not be fetched, try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        //When Login Button is Pressed by Customer
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email1=email.getText().toString().trim();
                final String password1=password.getText().toString().trim();
                //email is empty
                if(email1.isEmpty()){
                    email.setError("Provide Email");
                    email.requestFocus();
                }
                //password is empty
                else if(password1.isEmpty()){
                    password.setError("Provide Password");
                    password.requestFocus();
                }
                //Both email and password are empty
                else if(!email1.isEmpty() && !password1.isEmpty()){

                    progressBar.setVisibility(View.VISIBLE);
                    boolean customerExists= false;
                    //checking if email exists in users table, only then signing in
                    for(int i=0; i<listOfCustomers.size();i++)
                    {
                        if(listOfCustomers.get(i).getEmail().equals(email1)){
                            customerExists= true;
                            break;
                        }
                    }

                    if(customerExists)
                    {
                        signIn(email1, password1);
                    }
                    else
                    {
                        if(dataFetched) {
                            Toast.makeText(LoginCustomer.this, "Incorrect email", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(LoginCustomer.this, "Data could not be fetched, try again later", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                }
                else{
                    Toast.makeText(LoginCustomer.this,"Error occured",Toast.LENGTH_SHORT).show();
                }

            }
        });




//Directing to login page from signup page
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginCustomer.this, SignupCustomer.class);
                startActivity(intent);
            }
        });

//        for one time use only, not using finish() as guest will be returned to this screen when he presses back
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginCustomer.this,AddFloorPlan.class);
                Customer guest = new Customer(SplashScreen.guestUser,SplashScreen.guestUser,SplashScreen.guestUser,SplashScreen.guestUser);
                Log.i("LOGIN_CUSTOMER",SplashScreen.guestUser);
                Log.i("LOGIN_CUSTOMER",guest.getName());
                intent.putExtra("MYPROFILE",guest);
                startActivity(intent);
            }
        });

    }

    void signIn(String email1, String password1)
    {
        hideKeyboard(LoginCustomer.this);
        firebaseAuth.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(LoginCustomer.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    e.printStackTrace();
                    System.out.println(email1 + "---------" + password1);
                    Toast.makeText(LoginCustomer.this,"Login Error",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
                else{


                    progressBar.setVisibility(View.GONE);
                    Intent toHome=new Intent(LoginCustomer.this,MainPageCustomer.class);


                    toHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(toHome);
                    finish();
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
