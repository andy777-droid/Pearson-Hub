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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditBook extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    private NavigationView navigationView;
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mToggle;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    EditText textbookName, bookPrice, bookIsbn, bookAuthor,bookCondition;
    String newtextbookName, newbookPrice, newbookIsbn, newbookAuthor,newbookCondition,newsellersNumber;
    Button saveBtn;
    String title,price,sellNum,author,condition,isbn;
    DatabaseReference ref;
    FirebaseUser userID;
    AwesomeValidation validationA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        validationA = new AwesomeValidation(ValidationStyle.BASIC);

        userID = FirebaseAuth.getInstance().getCurrentUser();

        ref = FirebaseDatabase.getInstance().getReference("Books");

        textbookName= (EditText) findViewById(R.id.textbookName);
        bookPrice=(EditText) findViewById(R.id.price);
        bookIsbn=(EditText) findViewById(R.id.isbn);
        bookAuthor=(EditText) findViewById(R.id.author);
        bookCondition=(EditText) findViewById(R.id.condition);


        Intent intent=getIntent();
         title =intent.getExtras().getString("Title");
         price=intent.getExtras().getString("Price");
         sellNum =intent.getExtras().getString("SellerNumber");
         author=intent.getExtras().getString("Author");
         condition=intent.getExtras().getString("Condition");
         isbn=intent.getExtras().getString("ISBN");




        textbookName.setText(title);
        bookPrice.setText(price);
        bookCondition.setText(condition);
        bookAuthor.setText(author);
        bookIsbn.setText(isbn);




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
                Toast.makeText(EditBook.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });





        mdrawer = (DrawerLayout) findViewById(R.id.edit_drawer_layout);
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
                        Intent home = new Intent(EditBook.this, HomeActivity.class);
                        startActivity(home);
                        return true;
                    case R.id.menu_search:
                        Intent search = new Intent(EditBook.this, SearchBook.class);
                        startActivity(search);
                        return true;
                    case R.id.menu_sell:
                        Intent sell = new Intent(EditBook.this, SellBook.class);
                        startActivity(sell);
                        return true;
                    case R.id.menu_wishlist:
                        Intent wishlist = new Intent(EditBook.this, Wishlist.class);
                        startActivity(wishlist);
                        return true;
                    case R.id.menu_listings:
                        Intent listings = new Intent(EditBook.this, Listings.class);
                        startActivity(listings);
                        return true;
                    case R.id.menu_logout:
                        Intent logout = new Intent(EditBook.this, LoginActivity.class);
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

    public boolean validation(
            String ISBN, String AUTHOR, String CONDITION, String PRICE, String TITLE) {

        boolean valid = true;

        if (!ISBN.matches("^[0-9]*$")) {
            bookIsbn.setError("ISBN must contain numbers only");
            bookIsbn.setFocusable(true);
            valid = false;
        } else {
            if (ISBN.length() == 0) {
                bookIsbn.setError("ISBN must contain a value");
                bookIsbn.setFocusable(true);
                valid = false;
            }
        }

        if (AUTHOR.length() == 0) {
            valid = false;
            bookAuthor.setError("Author must contain a value");
            bookAuthor.setFocusable(true);

        } else {
            int len = AUTHOR.length();
            for (int i = 0; i < len; i++) {
                if ((Character.isLetter(AUTHOR.charAt(i)) == false)) {
                    valid = false;
                    bookAuthor.setError("Author must contain letters only");
                    bookAuthor.setFocusable(true);
                    break;
                }
            }
        }

        if (CONDITION.equals("") || CONDITION.equals(null)) {
            valid = false;
            bookCondition.setError("Please provide a condition");
            bookCondition.setFocusable(true);
        } else {
            int value = Integer.parseInt(CONDITION);
            if (value < 0 || value > 10) {

                valid = false;
                bookCondition.setError("Condition must be between 0 and 10");
                bookCondition.setFocusable(true);
            }
        }

        try {
            double d = Double.parseDouble(PRICE);
        } catch (NumberFormatException nfe) {
            valid = false;
            bookPrice.setError("Price must be a digit");
            bookPrice.setFocusable(true);
        }

        if (TITLE.length() == 0) {
            valid = false;
            textbookName.setError("Please enter a title");
            textbookName.setFocusable(true);
        }

        return valid;
    }


    public void update(View view)
    {

        textbookName= (EditText) findViewById(R.id.textbookName);
        bookPrice=(EditText) findViewById(R.id.price);
        bookIsbn=(EditText) findViewById(R.id.isbn);
        bookAuthor=(EditText) findViewById(R.id.author);
        bookCondition=(EditText) findViewById(R.id.condition);


        validationA.addValidation(this, R.id.textbookName, RegexTemplate.NOT_EMPTY, R.string.invalid_name); //Name validation
        validationA.addValidation(this, R.id.price, RegexTemplate.NOT_EMPTY, R.string.numbersOnly);
        validationA.addValidation(this, R.id.isbn,  "^[0-9]{13}$"    , R.string.invalid_ISBN);
        validationA.addValidation(this,R.id.author, RegexTemplate.NOT_EMPTY, R.string.auth);
        validationA.addValidation(this,R.id.condition, "[1-9]|10", R.string.condition);

        if (validationA.validate())
        {
            ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("title").setValue(textbookName.getText().toString());
            ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("author").setValue(bookAuthor.getText().toString());
            ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ISBN").setValue(bookIsbn.getText().toString());
            ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("condition").setValue(bookCondition.getText().toString());
            ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("price").setValue(bookPrice.getText().toString());

            Toast.makeText(EditBook.this,"Your update was successful",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(EditBook.this,"Validation Failed",Toast.LENGTH_LONG).show();
        }



    }

}