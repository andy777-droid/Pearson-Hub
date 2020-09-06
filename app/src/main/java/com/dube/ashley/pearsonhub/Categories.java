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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Categories extends AppCompatActivity
{
    List<CategoryHandler> catBooks;
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        RecyclerView v = (RecyclerView) findViewById(R.id.recyclerview_id);
        v.setNestedScrollingEnabled(false);
        mdrawer = (DrawerLayout) findViewById(R.id.id_drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);

        mdrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        getSupportActionBar().setElevation(0);

        catBooks=new ArrayList<>();

        catBooks.add(new CategoryHandler("Accounting",750,"Bcom","My Description",R.drawable.accounting));
        catBooks.add(new CategoryHandler("Arts and Culture",550,"BA","My Description",R.drawable.arts));
        catBooks.add(new CategoryHandler("English",650,"BA","My Description",R.drawable.english));
        catBooks.add(new CategoryHandler("Mathematics",450,"BSc","My Description",R.drawable.maths));
        catBooks.add(new CategoryHandler("Science",350,"BSc","My Description",R.drawable.science));
        catBooks.add(new CategoryHandler("Business Management",480,"Bcom","My Description",R.drawable.business));
        catBooks.add(new CategoryHandler("Arts and Culture",550,"BA","My Description",R.drawable.arts));
        catBooks.add(new CategoryHandler("English",650,"BA","My Description",R.drawable.english));
        catBooks.add(new CategoryHandler("Mathematics",450,"BSc","My Description",R.drawable.maths));
        catBooks.add(new CategoryHandler("Science",350,"BSc","My Description",R.drawable.science));
        catBooks.add(new CategoryHandler("Business Management",480,"Bcom","My Description",R.drawable.business));


        RecyclerView rv=(RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter rva=new RecyclerViewAdapter(this,catBooks);
        rv.setLayoutManager(new GridLayoutManager(this,2));
        rv.setAdapter(rva);

        mToggle.setDrawerArrowDrawable(new Categories.HamburgerDrawable(this));

    }
    public class HamburgerDrawable extends DrawerArrowDrawable
    {

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


}