package com.dube.ashley.pearsonhub;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class Book extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    private NavigationView navigationView;
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mToggle;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private TextView tvTitle, tvPrice, tvSellerNumber, tvAuthor, tvCondition, tvISBN, tvSellerName;
    private ImageView img;
    Button wishBtn;
    User curUsers;
    List<CategoryHandler> catBooks;
    FirebaseRecyclerOptions<CategoryHandler> options;
    FirebaseRecyclerAdapter<CategoryHandler, MyViewHolder> adapter;

    //progressBar
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Adding to Wishlist...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        wishBtn = (Button) findViewById(R.id.wishlistButton);
        tvTitle = (TextView) findViewById(R.id.textView);
        tvPrice = (TextView) findViewById(R.id.textView2);
        tvSellerNumber = (TextView) findViewById(R.id.textView12);
        tvSellerName = (TextView) findViewById(R.id.textView11);
        tvAuthor = (TextView) findViewById(R.id.textView9);
        tvCondition = (TextView) findViewById(R.id.textView10);
        tvISBN = (TextView) findViewById(R.id.textView8);
        img = (ImageView) findViewById(R.id.imageView4);

        final Intent intent = getIntent();

        final String image = intent.getExtras().getString("Thumbnail");
        final String title = intent.getExtras().getString("Title");
        final String price = intent.getExtras().getString("Price");
        final String sellerNumber = intent.getExtras().getString("SellerNumber");
        final String sellerName = intent.getExtras().getString("SellerName");
        final String author = intent.getExtras().getString("Author");
        final String condition = intent.getExtras().getString("Condition");
        final String isbn = intent.getExtras().getString("ISBN");
        final String category = intent.getExtras().getString("category");
        final String email = intent.getExtras().getString("email");

        tvTitle.setText(title);
        tvPrice.setText("R " + price);
        tvSellerNumber.setText(sellerNumber);
        tvAuthor.setText(author);
        tvCondition.setText(String.valueOf(condition));
        tvISBN.setText(isbn);
        tvSellerName.setText(sellerName);
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        builder.build().load(image).into(img);

        mFirebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                curUsers = snapshot.getValue(User.class);
                assert curUsers != null;
                NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
                View header = navigationView.getHeaderView(0);
                TextView tv = (TextView) header.findViewById(R.id.id_nav_header);
                tv.setText(curUsers.getFirstname() + " " + curUsers.getLastname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Book.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        mdrawer = (DrawerLayout) findViewById(R.id.book_drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);

        mdrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#008aa3'> Book Details </font>"));
        getSupportActionBar().setElevation(0);

        mToggle.setDrawerArrowDrawable(new HamburgerDrawable(this));

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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

                wishBtn.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mProgress.show();
                                String uuid = UUID.randomUUID().toString();
                                final String uniqueID = uuid.replace("-", "");


                                DatabaseReference imagestore =
                                        FirebaseDatabase.getInstance()
                                                .getReference()
                                                .child("Wishlist")
                                                .child(uniqueID);

                                HashMap<String, String> hash = new HashMap<>();
                                hash.put("ISBN", isbn);
                                hash.put("author", author);
                                hash.put("category", category);
                                hash.put("condition", condition);
                                hash.put("email", email);
                                hash.put("price", price);
                                hash.put("sellerName", sellerName);
                                hash.put("sellerNumber", sellerNumber);
                                hash.put("thumbnail", image);
                                hash.put("title", title);
                                hash.put("bookID", uniqueID);

                                imagestore.setValue(hash).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mProgress.dismiss();

                                        Toast.makeText(
                                                Book.this,
                                                "Uploaded",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                        Intent i =
                                                new Intent(
                                                        Book.this, Wishlist.class);
                                        startActivity(i);
                                    }
                                });
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