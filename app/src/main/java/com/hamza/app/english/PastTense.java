package com.hamza.app.english;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.hamza.app.R;
import com.hamza.app.dictionary.SharedClass;

import java.io.IOException;


public class PastTense extends AppCompatActivity {


    Conversation_DBManager dbManager;

    ListView mainOptionsLV;
    String[] shorttxt = {"ET", "ET", "ET", "ET"};
    String[] mainOpionsUrdu = {"فعل ماضی", "فعل ماضی جاریہ", "فعل ماضی مکمل", "فعل ماضی مکمل جاری"};
    MainOptionsAdapter mainOptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_tense);

        dbManager = new Conversation_DBManager(this);
        dbManager.open();
        try {
            dbManager.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }


        mainOptionsLV = findViewById(R.id.mainOptionsLV);
        mainOptionsAdapter = new MainOptionsAdapter();
        mainOptionsLV.setAdapter(mainOptionsAdapter);
        mainOptionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    SharedClass.tense_id = dbManager.getTenses().get(4).getId();
                    showandstart(PastIndefinateTense.class);
                }
                if (i == 1) {
                    SharedClass.tense_id = dbManager.getTenses().get(5).getId();
                    showandstart(PastContinuousTense.class);
                }
                if (i == 2) {
                    SharedClass.tense_id = dbManager.getTenses().get(6).getId();
                    showandstart(PastPerfectTense.class);
                }
                if (i == 3) {
                    SharedClass.tense_id = dbManager.getTenses().get(7).getId();
                    showandstart(PastPerfectContinuousTense.class);
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Tenses.class));
        finish();
    }

    public class MainOptionsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dbManager.getTensesNames(SharedClass.mainOption).size();
        }

        @Override
        public Object getItem(int i) {
            return dbManager.getTensesNames(SharedClass.mainOption).get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = View.inflate(PastTense.this, R.layout.tenses_cell, null);

            LinearLayout cell = rowView.findViewById(R.id.cell);
            CardView shortTxtCard = rowView.findViewById(R.id.shortTxtCard);
            CardView mainCard = rowView.findViewById(R.id.mainCard);
            TextView shortTxt = rowView.findViewById(R.id.shortTxt);
            TextView optionname = rowView.findViewById(R.id.optionname);
            TextView opurdu = rowView.findViewById(R.id.opurdu);
            opurdu.setText(mainOpionsUrdu[i]);
            optionname.setText(dbManager.getTensesNames(SharedClass.mainOption).get(i).getName());
            optionname.setTextColor(PastTense.this.getResources().getColor(R.color.white));
            shortTxt.setText(shorttxt[i]);
            shortTxt.setTextColor(PastTense.this.getResources().getColor(R.color.purple_500));
            shortTxtCard.setCardBackgroundColor(PastTense.this.getResources().getColor(R.color.white));
            mainCard.setCardBackgroundColor(PastTense.this.getResources().getColor(R.color.purple_500));
            cell.setBackgroundColor(PastTense.this.getResources().getColor(R.color.purple_500));

            return rowView;
        }
    }


    public void showandstart(final Class activity){

            startMainActivity(activity);

    }

    private void startMainActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }

}
