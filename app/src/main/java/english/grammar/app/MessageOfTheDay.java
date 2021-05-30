package english.grammar.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import english.grammar.app.Manager.Quotes_DBManager;

import java.io.IOException;
import java.util.Locale;

public class MessageOfTheDay extends AppCompatActivity {

    private AdView mAdView;
    Quotes_DBManager dbManager;
    ImageButton backBtn;
    RecyclerView messageRV;
    ExampleAdapter exampleAdapter;
    private TextToSpeech textToSpeech;
    int fav_check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_of_day);

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        dbManager = new Quotes_DBManager(this);
        dbManager.open();
        try {
            dbManager.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        backBtn = findViewById(R.id.backBtn);
        messageRV = findViewById(R.id.messageRV);
        messageRV.setLayoutManager(new LinearLayoutManager(this));
        exampleAdapter = new ExampleAdapter(MessageOfTheDay.this);
        messageRV.setAdapter(exampleAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.MyAdapter> {

        Context context;
        private LayoutInflater inflater;

        public ExampleAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public MyAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if (inflater == null) {
                inflater = LayoutInflater.from(viewGroup.getContext());
            }
            View view = inflater.inflate(R.layout.message_custom_cell, viewGroup, false);
            return new MyAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter myAdapter, int i) {
            myAdapter.msgTxt.setText(dbManager.getQuote().get(i).getEng_quote());
            myAdapter.urduMeaningTxt.setText(dbManager.getQuote().get(i).getAuthor());
            if (dbManager.getQuote().get(i).getFav().equals("1")) {
                myAdapter.favouriteBtn.setBackgroundResource(R.drawable.favorite_heart_button);
            } else {
                myAdapter.favouriteBtn.setBackgroundResource(R.drawable.favorite);
            }
        }

        @Override
        public int getItemCount() {
            return dbManager.getQuote().size();
        }

        public class MyAdapter extends RecyclerView.ViewHolder {
            TextView dateTxt, msgTxt, urduMeaningTxt;
            ImageButton speakBtn, favouriteBtn, shareBtn;
            LinearLayout favLayout;

            public MyAdapter(@NonNull View itemView) {
                super(itemView);
                dateTxt = itemView.findViewById(R.id.dateTxt);
                msgTxt = itemView.findViewById(R.id.msgTxt);
                urduMeaningTxt = itemView.findViewById(R.id.urduMeaningTxt);
                speakBtn = itemView.findViewById(R.id.speakBtn);
                favouriteBtn = itemView.findViewById(R.id.favouriteBtn);
                favLayout = itemView.findViewById(R.id.favLayout);
                shareBtn = itemView.findViewById(R.id.shareBtn);

                shareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            String shareMessage = dbManager.getQuote().get(getAdapterPosition()).getEng_quote()+"\n\n"+dbManager.getQuote().get(getAdapterPosition()).getAuthor();
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                        }
                    }
                });

                favouriteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fav_check == 0) {
                            favouriteBtn.setBackgroundResource(R.drawable.favorite_heart_button);
                            fav_check = 1;
                            dbManager.updateMessageFav(dbManager.getQuote().get(getAdapterPosition()).getId(), "1");
                        } else {
                            favouriteBtn.setBackgroundResource(R.drawable.favorite);
                            fav_check = 0;
                            dbManager.updateMessageFav(dbManager.getQuote().get(getAdapterPosition()).getId(), "0");
                        }
                    }
                });

                speakBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textToSpeech.speak(dbManager.getQuote().get(getAdapterPosition()).getEng_quote(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                });

            }
        }
    }

    public void favbtn(View view){
        startActivity(new Intent(getApplicationContext(), FavouriteMessages.class));
    }

}
