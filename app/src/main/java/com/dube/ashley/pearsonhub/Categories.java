package com.dube.ashley.pearsonhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Categories extends AppCompatActivity
{
    List<CategoryHandler> catBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        catBooks=new ArrayList<>();

        /*catBooks.add(new CategoryHandler("Accounting",750,"Bcom","My Description",R.drawable.accounting));
        catBooks.add(new CategoryHandler("Arts and Culture",550,"BA","My Description",R.drawable.arts));
        catBooks.add(new CategoryHandler("English",650,"BA","My Description",R.drawable.english));
        catBooks.add(new CategoryHandler("Mathematics",450,"BSc","My Description",R.drawable.maths));
        catBooks.add(new CategoryHandler("Science",350,"BSc","My Description",R.drawable.science));
        catBooks.add(new CategoryHandler("Business Management",480,"Bcom","My Description",R.drawable.business));
        catBooks.add(new CategoryHandler("Arts and Culture",550,"BA","My Description",R.drawable.arts));
        catBooks.add(new CategoryHandler("English",650,"BA","My Description",R.drawable.english));
        catBooks.add(new CategoryHandler("Mathematics",450,"BSc","My Description",R.drawable.maths));
        catBooks.add(new CategoryHandler("Science",350,"BSc","My Description",R.drawable.science));
        catBooks.add(new CategoryHandler("Business Management",480,"Bcom","My Description",R.drawable.business));*/

        catBooks.add(new CategoryHandler("Accounting","Bcom","My Description",R.drawable.accounting));
        catBooks.add(new CategoryHandler("Arts and Culture","BA","My Description",R.drawable.arts));
        catBooks.add(new CategoryHandler("English","BA","My Description",R.drawable.english));
        catBooks.add(new CategoryHandler("Mathematics","BSc","My Description",R.drawable.maths));
        catBooks.add(new CategoryHandler("Science","BSc","My Description",R.drawable.science));
        catBooks.add(new CategoryHandler("Business Management","Bcom","My Description",R.drawable.business));
        catBooks.add(new CategoryHandler("Arts and Culture","BA","My Description",R.drawable.arts));
        catBooks.add(new CategoryHandler("English","BA","My Description",R.drawable.english));
        catBooks.add(new CategoryHandler("Mathematics","BSc","My Description",R.drawable.maths));
        catBooks.add(new CategoryHandler("Science","BSc","My Description",R.drawable.science));
        catBooks.add(new CategoryHandler("Business Management","Bcom","My Description",R.drawable.business));

        RecyclerView rv=(RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter rva=new RecyclerViewAdapter(this,catBooks);
        rv.setLayoutManager(new GridLayoutManager(this,2));
        rv.setAdapter(rva);


    }
}