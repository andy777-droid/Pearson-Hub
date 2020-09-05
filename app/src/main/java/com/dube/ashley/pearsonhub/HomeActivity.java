package com.dube.ashley.pearsonhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
  Button signOutBTN;
  FirebaseAuth mFirebaseAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;
  private ViewPager viewPager;
  private SliderAdapter sliderAdapter;
  private LinearLayout mDotsSlider;
  private TextView[] mdots;
  private DrawerLayout mdrawer;
  private ActionBarDrawerToggle mToggle;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    mdrawer = (DrawerLayout) findViewById(R.id.id_drawer_layout);
    mToggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);

    mdrawer.addDrawerListener(mToggle);
    mToggle.syncState();

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle("");
    getSupportActionBar().setElevation(0);
    viewPager = (ViewPager) findViewById(R.id.SlideViewPager);
    mDotsSlider = (LinearLayout) findViewById(R.id.dots);

    sliderAdapter = new SliderAdapter(this);
    viewPager.setAdapter(sliderAdapter);
    addDotsSlider(0);
    viewPager.addOnPageChangeListener(viewListener);

    mToggle.setDrawerArrowDrawable(new HamburgerDrawable(this));

  }

  public class HamburgerDrawable extends DrawerArrowDrawable {

    public HamburgerDrawable(Context context){
      super(context);
      setColor(context.getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void draw(Canvas canvas){
      super.draw(canvas);

      setBarLength(100.0f);
      setBarThickness(16.0f);
      setGapSize(20.0f);

    }
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (mToggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void addDotsSlider(int indicator) {
    mdots = new TextView[6];
    mDotsSlider.removeAllViews();

    for (int i = 0; i < mdots.length; i++) {
      mdots[i] = new TextView(this);
      mdots[i].setText(Html.fromHtml("&#8226"));
      mdots[i].setTextSize(35);
      mdots[i].setTextColor(getResources().getColor(R.color.unchecked));

      mDotsSlider.addView(mdots[i]);
    }
    if (mdots.length > 0) {
      mdots[indicator].setTextColor(getResources().getColor(R.color.checked));
    }
  }

  ViewPager.OnPageChangeListener viewListener =
      new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
          addDotsSlider(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
      };
}
