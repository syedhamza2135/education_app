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

public class PresentTense extends AppCompatActivity {

    private AdView mAdView;
    InterstitialAd interstitial;
    Conversation_DBManager dbManager;
    ImageButton backBtn;
    ListView mainOptionsLV;
    String[] shorttxt = {"ET", "ET", "ET", "ET"};
    String[] mainOpionsUrdu = {"فعل حال", "فعل حال جاریہ", "فعل حال مکمل", "فعل حال مکمل جاری"};
    MainOptionsAdapter mainOptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_tense);

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.interstitial_adunit_id_present_tense));
        interstitial.loadAd(new AdRequest.Builder().build());

        dbManager = new Conversation_DBManager(this);
        dbManager.open();
        try {
            dbManager.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        backBtn = findViewById(R.id.backBtn);
        mainOptionsLV = findViewById(R.id.mainOptionsLV);
        mainOptionsAdapter = new MainOptionsAdapter();
        mainOptionsLV.setAdapter(mainOptionsAdapter);
        mainOptionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    SharedClass.tense_id = dbManager.getTenses().get(0).getId();
                    showandstart(PresentIndefinateTense.class);
                }
                if (i == 1) {
                    SharedClass.tense_id = dbManager.getTenses().get(1).getId();
                    showandstart(PresentContinuousTense.class);
                }
                if (i == 2) {
                    SharedClass.tense_id = dbManager.getTenses().get(2).getId();
                    showandstart(PresentPerfectTense.class);
                }
                if (i == 3) {
                    SharedClass.tense_id = dbManager.getTenses().get(3).getId();
                    showandstart(PresentPerfectContinuousTense.class);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Tenses.class));
                finish();
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
            View rowView = View.inflate(PresentTense.this, R.layout.tenses_cell, null);

            LinearLayout cell = rowView.findViewById(R.id.cell);
            CardView shortTxtCard = rowView.findViewById(R.id.shortTxtCard);
            CardView mainCard = rowView.findViewById(R.id.mainCard);
            TextView shortTxt = rowView.findViewById(R.id.shortTxt);
            TextView optionname = rowView.findViewById(R.id.optionname);
            TextView opurdu = rowView.findViewById(R.id.opurdu);
            opurdu.setText(mainOpionsUrdu[i]);
            optionname.setText(dbManager.getTensesNames(SharedClass.mainOption).get(i).getName());
            optionname.setTextColor(PresentTense.this.getResources().getColor(R.color.white));
            shortTxt.setText(shorttxt[i]);
            shortTxt.setTextColor(PresentTense.this.getResources().getColor(R.color.maincolor));
            shortTxtCard.setCardBackgroundColor(PresentTense.this.getResources().getColor(R.color.white));
            mainCard.setCardBackgroundColor(PresentTense.this.getResources().getColor(R.color.maincolor));
            cell.setBackgroundColor(PresentTense.this.getResources().getColor(R.color.maincolor));

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
