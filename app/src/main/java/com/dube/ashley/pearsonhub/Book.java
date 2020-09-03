package com.dube.ashley.pearsonhub;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Book extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Book Name Here");


    }
}