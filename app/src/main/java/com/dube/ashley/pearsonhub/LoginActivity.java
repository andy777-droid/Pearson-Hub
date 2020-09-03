package com.dube.ashley.pearsonhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    EditText emailId, passwordTXT;
    Button btnSignIn;
    TextView SignUp, resetPsw;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onStart()
    {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

       }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId=findViewById(R.id.editTextTextEmailAddress);
        passwordTXT=findViewById(R.id.editTextTextPassword);

        SignUp=findViewById(R.id.signUpTV);
        SpannableString content1 = new SpannableString("Create Account");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        SignUp.setText(content1);

        resetPsw=findViewById(R.id.linkForgot);
        SpannableString content2 = new SpannableString("Forgot Password?");
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        resetPsw.setText(content2);

        btnSignIn=findViewById(R.id.signInBTN);

        mAuthStateListener= new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser mFirebaseUser =mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser != null && mFirebaseUser.isEmailVerified())
                {
                    Toast.makeText(LoginActivity.this,"You're logged in",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(i);
                }

            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email=emailId.getText().toString();
                String passwrd=passwordTXT.getText().toString();

                if(email.isEmpty())
                {
                    emailId.setError("Please enter your email address");
                    emailId.requestFocus();
                }
                else if (passwrd.isEmpty())
                {
                    passwordTXT.setError("Please enter your password");
                    passwordTXT.requestFocus();
                }
                else if(email.isEmpty() && passwrd.isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"Fields are empty",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && passwrd.isEmpty()))
                {
                    mFirebaseAuth.signInWithEmailAndPassword(email,passwrd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this,"Login Error, please attempt Login again",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if(mFirebaseAuth.getCurrentUser().isEmailVerified())
                                {
                                    Intent intentHome=new Intent(LoginActivity.this,HomeActivity.class);
                                    startActivity(intentHome);
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this,"Please verify your email address",Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();
                }

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intentSignUp=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intentSignUp);
            }
        });

        resetPsw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intentReset=new Intent(LoginActivity.this,ResetPassword.class);
                startActivity(intentReset);
            }
        });

    }
}