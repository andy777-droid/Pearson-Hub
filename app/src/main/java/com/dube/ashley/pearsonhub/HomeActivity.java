package com.dube.ashley.pearsonhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
{
    Button signOutBTN;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ViewPager viewPager;
    private SliderAdapter sliderAdapter;
    private LinearLayout mDotsSlider;
    private TextView[] mdots;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            viewPager = (ViewPager) findViewById(R.id.SlideViewPager);
            mDotsSlider = (LinearLayout) findViewById(R.id.dots);

            sliderAdapter = new SliderAdapter(this);
            viewPager.setAdapter(sliderAdapter);
            addDotsSlider(0);
            viewPager.addOnPageChangeListener(viewListener);

    }

    public void addDotsSlider(int indicator){
        mdots = new TextView[6];
        mDotsSlider.removeAllViews();

        for (int i = 0; i < mdots.length; i++){
            mdots[i] = new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.unchecked));

            mDotsSlider.addView(mdots[i]);


        }
        if (mdots.length > 0){
            mdots[indicator].setTextColor(getResources().getColor(R.color.checked));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener()  {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
           addDotsSlider(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
