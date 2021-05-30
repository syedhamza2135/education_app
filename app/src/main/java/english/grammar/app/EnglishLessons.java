package english.grammar.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class EnglishLessons extends AppCompatActivity {

    private AdView mAdView;
    InterstitialAd interstitial;
    ImageButton backBtn;
    RecyclerView mainOptionsLV;
    String[] shorttxt = {"EC", "EP"};
    String[] mainOpions = {"English Conversation", "English Phrases"};
    String[] mainOpionsurdu = {"انگریزی میں بات چیت", "انگریزی جملے"};
    MainOptionsAdapter mainOptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_lessons);
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
        mainOptionsLV.setLayoutManager(new LinearLayoutManager(this));
        mainOptionsAdapter = new MainOptionsAdapter();
        mainOptionsLV.setAdapter(mainOptionsAdapter);

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

    public class MainOptionsAdapter extends RecyclerView.Adapter<MainOptionsAdapter.MyAdapter> {

        private LayoutInflater inflater;

        @NonNull
        @Override
        public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (inflater==null){
                inflater=LayoutInflater.from(parent.getContext());
            }
            View view = inflater.inflate(R.layout.categories_celltwo, parent, false);
            return new MyAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
            holder.optionname.setText(mainOpions[position]);
            holder.optionnameurdu.setText(mainOpionsurdu[position]);
            holder.shortTxt.setText(shorttxt[position]);
            holder.shortTxtCard.setCardBackgroundColor(EnglishLessons.this.getResources().getColor(R.color.white));
        }

        @Override
        public int getItemCount() {
            return mainOpions.length;
        }

        public class MyAdapter extends RecyclerView.ViewHolder {
            LinearLayout cell;
            CardView shortTxtCard, mainCard;
            TextView shortTxt, optionname, optionnameurdu;

            public MyAdapter(@NonNull View itemView) {
                super(itemView);

                cell = itemView.findViewById(R.id.cell);
                shortTxtCard = itemView.findViewById(R.id.shortTxtCard);
                mainCard = itemView.findViewById(R.id.mainCard);
                shortTxt = itemView.findViewById(R.id.shortTxt);
                optionname = itemView.findViewById(R.id.optionname);
                optionnameurdu = itemView.findViewById(R.id.optionnameurdu);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getAdapterPosition() == 0) {
                            showandstart(ConversationsList.class);
                        } else if (getAdapterPosition() == 1) {
                            showandstart(Phrases.class);
                        } else if (getAdapterPosition() == 2) {
                            showandstart(VocabularyWords.class);
                        }
                    }
                });

            }
        }
    }

    public void favbtn(View view) {
        showandstart(FavouriteMessages.class);
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
