package com.dube.ashley.pearsonhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity
{
    Button signOutBTN;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //GOOGLE VARIABLES
    TextView name, mail;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            //GOOGLE LOG OUT
            logout = findViewById(R.id.google_log_out);
            name = findViewById(R.id.userName);
            mail = findViewById(R.id.userMail);

            GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
            if (signInAccount != null)
            {
                name.setText(signInAccount.getDisplayName());
                mail.setText(signInAccount.getEmail());
            }

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

            signOutBTN=findViewById(R.id.logOutBTN);

            signOutBTN.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FirebaseAuth.getInstance().signOut();
                    Intent intentMain= new Intent(HomeActivity.this,LoginActivity.class);
                    startActivity(intentMain);
                }
            });


    }
}
