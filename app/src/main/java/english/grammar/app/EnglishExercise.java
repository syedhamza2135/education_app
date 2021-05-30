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

public class EnglishExercise extends AppCompatActivity {

    InterstitialAd interstitial;
    private AdView mAdView;
    ImageButton backBtn;
    ListView mainOptionsLV;
    String[] shorttxt = {"ET", "EG"};
    String[] mainOpions = {"English Tenses", "English Grammer"};
    String[] mainOpionsurdu = {"فعل کے زمانے", "انگریزی گرائمر"};
    MainOptionsAdapter mainOptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_exercise);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.interstitial_adunit_id_dictionary));
        interstitial.loadAd(new AdRequest.Builder().build());

        backBtn = findViewById(R.id.backBtn);
        mainOptionsLV = findViewById(R.id.mainOptionsLV);
        mainOptionsAdapter = new MainOptionsAdapter();
        mainOptionsLV.setAdapter(mainOptionsAdapter);
        mainOptionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    showandstart(Tenses.class);
                }
                if (i == 1) {
                    showandstart(PartsOfSpeech.class);
                }
            }
        });

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
            View rowView = View.inflate(EnglishExercise.this, R.layout.exercise_categories_cell, null);

            LinearLayout cell = rowView.findViewById(R.id.cell);
            CardView shortTxtCard = rowView.findViewById(R.id.shortTxtCard);
            CardView mainCard = rowView.findViewById(R.id.mainCard);
            TextView shortTxt = rowView.findViewById(R.id.shortTxt);
            TextView optionname = rowView.findViewById(R.id.optionname);
            TextView optionnameurdu = rowView.findViewById(R.id.optionnameurdu);
            optionname.setText(mainOpions[i]);
            optionnameurdu.setText(mainOpionsurdu[i]);
            shortTxt.setText(shorttxt[i]);
            shortTxt.setTextColor(EnglishExercise.this.getResources().getColor(R.color.maincolor));
            shortTxtCard.setCardBackgroundColor(EnglishExercise.this.getResources().getColor(R.color.white));
            mainCard.setCardBackgroundColor(EnglishExercise.this.getResources().getColor(R.color.maincolor));
            cell.setBackgroundColor(EnglishExercise.this.getResources().getColor(R.color.maincolor));

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
