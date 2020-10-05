package com.dube.ashley.pearsonhub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class SellBook extends AppCompatActivity {
  TextView name, number, isbn, author, condition, price, textbookname, imagesrc;
  Spinner myspinner;
  String userID, titl, is, au, cat, con, pri, nam, num;
  int index;
  Uri imageData;
  Button list, save;
  private DrawerLayout mdrawer;
  private ActionBarDrawerToggle mToggle;
  private FirebaseAuth mFirebaseAuth;
  private NavigationView navigationView;
  private DatabaseReference databaseReference;
  private FirebaseUser user;
  public static final int ImageBack = 1;

  private StorageReference folder;
  final String[] arraySpinner =
      new String[] {
        "Graphics Design",
        "Psychology",
        "Tourism",
        "Computer Science",
        "Law",
        "Bachelor of Arts",
        "Mathematics",
        "Biology",
        "Physical Science",
        "Chemistry",
        "Business Management",
        "English Language"
      };

  // progressBar
  private ProgressDialog mProgress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sell_book);

    mProgress = new ProgressDialog(this);
    mProgress.setTitle("Uploading Your Book...");
    mProgress.setMessage("Please wait...");
    mProgress.setCancelable(false);
    mProgress.setIndeterminate(true);

    list = (Button) findViewById(R.id.listSale);
    isbn = (TextView) findViewById(R.id.isbn);
    author = (TextView) findViewById(R.id.author);
    condition = (TextView) findViewById(R.id.condition);
    price = (TextView) findViewById(R.id.price);
    textbookname = (TextView) findViewById(R.id.textbookName);
    name = (TextView) findViewById(R.id.sellerName);
    number = (TextView) findViewById(R.id.sellerNumber);
    myspinner = (Spinner) findViewById(R.id.spinner);
    imagesrc = (TextView) findViewById(R.id.imageSrc);
    save = (Button) findViewById(R.id.saveBook);

    folder = FirebaseStorage.getInstance().getReference().child("Books");

    final ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    myspinner.setAdapter(adapter);

    mFirebaseAuth = FirebaseAuth.getInstance();
    user = FirebaseAuth.getInstance().getCurrentUser();
    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
    databaseReference.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            User curUsers = snapshot.getValue(User.class);
            assert curUsers != null;
            navigationView = findViewById(R.id.navigation_view);
            View header = navigationView.getHeaderView(0);
            TextView tv = header.findViewById(R.id.id_nav_header);
            tv.setText(curUsers.getFirstname() + " " + curUsers.getLastname());
            name.setText(curUsers.getFirstname());
            number.setText(curUsers.getCellNumber());
            userID = curUsers.getUserId();
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(SellBook.this, error.getMessage(), Toast.LENGTH_LONG).show();
          }
        });

    mdrawer = findViewById(R.id.sell_drawer_layout);
    mToggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);

    mdrawer.addDrawerListener(mToggle);
    mToggle.syncState();

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(Html.fromHtml("<font color='#008aa3'> Sell </font>"));
    getSupportActionBar().setElevation(0);

    mToggle.setDrawerArrowDrawable(new SellBook.HamburgerDrawable(this));

    navigationView = findViewById(R.id.navigation_view);

    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.menu_home:
                Intent home = new Intent(SellBook.this, HomeActivity.class);
                startActivity(home);
                return true;
              case R.id.menu_search:
                Intent search = new Intent(SellBook.this, SearchBook.class);
                startActivity(search);
                return true;
              case R.id.menu_sell:
                Intent sell = new Intent(SellBook.this, SellBook.class);
                startActivity(sell);
                return true;
              case R.id.menu_wishlist:
                Intent wishlist = new Intent(SellBook.this, Wishlist.class);
                startActivity(wishlist);
                return true;
              case R.id.menu_listings:
                Intent listings = new Intent(SellBook.this, Listings.class);
                startActivity(listings);
                return true;
              case R.id.menu_logout:
                Intent logout = new Intent(SellBook.this, LoginActivity.class);
                startActivity(logout);
                return true;
            }
            return true;
          }
        });
  }

  public String getCategoryValue(int value) {
    String correct = "";
    switch (value) {
      case 0:
        correct = "GraphicsDesign";
        break;
      case 1:
        correct = "Psychology";
        break;
      case 2:
        correct = "Tourism";
        break;
      case 3:
        correct = "ComputerScience";
        break;
      case 4:
        correct = "Law";
        break;
      case 5:
        correct = "BA";
        break;
      case 6:
        correct = "Mathematics";
        break;
      case 7:
        correct = "Biology";
        break;
      case 8:
        correct = "PhysicalScience";
        break;
      case 9:
        correct = "Chemistry";
        break;
      case 10:
        correct = "BusinessManagement";
        break;
      case 11:
        correct = "English";
        break;
    }
    return correct;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (mToggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void Upload(View view) {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    startActivityForResult(intent, ImageBack);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == ImageBack) {
      if (resultCode == RESULT_OK) {
        imageData = data.getData();

        if (!imageData.equals(null)) {
          save.setBackgroundResource(R.drawable.rounded_image_fill);
          imagesrc.setText(imageData + "");

          list.setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  mProgress.show();

                  titl = textbookname.getText().toString();
                  is = isbn.getText().toString();
                  au = author.getText().toString();
                  index = myspinner.getSelectedItemPosition();
                  cat = getCategoryValue(index);
                  con = condition.getText().toString();
                  pri = price.getText().toString();
                  nam = name.getText().toString();
                  num = number.getText().toString();

                  final StorageReference imagename =
                      folder.child(titl + imageData.getLastPathSegment());

                  if (validation(is, au, con, pri, titl) == true) {
                    String uuid = UUID.randomUUID().toString();
                    final String uniqueID = uuid.replace("-", "");
                    Bitmap bmp = null;


                    try {
                      bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageData);
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                    
                    try {
                      RotateImage rotate = new RotateImage();
                      bmp = rotate.HandleSamplingAndRotationBitmap(getApplicationContext(), imageData);
                    } catch (Exception e) {
                      System.out.println(e);
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    byte[] data = baos.toByteArray();

                    imagename
                        .putBytes(data)
                        .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                              @Override
                              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imagename
                                    .getDownloadUrl()
                                    .addOnSuccessListener(
                                        new OnSuccessListener<Uri>() {
                                          @Override
                                          public void onSuccess(Uri uri) {
                                            DatabaseReference imagestore =
                                                FirebaseDatabase.getInstance()
                                                    .getReference()
                                                    .child("Books")
                                                    .child(uniqueID);

                                            HashMap<String, String> hash = new HashMap<>();
                                            hash.put("ISBN", is);
                                            hash.put("author", au);
                                            hash.put("category", cat);
                                            hash.put("condition", con);
                                            hash.put("price", pri);
                                            hash.put("sellerName", nam);
                                            hash.put("sellerNumber", num);
                                            hash.put("thumbnail", String.valueOf(uri));
                                            hash.put("title", titl);
                                            hash.put("bookID", uniqueID);
                                            imagestore
                                                .setValue(hash)
                                                .addOnSuccessListener(
                                                    new OnSuccessListener<Void>() {
                                                      @Override
                                                      public void onSuccess(Void aVoid) {
                                                        mProgress.dismiss();
                                                        Toast.makeText(
                                                                SellBook.this,
                                                                "Uploaded",
                                                                Toast.LENGTH_SHORT)
                                                            .show();
                                                        Intent i =
                                                            new Intent(
                                                                SellBook.this, HomeActivity.class);
                                                        startActivity(i);
                                                      }
                                                    });
                                          }
                                        });
                              }
                            });
                  }
                }
              });
        }
      }
    }
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

  public boolean validation(
      String ISBN, String AUTHOR, String CONDITION, String PRICE, String TITLE) {

    boolean valid = true;

    if (!ISBN.matches("^[0-9]*$")) {
      isbn.setError("ISBN must contain numbers only");
      isbn.setFocusable(true);
      valid = false;
    } else {
      if (ISBN.length() == 0) {
        isbn.setError("ISBN must contain a value");
        isbn.setFocusable(true);
        valid = false;
      }
    }

    if (AUTHOR.length() == 0) {
      valid = false;
      author.setError("Author must contain a value");
      author.setFocusable(true);

    } else {
      int len = AUTHOR.length();
      for (int i = 0; i < len; i++) {
        if ((Character.isLetter(AUTHOR.charAt(i)) == false)) {
          valid = false;
          author.setError("Author must contain letters only");
          author.setFocusable(true);
          break;
        }
      }
    }

    if (CONDITION.equals("") || CONDITION.equals(null)) {
      valid = false;
      condition.setError("Please provide a condition");
      condition.setFocusable(true);
    } else {
      int value = Integer.parseInt(CONDITION);
      if (value < 0 || value > 10) {

        valid = false;
        condition.setError("Condition must be between 0 and 10");
        condition.setFocusable(true);
      }
    }

    try {
      double d = Double.parseDouble(PRICE);
    } catch (NumberFormatException nfe) {
      valid = false;
      price.setError("Price must be a digit");
      price.setFocusable(true);
    }

    if (TITLE.length() == 0) {
      valid = false;
      textbookname.setError("Please enter a title");
      textbookname.setFocusable(true);
    }

    return valid;
  }
}
