package com.dube.ashley.pearsonhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
{
    Button signOutBTN;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ViewPager viewPager;
    private SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            viewPager = (ViewPager) findViewById(R.id.SlideViewPager);
            sliderAdapter = new SliderAdapter(this);
          viewPager.setAdapter(sliderAdapter);
            //signOutBTN=findViewById(R.id.logOutBTN);

//            signOutBTN.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    FirebaseAuth.getInstance().signOut();
//                    Intent intentMain= new Intent(HomeActivity.this,LoginActivity.class);
//                    startActivity(intentMain);
//                }
//            });


    }

}
