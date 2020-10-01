package com.dube.ashley.pearsonhub;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class Wishlist extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    private NavigationView navigationView;
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mToggle;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    List<CategoryHandler> catBooks;
    FirebaseRecyclerOptions<CategoryHandler> options;
    FirebaseRecyclerAdapter<CategoryHandler, MyViewHolder> adapter;
    RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        mFirebaseAuth=FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        rv=(RecyclerView) findViewById(R.id.recyclerviewWishlist_id);
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
                loadData(Wishlist.this, curUsers.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Wishlist.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        mdrawer = (DrawerLayout) findViewById(R.id.wishlist_drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);

        //Request Books
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(this,1));

        mdrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#008aa3'> Your Wishlist </font>"));
        getSupportActionBar().setElevation(0);

        mToggle.setDrawerArrowDrawable(new HamburgerDrawable(this));

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        Intent home = new Intent(Wishlist.this, HomeActivity.class);
                        startActivity(home);
                        return true;
                    case R.id.menu_search:
                        Intent search = new Intent(Wishlist.this, SearchBook.class);
                        startActivity(search);
                        return true;
                    case R.id.menu_sell:
                        Intent sell = new Intent(Wishlist.this, SellBook.class);
                        startActivity(sell);
                        return true;
                    case R.id.menu_wishlist:
                        Intent wishlist = new Intent(Wishlist.this, Wishlist.class);
                        startActivity(wishlist);
                        return true;
                    case R.id.menu_listings:
                        Intent listings = new Intent(Wishlist.this, Listings.class);
                        startActivity(listings);
                        return true;
                    case R.id.menu_logout:
                        Intent logout = new Intent(Wishlist.this, LoginActivity.class);
                        startActivity(logout);
                        return true;
                }
                return true;
            }
        });
    }

    private void loadData(Context mContext,String UserEmail)
    {
        Log.d("Seller Number Loaddata ",UserEmail);
        final Context myContext;
        myContext = mContext;
        catBooks=new ArrayList<>();

        options= new FirebaseRecyclerOptions.Builder<CategoryHandler>().setQuery(FirebaseDatabase.getInstance().getReference("Wishlist").orderByChild("email").equalTo(UserEmail),CategoryHandler.class).build();
        adapter=new FirebaseRecyclerAdapter<CategoryHandler, MyViewHolder>(options)
        {

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_book_wishlist,parent,false);
                return new MyViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull CategoryHandler model)
            {

                holder.wishlistTitle.setText(model.getTitle());
                holder.wishlistPrice.setText("R "+model.getPrice());
                Picasso.Builder builder = new Picasso.Builder(myContext);
                builder.listener(new Picasso.Listener()
                {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                    {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(model.getThumbnail()).into(holder.WishlistImageView);

                catBooks.add(new CategoryHandler(model.getTitle(),model.getPrice(),model.getCategory(),model.getAuthor(),model.getCondition(),model.getISBN(),model.getSellerNumber(),model.getSellerName(),model.getThumbnail(),model.getBookID()));
                holder.WishlistImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent=new Intent(myContext,Book.class);
                        intent.putExtra("Thumbnail",catBooks.get(position).getThumbnail());
                        intent.putExtra("Title",catBooks.get(position).getTitle());
                        intent.putExtra("Price",catBooks.get(position).getPrice());
                        intent.putExtra("SellerNumber",catBooks.get(position).getSellerNumber());
                        intent.putExtra("SellerName",catBooks.get(position).getSellerName());
                        intent.putExtra("Author",catBooks.get(position).getAuthor());
                        intent.putExtra("Condition",catBooks.get(position).getCondition());
                        intent.putExtra("ISBN",catBooks.get(position).getISBN());
                        myContext.startActivity(intent);

                    }
                });

                holder.removeButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wishlist").child(catBooks.get(position).getBookID());
                        ref.removeValue();

                        Toast.makeText(Wishlist.this,"Wishlist Item Has Been Deleted", Toast.LENGTH_SHORT).show();
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