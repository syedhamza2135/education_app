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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import english.grammar.app.Manager.Quotes_DBManager;
import english.grammar.app.Models.QuotesModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FavouriteMessages extends AppCompatActivity {

    private AdView mAdView;
    List<QuotesModel> quotesData = new ArrayList<>();
    Quotes_DBManager dbManager;
    ImageButton backBtn;
    RecyclerView messageRV;
    ExampleAdapter exampleAdapter;
    private TextToSpeech textToSpeech;
    TextView titletxt, noRecord;

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
        quotesData = dbManager.getFavQuote();

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
        titletxt = findViewById(R.id.titletxt);
        noRecord = findViewById(R.id.noRecord);
        backBtn = findViewById(R.id.backBtn);
        messageRV = findViewById(R.id.messageRV);
        messageRV.setLayoutManager(new LinearLayoutManager(this));
        titletxt.setText("Favourite Messsages");
        exampleAdapter = new ExampleAdapter(FavouriteMessages.this);
        messageRV.setAdapter(exampleAdapter);

        if (dbManager.getFavQuote().size() == 0) {
            noRecord.setVisibility(View.VISIBLE);
            messageRV.setVisibility(View.GONE);
        }

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
            myAdapter.msgTxt.setText(quotesData.get(i).getEng_quote());
            myAdapter.urduMeaningTxt.setText(quotesData.get(i).getAuthor());
            myAdapter.favouriteBtn.setBackgroundResource(R.drawable.favorite_heart_button);
        }

        @Override
        public int getItemCount() {
            return quotesData.size();
        }

        public class MyAdapter extends RecyclerView.ViewHolder {
            TextView dateTxt, msgTxt, urduMeaningTxt;
            ImageButton speakBtn, favouriteBtn;

            public MyAdapter(@NonNull View itemView) {
                super(itemView);
                dateTxt = itemView.findViewById(R.id.dateTxt);
                msgTxt = itemView.findViewById(R.id.msgTxt);
                urduMeaningTxt = itemView.findViewById(R.id.urduMeaningTxt);
                speakBtn = itemView.findViewById(R.id.speakBtn);
                favouriteBtn = itemView.findViewById(R.id.favouriteBtn);

                favouriteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        favouriteBtn.setBackgroundResource(R.drawable.favorite);
                        dbManager.updateMessageFav(quotesData.get(getAdapterPosition()).getId(), "0");
                        quotesData = dbManager.getFavQuote();
                        notifyDataSetChanged();
                        if (dbManager.getFavQuote().size() == 0) {
                            noRecord.setVisibility(View.VISIBLE);
                            messageRV.setVisibility(View.GONE);
                        }
                    }
                });

                speakBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textToSpeech.speak(quotesData.get(getAdapterPosition()).getEng_quote(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(FavouriteMessages.this, quotesData.get(getAdapterPosition()).getFav() + "", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }

    public void favbtn(View view){
        startActivity(new Intent(getApplicationContext(), FavouriteMessages.class));
    }

}
