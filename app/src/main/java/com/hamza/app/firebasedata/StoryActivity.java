package com.hamza.app.firebasedata;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hamza.app.R;

public class StoryActivity extends AppCompatActivity {


    Typeface alvi_Nastaleeq_Lahori;
    TextView txtDesc, txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        txtDesc=findViewById(R.id.txtDesc);
        txtTitle=findViewById(R.id.txtTitle);

//
        Intent intent = getIntent();
   String title=intent.getStringExtra("title");
   String story=intent.getStringExtra("story");
        txtTitle.setText(title);
        txtDesc.setText(story);
        alvi_Nastaleeq_Lahori = Typeface.createFromAsset(this.getAssets(), "fonts/alvi_Nastaleeq_Lahori.ttf");




    }



}
