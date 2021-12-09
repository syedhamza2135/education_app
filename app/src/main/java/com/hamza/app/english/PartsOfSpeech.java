package com.hamza.app.english;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.hamza.app.R;
import com.hamza.app.dictionary.SharedClass;

import java.io.IOException;


public class PartsOfSpeech extends AppCompatActivity {


    ListView mainOptionsLV;
    Conversation_DBManager dbManager;
    String[] shorttxt = {"ET", "EG"};
    MainOptionsAdapter mainOptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts_speech);
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
                SharedClass.tense_id = dbManager.getPartsOfSpeech().get(i).getId();
                showandstart(PartOfSpeech_Detail.class);
                Toast.makeText(PartsOfSpeech.this, "here", Toast.LENGTH_SHORT).show();
            }
        });


    }


    public class MainOptionsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dbManager.getPartsOfSpeech().size();
        }

        @Override
        public Object getItem(int i) {
            return dbManager.getPartsOfSpeech().get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = View.inflate(PartsOfSpeech.this, R.layout.partofspeech_cell, null);

            LinearLayout cell = rowView.findViewById(R.id.cell);
            CardView shortTxtCard = rowView.findViewById(R.id.shortTxtCard);
            CardView mainCard = rowView.findViewById(R.id.mainCard);
            TextView shortTxt = rowView.findViewById(R.id.shortTxt);
            TextView optionname = rowView.findViewById(R.id.optionname);
            TextView optionnameurdu = rowView.findViewById(R.id.optionnameurdu);
            optionname.setText(dbManager.getPartsOfSpeech().get(i).getName_eng());
            optionnameurdu.setText(dbManager.getPartsOfSpeech().get(i).getName_urdu());
            optionname.setTextColor(PartsOfSpeech.this.getResources().getColor(R.color.purple_500));
            optionnameurdu.setTextColor(PartsOfSpeech.this.getResources().getColor(R.color.purple_500));
            shortTxt.setText(shorttxt[0]);
            shortTxt.setTextColor(PartsOfSpeech.this.getResources().getColor(R.color.white));
            shortTxtCard.setCardBackgroundColor(PartsOfSpeech.this.getResources().getColor(R.color.purple_500));
            mainCard.setCardBackgroundColor(PartsOfSpeech.this.getResources().getColor(R.color.white));
            cell.setBackgroundColor(PartsOfSpeech.this.getResources().getColor(R.color.white));

            return rowView;
        }
    }


    public void showandstart(final Class activity) {
        startMainActivity(activity);
    }


    private void startMainActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }

}
