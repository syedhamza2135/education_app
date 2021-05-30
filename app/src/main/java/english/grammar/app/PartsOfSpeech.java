package english.grammar.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import english.grammar.app.Manager.Conversation_DBManager;
import english.grammar.app.SharedData.SharedClass;

import java.io.IOException;

public class PartsOfSpeech extends AppCompatActivity {

    InterstitialAd interstitial;
    private AdView mAdView;
    ImageButton backBtn;
    ListView mainOptionsLV;
    Conversation_DBManager dbManager;
    String[] shorttxt = {"ET", "EG"};
    MainOptionsAdapter mainOptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts_speech);

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.interstitial_adunit_id_pos));
        interstitial.loadAd(new AdRequest.Builder().build());

        dbManager = new Conversation_DBManager(this);
        dbManager.open();
        try {
            dbManager.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        backBtn = findViewById(R.id.backBtn);
        mainOptionsLV = findViewById(R.id.mainOptionsLV);
        mainOptionsAdapter = new MainOptionsAdapter();
        mainOptionsLV.setAdapter(mainOptionsAdapter);
        mainOptionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedClass.tense_id = dbManager.getPartsOfSpeech().get(i).getId();
                showandstart(PartOfSpeech_Detail.class);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EnglishExercise.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), EnglishExercise.class));
        finish();
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
            View rowView = View.inflate(PartsOfSpeech.this, R.layout.pos_cell, null);

            LinearLayout cell = rowView.findViewById(R.id.cell);
            CardView shortTxtCard = rowView.findViewById(R.id.shortTxtCard);
            CardView mainCard = rowView.findViewById(R.id.mainCard);
            TextView shortTxt = rowView.findViewById(R.id.shortTxt);
            TextView optionname = rowView.findViewById(R.id.optionname);
            TextView optionnameurdu = rowView.findViewById(R.id.optionnameurdu);
            optionname.setText(dbManager.getPartsOfSpeech().get(i).getName_eng());
            optionnameurdu.setText(dbManager.getPartsOfSpeech().get(i).getName_urdu());
            optionname.setTextColor(PartsOfSpeech.this.getResources().getColor(R.color.maincolor));
            optionnameurdu.setTextColor(PartsOfSpeech.this.getResources().getColor(R.color.maincolor));
            shortTxt.setText(shorttxt[0]);
            shortTxt.setTextColor(PartsOfSpeech.this.getResources().getColor(R.color.white));
            shortTxtCard.setCardBackgroundColor(PartsOfSpeech.this.getResources().getColor(R.color.maincolor));
            mainCard.setCardBackgroundColor(PartsOfSpeech.this.getResources().getColor(R.color.white));
            cell.setBackgroundColor(PartsOfSpeech.this.getResources().getColor(R.color.white));

            return rowView;
        }
    }

    public void favbtn(View view){
        showandstart(FavouriteMessages.class);
    }

    public void showandstart(final Class activity){
        if (interstitial.isLoaded()) {
            interstitial.show();
        } else {
            startMainActivity(activity);
        }

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startMainActivity(activity);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                startMainActivity(activity);
            }

        });
    }

    private void startMainActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }

}
