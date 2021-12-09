package com.hamza.app.english;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import java.io.IOException;

import com.hamza.app.dictionary.SharedClass;
import com.hamza.app.english.Conversation_DBManager;


public class Tenses extends AppCompatActivity {


    ListView mainOptionsLV;
    String[] shorttxt = {"FT", "PT", "FT"};
    String[] mainOpions = {"Present Tense", "Past Tense", "Future Tense"};
    String[] mainOpionsUrdu = {"فعل حال", "فعل ماضی", "فعل مستقبل"};
    Integer[] colors = {R.color.white, R.color.white, R.color.white};
    MainOptionsAdapter mainOptionsAdapter;
    Conversation_DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenses);


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
                SharedClass.mainOption = mainOpions[i];
                if (i == 0) {
                    showandstart(PresentTense.class);
                }
                if (i == 1) {
                    showandstart(PastTense.class);
                }
                if (i == 2) {
                    showandstart(FutureTense.class);
                }
            }
        });



    }



    public class MainOptionsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mainOpions.length;
        }

        @Override
        public Object getItem(int i) {
            return mainOpions[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View rowView = inflater.inflate(R.layout.tenses_cell, viewGroup, false);

            LinearLayout cell = rowView.findViewById(R.id.cell);
            CardView shortTxtCard = rowView.findViewById(R.id.shortTxtCard);
            CardView mainCard = rowView.findViewById(R.id.mainCard);
            TextView shortTxt = rowView.findViewById(R.id.shortTxt);
            TextView optionname = rowView.findViewById(R.id.optionname);
            TextView opurdu = rowView.findViewById(R.id.opurdu);
            opurdu.setText(mainOpionsUrdu[i]);
            optionname.setText(mainOpions[i]);
            optionname.setTextColor(Tenses.this.getResources().getColor(R.color.white));
            shortTxt.setText(shorttxt[i]);
            shortTxt.setTextColor(Tenses.this.getResources().getColor(R.color.purple_500));
            shortTxtCard.setCardBackgroundColor(Tenses.this.getResources().getColor(colors[i]));
//            mainCard.setCardBackgroundColor(Tenses.this.getResources().getColor(R.color.purple_500));
            cell.setBackgroundColor(Tenses.this.getResources().getColor(R.color.purple_500));

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
