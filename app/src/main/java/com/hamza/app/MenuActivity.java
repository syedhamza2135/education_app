package com.hamza.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hamza.app.dictionary.DictionaryActivity;
import com.hamza.app.firebasedata.Stories;
import com.hamza.app.ocr.MainActivityOcr;
import com.hamza.app.summary.HomeActivity;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout l1, l2, l3, l4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        l1=findViewById(R.id.l1);l1.setOnClickListener(this);
        l2=findViewById(R.id.l2);l2.setOnClickListener(this);
        l3=findViewById(R.id.l3);l3.setOnClickListener(this);
        l4=findViewById(R.id.l4);l4.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.l1:
                Intent i1=new Intent(MenuActivity.this, DictionaryActivity.class);
                startActivity(i1);
                break;
            case R.id.l2:
                Intent i=new Intent(MenuActivity.this, MainActivityOcr.class);
                startActivity(i);
                break;
            case R.id.l3:
                Intent ii=new Intent(MenuActivity.this, HomeActivity.class);
                startActivity(ii);
                break;
            case R.id.l4:
                Intent i4=new Intent(MenuActivity.this, Stories.class);
                startActivity(i4);
                break;

        }
    }
}