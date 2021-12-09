package com.hamza.app.english;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hamza.app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.hamza.app.dictionary.SharedClass;
import com.hamza.app.english.ExampleModel;


public class PartOfSpeech_Detail extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    List<ExampleModel> exampleData = new ArrayList<>();

//    TextView titleTxt;
    Conversation_DBManager dbManager;
    Typeface alvi_Nastaleeq_Lahori;
    TextView engDeftxt, urduDeftxt, methodEnglish, methodUrdu;
    ImageButton defBtn, exBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_of_speech_detail);

        initExample();
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Log.d("msg", "failed");
                }
            }
        });

        defBtn = findViewById(R.id.defBtn);
        exBtn = findViewById(R.id.exBtn);

        alvi_Nastaleeq_Lahori = Typeface.createFromAsset(this.getAssets(), "fonts/alvi_Nastaleeq_Lahori.ttf");


        dbManager = new Conversation_DBManager(this);
        dbManager.open();
        try {
            dbManager.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        titleTxt = findViewById(R.id.titleTxt);
        methodUrdu = findViewById(R.id.methodUrdu);
        methodEnglish = findViewById(R.id.methodEnglish);
        engDeftxt = findViewById(R.id.engDeftxt);
        urduDeftxt = findViewById(R.id.urduDeftxt);
        urduDeftxt.setTypeface(alvi_Nastaleeq_Lahori);
        engDeftxt.setText(dbManager.getPartsOfSpeech(SharedClass.tense_id).get(0).getDef_eng());
        urduDeftxt.setText(dbManager.getPartsOfSpeech(SharedClass.tense_id).get(0).getDef_urdu());
        methodEnglish.setText(dbManager.getPartsOfSpeech(SharedClass.tense_id).get(0).getEg_eng());
//        titleTxt.setText(dbManager.getPartsOfSpeech(SharedClass.tense_id).get(0).getName_eng());

        defBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(dbManager.getPartsOfSpeech(SharedClass.tense_id).get(0).getDef_eng(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        exBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(dbManager.getPartsOfSpeech(SharedClass.tense_id).get(0).getEg_eng(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });



    }

    //  WHEN ACTIVITY IS PAUSED
    @Override
    protected void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
        super.onPause();
    }

    //  WHEN ACTIVITY IS DESTROYED
    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void initExample() {
        ExampleModel exampleModel = new ExampleModel("Example", "Urdu Meaning");
        exampleData.add(exampleModel);
        ExampleModel exampleModel1 = new ExampleModel("Example", "Urdu Meaning");
        exampleData.add(exampleModel1);
        ExampleModel exampleModel2 = new ExampleModel("Example", "Urdu Meaning");
        exampleData.add(exampleModel2);
        ExampleModel exampleModel3 = new ExampleModel("Example", "Urdu Meaning");
        exampleData.add(exampleModel3);
    }



    public class ExampleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return exampleData.size();
        }

        @Override
        public Object getItem(int i) {
            return exampleData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = View.inflate(PartOfSpeech_Detail.this, R.layout.example_cell, null);

            TextView exampleTxt = rowView.findViewById(R.id.exampleTxt);
            TextView meaningTxt = rowView.findViewById(R.id.meaningTxt);

            exampleTxt.setText(exampleData.get(i).getExample());
            meaningTxt.setText(exampleData.get(i).getUrduMeaning());

            return rowView;
        }
    }


}
