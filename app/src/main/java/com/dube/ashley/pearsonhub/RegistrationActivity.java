package com.dube.ashley.pearsonhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity
{
    EditText emailId, passwordTXT;
    Button btnSignUp;
    TextView SignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextTextEmailAddress);
        passwordTXT = findViewById(R.id.editTextTextPassword);
        SignIn = findViewById(R.id.signUpTV);
        btnSignUp = findViewById(R.id.signUpBTN);
        btnSignUp.setOnClickListener
        (
            new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String email = emailId.getText().toString();
                    String passwrd = passwordTXT.getText().toString();
                    if (email.isEmpty()) {
                        emailId.setError("Please enter your email address");
                        emailId.requestFocus();
                    } else if (passwrd.isEmpty()) {
                        passwordTXT.setError("Please enter your email address");
                        passwordTXT.requestFocus();
                    } else if (email.isEmpty() && passwrd.isEmpty()) {
                        Toast.makeText(RegistrationActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                    } else if (!(email.isEmpty() && passwrd.isEmpty())) {
                        mFirebaseAuth.createUserWithEmailAndPassword(email, passwrd).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegistrationActivity.this, "SignUp unsuccessful", Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}