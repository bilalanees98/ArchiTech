package com.architech.architech.contractor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
//import com.onesignal.OneSignal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.architech.architech.R;
import com.architech.architech.customer.LoginCustomer;
import com.architech.architech.customer.MainPageCustomer;
import com.architech.architech.model.Contractor;
import com.architech.architech.model.Customer;

import org.json.JSONObject;

import java.util.ArrayList;

public class LoginContractor extends AppCompatActivity {

    TextView signupText;
    Button loginButton;
    EditText email,password;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    ArrayList<Contractor> listOfContractors;
    DatabaseReference database;
    boolean dataFetched;
    private FirebaseAuth.AuthStateListener authStateListener;

    private static final String ONESIGNAL_APP_ID = "917cc082-1cf6-4503-96dc-992c8793aba6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_contractor);


        progressBar= findViewById(R.id.progressBarLoginContractor);
        signupText= findViewById(R.id.signupTextOnLoginContractor);
        loginButton= findViewById(R.id.loginContractorButton);
        listOfContractors= new ArrayList<>();


        email=findViewById(R.id.email_input_contractor_login);
        password=findViewById(R.id.password_input_contractor_login);
        firebaseAuth= FirebaseAuth.getInstance();
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

        //for checking if this login is from customer or contractor
        dataFetched=true;
        database = FirebaseDatabase.getInstance().getReference().child("Contractors");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        Contractor newContractor= s.getValue(Contractor.class);
                        listOfContractors.add(newContractor);
                    }
                }
                else{
                    dataFetched=false;
                    Toast.makeText(LoginContractor.this, "Data could not be fetched, try again later", Toast.LENGTH_SHORT).show();
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
                    boolean contractorExists= false;
                    //checking if email exists in users table, only then signing in
                    for(int i=0; i<listOfContractors.size();i++)
                    {
                        if(listOfContractors.get(i).getEmail().equals(email1)){
                            contractorExists= true;
                            break;
                        }
                    }

                    if(contractorExists)
                    {
                        signIn(email1, password1);
                    }
                    else
                    {
                        if(dataFetched) {
                            Toast.makeText(LoginContractor.this, "Incorrect email", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(LoginContractor.this, "Data could not be fetched, try again later", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }



                }
                else{
                    Toast.makeText(LoginContractor.this,"Error occured",Toast.LENGTH_SHORT).show();
                }

            }
        });


        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginContractor.this, SignupContractor.class);
                startActivity(intent);
            }
        });


    }


    void signIn(String email1, String password1)
    {
        hideKeyboard(LoginContractor.this);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(LoginContractor.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    e.printStackTrace();
                    System.out.println(email1 + "---------" + password1);
                    Toast.makeText(LoginContractor.this,"Login Error",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Intent toHome=new Intent(LoginContractor.this, MainPageContractor.class);
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
