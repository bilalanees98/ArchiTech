package com.hyderabbasnaqvi.architech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

public class Register extends AppCompatActivity {
    EditText email,password;
    TextView loginText;
    Button register;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.emailRegister);
        password=findViewById(R.id.passwordRegister);
        register=findViewById(R.id.registerButton);
        loginText=findViewById(R.id.loginTextOnSignup);
        progressBar=findViewById(R.id.progressBar1);

        register.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
//              trimming whitespaces before and after
                String email1=email.getText().toString().trim();
                String password1=password.getText().toString().trim();
//                UI checks
                if(email1.isEmpty()){
                    email.setError("Provide email id");
                    email.requestFocus();
                }

                else if(password1.isEmpty()){
                    password.setError("Provide email id");
                    password.requestFocus();
                }
//              if email and password provided
                else if(!email1.isEmpty() && !password1.isEmpty()){
                    firebaseAuth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                e.printStackTrace();
                                Toast.makeText(Register.this,"Error occured in registration",Toast.LENGTH_SHORT).show();
                            }
                            else{
//                                if successful move to imageUpload screen
                                startActivity(new Intent(Register.this,ImageUpload.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Register.this,"Error occured",Toast.LENGTH_SHORT).show();
                }

            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                open up login screen
                Intent signinbut=new Intent(Register.this,Login.class);
                startActivity(signinbut);
            }
        });
    }
}