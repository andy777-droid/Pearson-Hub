package com.dube.ashley.pearsonhub;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Book extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    private NavigationView navigationView;
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mToggle;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private TextView tvTitle,tvPrice,tvSellerNumber,tvAuthor,tvCondition,tvISBN,tvSellerName;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        tvTitle=(TextView) findViewById(R.id.textView);
        tvPrice=(TextView) findViewById(R.id.textView2);
        tvSellerNumber=(TextView) findViewById(R.id.textView12);
        tvSellerName=(TextView) findViewById(R.id.textView11);
        tvAuthor=(TextView) findViewById(R.id.textView9);
        tvCondition=(TextView) findViewById(R.id.textView10);
        tvISBN=(TextView) findViewById(R.id.textView8);
        img=(ImageView) findViewById(R.id.imageView4);

        Intent intent=getIntent();

        int image=intent.getExtras().getInt("Thumbnail");
        String title =intent.getExtras().getString("Title");
        String price=intent.getExtras().getString("Price");
        String sellerNumber=intent.getExtras().getString("SellerNumber");
        String sellerName=intent.getExtras().getString("SellerName");
        String author=intent.getExtras().getString("Author");
        int condition=intent.getExtras().getInt("Condition");
        String isbn=intent.getExtras().getString("ISBN");

        tvTitle.setText(title);
        tvPrice.setText("R "+price);
        tvSellerNumber.setText(sellerNumber);
        tvAuthor.setText(author);
        tvCondition.setText(String.valueOf(condition));
        tvISBN.setText(isbn);
        tvSellerName.setText(sellerName);
        img.setImageResource(image);


        mFirebaseAuth=FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                User curUsers =snapshot.getValue(User.class);
                assert curUsers != null;
                NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
                View header = navigationView.getHeaderView(0);
                TextView tv = (TextView) header.findViewById(R.id.id_nav_header);
                tv.setText( curUsers.getFirstname()+" "+curUsers.getLastname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Book.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        mdrawer = (DrawerLayout) findViewById(R.id.book_drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);

        mdrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        getSupportActionBar().setElevation(0);

        mToggle.setDrawerArrowDrawable(new HamburgerDrawable(this));

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        Intent home = new Intent(Book.this, HomeActivity.class);
                        startActivity(home);
                        return true;
                    case R.id.menu_search:
                        Intent search = new Intent(Book.this, SearchBook.class);
                        startActivity(search);
                        return true;
                    case R.id.menu_sell:
                        Intent sell = new Intent(Book.this, SellBook.class);
                        startActivity(sell);
                        return true;
                    case R.id.menu_wishlist:
                        Intent Book = new Intent(Book.this, Book.class);
                        startActivity(Book);
                        return true;
                    case R.id.menu_listings:
                        Intent listings = new Intent(Book.this, Listings.class);
                        startActivity(listings);
                        return true;
                    case R.id.menu_logout:
                        Intent logout = new Intent(Book.this, LoginActivity.class);
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
}