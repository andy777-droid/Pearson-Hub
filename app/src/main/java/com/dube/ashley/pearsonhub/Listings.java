package com.dube.ashley.pearsonhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Listings extends AppCompatActivity {
  List<CategoryHandler> catBooks;
  FirebaseAuth mFirebaseAuth;
  private NavigationView navigationView;
  private DrawerLayout mdrawer;
  private ActionBarDrawerToggle mToggle;
  private DatabaseReference databaseReference;
  private FirebaseUser user;
  String sellerNumber;
  FirebaseRecyclerOptions<CategoryHandler> options;
  FirebaseRecyclerAdapter<CategoryHandler, MyViewHolder> adapter;
  RecyclerView rv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_listings);

    mFirebaseAuth = FirebaseAuth.getInstance();
    user = FirebaseAuth.getInstance().getCurrentUser();
    rv = (RecyclerView) findViewById(R.id.recyclerviewListings_id);
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
            sellerNumber = curUsers.getCellNumber();
            loadData(Listings.this, sellerNumber, curUsers.getEmail());
            tv.setText(curUsers.getFirstname() + " " + curUsers.getLastname());
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(Listings.this, error.getMessage(), Toast.LENGTH_LONG).show();
          }
        });

    // Request Books
    rv.setNestedScrollingEnabled(false);
    rv.setLayoutManager(new GridLayoutManager(this, 1));

    mdrawer = (DrawerLayout) findViewById(R.id.listings_drawer_layout);
    mToggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);

    mdrawer.addDrawerListener(mToggle);
    mToggle.syncState();

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(Html.fromHtml("<font color='#008aa3'> Your Listings </font>"));
    getSupportActionBar().setElevation(0);

    mToggle.setDrawerArrowDrawable(new HamburgerDrawable(this));

    navigationView = (NavigationView) findViewById(R.id.navigation_view);

    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.menu_home:
                Intent home = new Intent(Listings.this, HomeActivity.class);
                startActivity(home);
                return true;
              case R.id.menu_search:
                Intent search = new Intent(Listings.this, SearchBook.class);
                startActivity(search);
                return true;
              case R.id.menu_sell:
                Intent sell = new Intent(Listings.this, SellBook.class);
                startActivity(sell);
                return true;
              case R.id.menu_wishlist:
                Intent wishlist = new Intent(Listings.this, Wishlist.class);
                startActivity(wishlist);
                return true;
              case R.id.menu_listings:
                Intent listings = new Intent(Listings.this, Listings.class);
                startActivity(listings);
                return true;
              case R.id.menu_logout:
                Intent logout = new Intent(Listings.this, LoginActivity.class);
                startActivity(logout);
                return true;
            }
            return true;
          }
        });
  }

  private void loadData(Context mContext, String theSellerNum, final String theEmail) {
    Log.d("Seller Number Loaddata ", theSellerNum);
    final Context myContext;
    myContext = mContext;
    catBooks = new ArrayList<>();

    options =
        new FirebaseRecyclerOptions.Builder<CategoryHandler>()
            .setQuery(
                FirebaseDatabase.getInstance()
                    .getReference("Books")
                    .orderByChild("sellerNumber")
                    .equalTo(theSellerNum),
                CategoryHandler.class)
            .build();
    adapter =
        new FirebaseRecyclerAdapter<CategoryHandler, MyViewHolder>(options) {

          @NonNull
          @Override
          public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view =
                LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_book_listings, parent, false);
            return new MyViewHolder(view);
          }

          @Override
          protected void onBindViewHolder(
              @NonNull MyViewHolder holder, final int position, @NonNull CategoryHandler model) {

            holder.listingsTitle.setText(model.getTitle());
            holder.listingsPrice.setText("R " + model.getPrice());
            Picasso.Builder builder = new Picasso.Builder(myContext);
            builder.listener(
                new Picasso.Listener() {
                  @Override
                  public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                  }
                });

             builder.build().load(model.getThumbnail()).into(holder.listingsImageView);


            catBooks.add(
                new CategoryHandler(
                    model.getTitle(),
                    model.getPrice(),
                    model.getCategory(),
                    model.getAuthor(),
                    model.getCondition(),
                    model.getISBN(),
                    model.getSellerNumber(),
                    model.getSellerName(),
                    model.getThumbnail(),
                    model.getBookID()));
            holder.listingsImageView.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    Intent intent = new Intent(myContext, Book.class);
                    intent.putExtra("Thumbnail", catBooks.get(position).getThumbnail());
                    intent.putExtra("Title", catBooks.get(position).getTitle());
                    intent.putExtra("Price", catBooks.get(position).getPrice());
                    intent.putExtra("SellerNumber", catBooks.get(position).getSellerNumber());
                    intent.putExtra("SellerName", catBooks.get(position).getSellerName());
                    intent.putExtra("Author", catBooks.get(position).getAuthor());
                    intent.putExtra("Condition", catBooks.get(position).getCondition());
                    intent.putExtra("ISBN", catBooks.get(position).getISBN());
                    intent.putExtra("email", theEmail);
                    intent.putExtra("category", catBooks.get(position).getCategory());
                    intent.putExtra("bookID", catBooks.get(position).getBookID());
                    myContext.startActivity(intent);
                  }
                });

            holder.removeButton.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                    AlertDialog.Builder popper = new AlertDialog.Builder(Listings.this);
                    popper
                        .setMessage("Do you want to remove this listing?")
                        .setCancelable(false)
                        .setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference ref =
                                    FirebaseDatabase.getInstance()
                                        .getReference("Books")
                                        .child(catBooks.get(position).getBookID());
                                ref.removeValue();

                                Toast.makeText(
                                        Listings.this, "Book Has Been Deleted", Toast.LENGTH_SHORT)
                                    .show();
                                Intent intentHome = new Intent(Listings.this, Listings.class);
                                startActivity(intentHome);
                                finish();
                              }
                            })
                        .setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                              }
                            });

                    AlertDialog alert = popper.create();
                    alert.setTitle("Remove Listing");
                    alert.show();
                  }
                });

            holder.editButton.setOnClickListener(
                new View.OnClickListener() {
                  public void onClick(View v) {
                    // Perform action on click
                    Intent intent = new Intent(myContext, EditBook.class);
                    intent.putExtra("Thumbnail", catBooks.get(position).getThumbnail());
                    intent.putExtra("Title", catBooks.get(position).getTitle());
                    intent.putExtra("Price", catBooks.get(position).getPrice());
                    intent.putExtra("SellerNumber", catBooks.get(position).getSellerNumber());
                    intent.putExtra("SellerName", catBooks.get(position).getSellerName());
                    intent.putExtra("Author", catBooks.get(position).getAuthor());
                    intent.putExtra("Condition", catBooks.get(position).getCondition());
                    intent.putExtra("ISBN", catBooks.get(position).getISBN());
                    intent.putExtra("bookID", catBooks.get(position).getBookID());

                    myContext.startActivity(intent);
                  }
                });
          }
        };
    adapter.startListening();
    rv.setAdapter(adapter);
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
