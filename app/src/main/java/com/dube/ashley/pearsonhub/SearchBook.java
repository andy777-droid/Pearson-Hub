package com.dube.ashley.pearsonhub;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java.util.List;



public class SearchBook extends AppCompatActivity
{
    FirebaseAuth mFirebaseAuth;
    List<CategoryHandler> catBooks;
    private NavigationView navigationView;
    private DrawerLayout mdrawer;
    private Button searchBTN;
    private EditText searchBookET;
    private ActionBarDrawerToggle mToggle;
    private DatabaseReference databaseReference,databaseReference2;
    private FirebaseUser user;
    User curUsers;
    //progressBar
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Searching for Books...");
        mProgress.setMessage("Please Wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        searchBTN = findViewById(R.id.searchBTN);
        searchBookET = findViewById(R.id.searchBookET);

        databaseReference2=FirebaseDatabase.getInstance().getReference().child("Books");

        mFirebaseAuth=FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                curUsers =snapshot.getValue(User.class);
                assert curUsers != null;
                NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
                View header = navigationView.getHeaderView(0);
                TextView tv = (TextView) header.findViewById(R.id.id_nav_header);
                tv.setText( curUsers.getFirstname()+" "+curUsers.getLastname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(SearchBook.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        mdrawer = (DrawerLayout) findViewById(R.id.search_drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);

        mdrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#008aa3'> Search </font>"));

        getSupportActionBar().setElevation(0);

        mToggle.setDrawerArrowDrawable(new HamburgerDrawable(this));

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        Intent home = new Intent(SearchBook.this, HomeActivity.class);
                        startActivity(home);
                        return true;
                    case R.id.menu_search:
                        Intent search = new Intent(SearchBook.this, SearchBook.class);
                        startActivity(search);
                        return true;
                    case R.id.menu_sell:
                        Intent sell = new Intent(SearchBook.this, SellBook.class);
                        startActivity(sell);
                        return true;
                    case R.id.menu_wishlist:
                        Intent wishlist = new Intent(SearchBook.this, Wishlist.class);
                        startActivity(wishlist);
                        return true;
                    case R.id.menu_listings:
                        Intent listings = new Intent(SearchBook.this, Listings.class);
                        startActivity(listings);
                        return true;
                    case R.id.menu_logout:
                        Intent logout = new Intent(SearchBook.this, LoginActivity.class);
                        startActivity(logout);
                        return true;
                }
                return true;
            }
        });

        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mProgress.show();

                if (TextUtils.isEmpty(searchBookET.getText().toString().trim()))
                {
                    mProgress.dismiss();
                    searchBookET.setError("Enter a book Name");
                }
                else
                {
                    String search=searchBookET.getText().toString().trim().toLowerCase();
                    searchBooks(search, curUsers.getEmail());
                }
            }
        });
    }

    private void searchBooks(final String search, final String theEmail)
    {
        catBooks=new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean found;
                int x=0;
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    search.toLowerCase();
                    String bookName = ds.child("title").getValue(String.class).toLowerCase();
                    found = bookName.contains(search);
                    if (found==true)
                    {
                        String bookNameProper=ds.child("title").getValue(String.class);
                        String price = ds.child("price").getValue(String.class);
                        String category = ds.child("category").getValue(String.class);
                        String sellerNumber = ds.child("sellerNumber").getValue(String.class);
                        String sellerName = ds.child("sellerName").getValue(String.class);
                        String author = ds.child("author").getValue(String.class);
                        String condition = ds.child("condition").getValue(String.class);
                        String ISBN = ds.child("ISBN").getValue(String.class);
                        String thumbnail = ds.child("thumbnail").getValue(String.class);
                        String bookID = ds.child("bookID").getValue(String.class);


                        catBooks.add(new CategoryHandler(bookNameProper,price,category,author,condition,ISBN,sellerNumber,sellerName,thumbnail,bookID));

                        RecyclerView rv=(RecyclerView) findViewById(R.id.recyclerviewSearch_id);
                        RecyclerViewAdapter rva=new RecyclerViewAdapter(SearchBook.this,catBooks, theEmail);
                        rv.setLayoutManager(new GridLayoutManager(SearchBook.this,2));
                        mProgress.dismiss();
                        rv.setNestedScrollingEnabled(false);
                        rv.setAdapter(rva);
                        x++;
                    }
                }
                if(x==0)
                {
                    mProgress.dismiss();
                    Toast.makeText(
                            SearchBook.this,
                            "No Books Found",
                            Toast.LENGTH_LONG)
                            .show();
                }
                else
                {
                    mProgress.dismiss();
                    Toast.makeText(
                            SearchBook.this,x+
                            " Books Found",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference2.addListenerForSingleValueEvent(eventListener);
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