package com.dube.ashley.pearsonhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class Categories extends AppCompatActivity
{
    List<CategoryHandler> catBooks;
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private LinearLayout click1;
    //View Image from Database
    FirebaseRecyclerOptions<CategoryHandler> options;
    FirebaseRecyclerAdapter<CategoryHandler, MyViewHolder> adapter;
    DatabaseReference databaseReference,databaseReference2;
    RecyclerView rv;
    int myPosition;
    TextView catName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Intent intent=getIntent();

        final String branch=intent.getExtras().getString("branch");
        String categoryName=intent.getExtras().getString("categoryName");
        catName=(TextView) findViewById(R.id.categoryName);
        catName.setText(categoryName);

        mdrawer = (DrawerLayout) findViewById(R.id.category_drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);

        mdrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        getSupportActionBar().setElevation(0);
        //Request Books
        rv=(RecyclerView) findViewById(R.id.recyclerview_id);



        mToggle.setDrawerArrowDrawable(new Categories.HamburgerDrawable(this));

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        databaseReference2.addValueEventListener(new ValueEventListener()
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
                loadData(Categories.this,branch, curUsers.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Categories.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


        mdrawer = (DrawerLayout) findViewById(R.id.category_drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);

        mdrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new GridLayoutManager(this,2));
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
                        Intent home = new Intent(Categories.this, HomeActivity.class);
                        startActivity(home);
                        return true;
                    case R.id.menu_search:
                        Intent search = new Intent(Categories.this, SearchBook.class);
                        startActivity(search);
                        return true;
                    case R.id.menu_sell:
                        Intent sell = new Intent(Categories.this, SellBook.class);
                        startActivity(sell);
                        return true;
                    case R.id.menu_wishlist:
                        Intent wishlist = new Intent(Categories.this, Wishlist.class);
                        startActivity(wishlist);
                        return true;
                    case R.id.menu_listings:
                        Intent listings = new Intent(Categories.this, Listings.class);
                        startActivity(listings);
                        return true;
                    case R.id.menu_logout:
                        Intent logout = new Intent(Categories.this, LoginActivity.class);
                        startActivity(logout);
                        return true;
                }
                return true;
            }
        });

    }


    private void loadData(Context mContext, final String theCat, final String theEmail)
    {
        final Context myContext;
        myContext = mContext;
        catBooks=new ArrayList<>();

        options= new FirebaseRecyclerOptions.Builder<CategoryHandler>().setQuery(FirebaseDatabase.getInstance().getReference("Books").orderByChild("category").startAt(theCat).endAt(theCat),CategoryHandler.class).build();
        adapter=new FirebaseRecyclerAdapter<CategoryHandler, MyViewHolder>(options)
        {

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_book,parent,false);
                return new MyViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull CategoryHandler model)
            {
                holder.title.setText(model.getTitle());
                holder.price.setText("R "+model.getPrice());
                Picasso.Builder builder = new Picasso.Builder(myContext);
                builder.listener(new Picasso.Listener()
                {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                    {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(model.getThumbnail()).into(holder.thumbnail);

                catBooks.add(new CategoryHandler(model.getTitle(),model.getPrice(),model.getCategory(),model.getAuthor(),model.getCondition(),model.getISBN(),model.getSellerNumber(),model.getSellerName(),model.getThumbnail(),model.getBookID()));
                //click listener
                holder.cardView.setOnClickListener(new View.OnClickListener() {
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
                        intent.putExtra("email",theEmail);
                        intent.putExtra("category",catBooks.get(position).getCategory());
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