package com.dube.ashley.pearsonhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity
{
    private EditText etfirstName, etlastName, eteditTextTextEmailAddress, eteditTextTextPassword, eteditTextTextPassword2, etcellNumber;
    private String ETfirstName, ETlastName, ETeditTextTextEmailAddress, ETeditTextTextPassword, ETeditTextTextPassword2, ETcellNumber;
    Button laysignUpBTN;
    private FirebaseAuth mAuth;
    DatabaseReference reff;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth=FirebaseAuth.getInstance();
        etfirstName= (EditText) findViewById(R.id.firstName);
        etlastName= (EditText) findViewById(R.id.lastName);
        eteditTextTextEmailAddress= (EditText) findViewById(R.id.editTextTextEmailAddress);
        eteditTextTextPassword= (EditText) findViewById(R.id.editTextTextPassword);
        eteditTextTextPassword2= (EditText) findViewById(R.id.editTextTextPassword2);
        etcellNumber= (EditText) findViewById(R.id.cellNumber);
        laysignUpBTN= (Button) findViewById(R.id.signUpBTN);
        user = new User();

        laysignUpBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerNewUser();//Method to register new user
            }
        });
    }

    public void registerNewUser()
    {
        initialize();//initialize the input to string variables

        if(!validate())
        {
            Toast.makeText(this,"Signup has failed", Toast.LENGTH_LONG).show();
        }
        else
        {
            onSignupSuccess();
        }

    }

    public void onSignupSuccess()
    {
        mAuth.createUserWithEmailAndPassword(ETeditTextTextEmailAddress,ETeditTextTextPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                   mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
                   {
                       @Override
                       public void onComplete(@NonNull Task<Void> tasks)
                       {

                           user = new User(ETfirstName, ETlastName, ETeditTextTextEmailAddress, ETcellNumber);
                           reff = FirebaseDatabase.getInstance().getReference().child("Users");
                           reff.push().setValue(user);

                           if(tasks.isSuccessful())
                           {
                               Toast.makeText(SignUpActivity.this,"Registration Successful, please click the link that has been emailed to you",Toast.LENGTH_LONG).show();
                               setContentView(R.layout.activity_login);
                           }
                           else
                           {
                               Toast.makeText(SignUpActivity.this,tasks.getException().getMessage(),Toast.LENGTH_LONG).show();
                           }

                       }
                   });

                }
                else
                {
                    Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean validate()
    {
        boolean valid=true;

        if (ETfirstName.isEmpty()||ETfirstName.length()>32)
        {
            etfirstName.setError("Enter a valid name!");
            valid=false;
        }
        if (ETlastName.isEmpty()||ETlastName.length()>32)
        {
            etlastName.setError("Enter a valid surname!");
            valid=false;
        }
        if (ETeditTextTextEmailAddress.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(ETeditTextTextEmailAddress).matches())
        {
            eteditTextTextEmailAddress.setError("Enter a valid email");
            valid=false;
        }
        if (ETeditTextTextPassword.isEmpty())
        {
            eteditTextTextPassword.setError("Enter a password!");
            valid=false;
        }
        if (ETeditTextTextPassword2.isEmpty())
        {
            eteditTextTextPassword2.setError("Enter a password!");
            valid=false;
        }
        if (ETcellNumber.isEmpty())
        {
            etcellNumber.setError("Enter a password!");
            valid=false;
        }

        return  valid;
    }

    public void initialize()
    {
        ETfirstName=etfirstName.getText().toString().trim();
        ETlastName=etlastName.getText().toString().trim();
        ETeditTextTextEmailAddress=eteditTextTextEmailAddress.getText().toString().trim();
        ETeditTextTextPassword=eteditTextTextPassword.getText().toString().trim();
        ETeditTextTextPassword2=eteditTextTextPassword2.getText().toString().trim();
        ETcellNumber=etcellNumber.getText().toString().trim();
    }
}