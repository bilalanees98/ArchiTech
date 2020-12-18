 package com.hyderabbasnaqvi.architech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

 public class Login extends AppCompatActivity {
     EditText email,password;
     Button loginButton;
     FirebaseAuth firebaseAuth;
     TextView signUpText;
     ProgressBar progressBar;
//     private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.emailLogin);
        password=findViewById(R.id.passwordLogin);
        loginButton=findViewById(R.id.loginButton);
        signUpText=findViewById(R.id.signUpTextOnLogin);
        progressBar = findViewById(R.id.progressBar2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email1=email.getText().toString().trim();
                final String password1=password.getText().toString().trim();
                if(email1.isEmpty()){
                    email.setError("Provide email id");
                    email.requestFocus();
                }

                else if(password1.isEmpty()){
                    password.setError("Provide password");
                    password.requestFocus();
                }

                else if(!email1.isEmpty() && !password1.isEmpty()){
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                e.printStackTrace();
                                System.out.println(email1 + "---------" + password1);
                                Toast.makeText(Login.this,"Login error",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent toHome=new Intent(Login.this,ImageUpload.class);
                                startActivity(toHome);
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this,"Error occured",Toast.LENGTH_SHORT).show();
                }

            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinbut=new Intent(Login.this,Register.class);
                startActivity(signinbut);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
//        firebaseAuth.addAuthStateListener(authStateListener);
    }
}