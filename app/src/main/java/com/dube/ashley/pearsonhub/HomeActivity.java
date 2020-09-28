package com.dube.ashley.pearsonhub;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeActivity extends AppCompatActivity {
  FirebaseAuth mFirebaseAuth;
  private ViewPager viewPager;
  private SliderAdapter sliderAdapter;
  private NavigationView navigationView;
  private LinearLayout mDotsSlider;
  private TextView[] mdots;
  private DrawerLayout mdrawer;
  private ActionBarDrawerToggle mToggle;
  private Button graphicsDesignBTN,
      artsBTN,
      lawsBTN,
      compSciBTN,
      tourismBTN,
      psychologyBTN,
      mathBTN,
      biologyBTN,
      physicsBTN,
      chemistryBTN,
      businessBTN,
      englishBTN;
  private DatabaseReference databaseReference;
  Query databaseReference2;
  private FirebaseUser user;
  String name;
  List<CategoryHandler> catBooks;

  String graphicsCategory = "GraphicsDesign";
  String artsCategory = "BA";
  String lawsCategory = "Law";
  String compSciCategory = "ComputerScience";
  String tourismCategory = "Tourism";
  String psychoCategory = "Psychology";
  String biologyCategory = "Biology";
  String englishCategory = "English";
  String mathsCategory = "Mathematics";
  String businessCategory = "BusinessManagement";
  String physicsCategory = "PhysicalScience";
  String chemistryCategory = "Chemistry";

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    mFirebaseAuth = FirebaseAuth.getInstance();
    user = FirebaseAuth.getInstance().getCurrentUser();
    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
    databaseReference.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            User curUsers = snapshot.getValue(User.class);
            assert curUsers != null;
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
            View header = navigationView.getHeaderView(0);
            TextView tv = (TextView) header.findViewById(R.id.id_nav_header);
            tv.setText(curUsers.getFirstname() + " " + curUsers.getLastname());
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
          }
        });

    mdrawer = (DrawerLayout) findViewById(R.id.home_drawer_layout);
    mToggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);

    mdrawer.addDrawerListener(mToggle);
    mToggle.syncState();

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle("");
    getSupportActionBar().setElevation(0);
    viewPager = (ViewPager) findViewById(R.id.SlideViewPager);
    mDotsSlider = (LinearLayout) findViewById(R.id.dots);

      catBooks=new ArrayList<>();
      databaseReference2 = FirebaseDatabase.getInstance().getReference("Books").limitToLast(10);
      databaseReference2.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot)
          {
              for (DataSnapshot ds: snapshot.getChildren())
              {
                  String title = ds.child("title").getValue(String.class);
                  String price = ds.child("price").getValue(String.class);
                  String category = ds.child("category").getValue(String.class);
                  String sellerNumber = ds.child("sellerNumber").getValue(String.class);
                  String sellerName = ds.child("sellerName").getValue(String.class);
                  String author = ds.child("author").getValue(String.class);
                  String condition = ds.child("condition").getValue(String.class);
                  String ISBN = ds.child("ISBN").getValue(String.class);
                  String thumbnail = ds.child("thumbnail").getValue(String.class);
                  catBooks.add(new CategoryHandler(title,price,category,author,condition,ISBN,sellerNumber,sellerName,thumbnail));
              }
              sliderAdapter = new SliderAdapter(HomeActivity.this,catBooks);
              viewPager.setAdapter(sliderAdapter);
              addDotsSlider(0);
              viewPager.addOnPageChangeListener(viewListener);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error)
          {
              // Getting Post failed, log a message
              Log.w(TAG, "onCancelled", error.toException());
              // ...
          }
      });


    mToggle.setDrawerArrowDrawable(new HamburgerDrawable(this));

    graphicsDesignBTN = (Button) findViewById(R.id.graphicsDesignBTN);
    artsBTN = (Button) findViewById(R.id.artsBTN);
    lawsBTN = (Button) findViewById(R.id.lawsBTN);
    compSciBTN = (Button) findViewById(R.id.compSciBTN);
    tourismBTN = (Button) findViewById(R.id.tourismBTN);
    psychologyBTN = (Button) findViewById(R.id.psychologyBTN);

    mathBTN = (Button) findViewById(R.id.mathBTN);
    chemistryBTN = (Button) findViewById(R.id.chemistryBTN);
    biologyBTN = (Button) findViewById(R.id.biologyBTN);
    physicsBTN = (Button) findViewById(R.id.physicsBTN);
    businessBTN = (Button) findViewById(R.id.businessBTN);
    englishBTN = (Button) findViewById(R.id.englishBTN);

    englishBTN.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent categoryPage = new Intent(HomeActivity.this, Categories.class);
            categoryPage.putExtra("branch", englishCategory);
            categoryPage.putExtra("categoryName", "English");
            startActivity(categoryPage);
          }
        });

    businessBTN.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent categoryPage = new Intent(HomeActivity.this, Categories.class);
            categoryPage.putExtra("branch", businessCategory);
            categoryPage.putExtra("categoryName", "Business Management");
            startActivity(categoryPage);
          }
        });

    physicsBTN.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent categoryPage = new Intent(HomeActivity.this, Categories.class);
            categoryPage.putExtra("branch", physicsCategory);
            categoryPage.putExtra("categoryName", "Physical Science");
            startActivity(categoryPage);
          }
        });

    biologyBTN.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent categoryPage = new Intent(HomeActivity.this, Categories.class);
            categoryPage.putExtra("branch", biologyCategory);
            categoryPage.putExtra("categoryName", "Biology");
            startActivity(categoryPage);
          }
        });

    chemistryBTN.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent categoryPage = new Intent(HomeActivity.this, Categories.class);
            categoryPage.putExtra("branch", chemistryCategory);
            categoryPage.putExtra("categoryName", "Chemistry");
            startActivity(categoryPage);
          }
        });

    mathBTN.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent categoryPage = new Intent(HomeActivity.this, Categories.class);
            categoryPage.putExtra("branch", mathsCategory);
            categoryPage.putExtra("categoryName", "Mathematics");
            startActivity(categoryPage);
          }
        });

    graphicsDesignBTN.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent categoryPage = new Intent(HomeActivity.this, Categories.class);
            categoryPage.putExtra("branch", graphicsCategory);
            categoryPage.putExtra("categoryName", "Graphics Design");
            startActivity(categoryPage);
          }
        });

    artsBTN.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent categoryPage = new Intent(HomeActivity.this, Categories.class);
            categoryPage.putExtra("branch", artsCategory);
            categoryPage.putExtra("categoryName", "Arts");
            startActivity(categoryPage);
          }
        });

    compSciBTN.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent categoryPage = new Intent(HomeActivity.this, Categories.class);
            categoryPage.putExtra("branch", compSciCategory);
            categoryPage.putExtra("categoryName", "Computer Science");
            startActivity(categoryPage);
          }
        });

    lawsBTN.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent categoryPage = new Intent(HomeActivity.this, Categories.class);
            categoryPage.putExtra("branch", lawsCategory);
            categoryPage.putExtra("categoryName", "Law");
            startActivity(categoryPage);
          }
        });

    tourismBTN.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent categoryPage = new Intent(HomeActivity.this, Categories.class);
            categoryPage.putExtra("branch", tourismCategory);
            categoryPage.putExtra("categoryName", "Tourism");
            startActivity(categoryPage);
          }
        });

    psychologyBTN.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent categoryPage = new Intent(HomeActivity.this, Categories.class);
            categoryPage.putExtra("branch", psychoCategory);
            categoryPage.putExtra("categoryName", "Psychology");
            startActivity(categoryPage);
          }
        });

    navigationView = findViewById(R.id.navigation_view);

    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.menu_home:
                Intent home = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(home);
                return true;
              case R.id.menu_search:
                Intent search = new Intent(HomeActivity.this, SearchBook.class);
                startActivity(search);
                return true;
              case R.id.menu_sell:
                Intent sell = new Intent(HomeActivity.this, SellBook.class);
                startActivity(sell);
                return true;
              case R.id.menu_wishlist:
                Intent wishlist = new Intent(HomeActivity.this, Wishlist.class);
                startActivity(wishlist);
                return true;
              case R.id.menu_listings:
                Intent listings = new Intent(HomeActivity.this, Listings.class);
                startActivity(listings);
                return true;
              case R.id.menu_logout:
                Intent logout = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(logout);
                return true;
            }
            return true;
          }
        });
  }

  public class HamburgerDrawable extends DrawerArrowDrawable {

    public HamburgerDrawable(Context context) {
      super(context);
      setColor(context.getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void draw(Canvas canvas) {
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
    mdots = new TextView[catBooks.size()];
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
