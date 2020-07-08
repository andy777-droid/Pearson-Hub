package com.dube.ashley.pearsonhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    Button resetPasswordButton;
    EditText email;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPasswordButton=findViewById(R.id.btnSend);
        email=findViewById(R.id.edtEmail);
        mAuth=FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();

                if (TextUtils.isEmpty(userEmail)){
                    Toast.makeText(ResetPassword.this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(ResetPassword.this, "Check Your Email.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPassword.this, LoginActivity.class));
                            }
                            else
                            {
                                String msg = task.getException().getMessage();
                                Toast.makeText(ResetPassword.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}