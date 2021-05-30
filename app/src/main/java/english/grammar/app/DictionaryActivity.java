package english.grammar.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import english.grammar.app.Adapters.RecentWordsAdapter;
import english.grammar.app.Manager.DBManager;
import english.grammar.app.SharedData.SharedClass;
import english.grammar.app.databinding.WordsCellBinding;

import java.util.ArrayList;
import java.util.List;

public class DictionaryActivity extends AppCompatActivity {

    InterstitialAd interstitial;
    private AdView mAdView;
    List<String> savedwords = new ArrayList<>();
    List<String> savedmeanings = new ArrayList<>();
    EditText searchEdt;
    List<DictionaryViewModel> dictionaryData = new ArrayList<>();
    List<DictionaryViewModel> filterData = new ArrayList<>();
    ImageButton backBtn;
    RecyclerView exampleRV, recentWordsRV;
    ExampleAdapter exampleAdapter;
    Typeface montserratSemiBold;
    DBManager dbManager;
    Context context;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.interstitial_adunit_id_dictionary));
        interstitial.loadAd(new AdRequest.Builder().build());

        context = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        dbManager = new DBManager(this);
        dbManager.open();
        try {
            dbManager.copyDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dictionaryData.size() > 0) {
            dictionaryData.clear();
        }

        montserratSemiBold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        searchEdt = findViewById(R.id.searchEdt);
        backBtn = findViewById(R.id.backBtn);
        exampleRV = findViewById(R.id.exampleRV);
        recentWordsRV = findViewById(R.id.recentWordsRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        exampleRV.setLayoutManager(linearLayoutManager);
        recentWordsRV.setLayoutManager(linearLayoutManager1);
        exampleRV.setNestedScrollingEnabled(false);
        recentWordsRV.setNestedScrollingEnabled(false);
        exampleAdapter = new ExampleAdapter(DictionaryActivity.this);

        try {
            savedwords.addAll(preferences.getStringSet("recentWordsList", null));
            savedmeanings.addAll(preferences.getStringSet("recentMeaningsList", null));
            RecentWordsAdapter recentWordsAdapter = new RecentWordsAdapter(DictionaryActivity.this, savedwords, savedmeanings);
            recentWordsRV.setAdapter(recentWordsAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (dictionaryData.size() > 0) {
                    dictionaryData.clear();
                }
                if (searchEdt.getText().toString().equals("")) {
                    recentWordsRV.setVisibility(View.VISIBLE);
                    exampleRV.setVisibility(View.GONE);
                    dictionaryData.clear();
                    exampleAdapter.notifyDataSetChanged();
                } else {
                    recentWordsRV.setVisibility(View.GONE);
                    exampleRV.setVisibility(View.VISIBLE);
                    dictionaryData = dbManager.filterWordsMeanings(searchEdt.getText().toString());
                    exampleRV.setAdapter(exampleAdapter);
                    exampleAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
            WordsCellBinding wordsCellBinding = WordsCellBinding.inflate(inflater, viewGroup, false);
            return new MyAdapter(wordsCellBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter myAdapter, int i) {
            myAdapter.bind(dictionaryData.get(i));
        }

        @Override
        public int getItemCount() {
            return dictionaryData.size();
        }

        public class MyAdapter extends RecyclerView.ViewHolder {
            private WordsCellBinding wordsCellBinding;

            public MyAdapter(WordsCellBinding wordsCellBinding) {
                super(wordsCellBinding.getRoot());
                this.wordsCellBinding = wordsCellBinding;

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedClass.words.add(dictionaryData.get(getAdapterPosition()).word);
                        SharedClass.meanings.add(dictionaryData.get(getAdapterPosition()).meaning);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putStringSet("recentWordsList", SharedClass.words);
                        editor.putStringSet("recentMeaningsList", SharedClass.meanings);
                        editor.apply();
                        editor.commit();
                        showandstart(WordDetailActivity.class, getAdapterPosition());
                    }
                });
            }

            public void bind(DictionaryViewModel dictionaryViewModel) {
                this.wordsCellBinding.setDictionaryViewModel(dictionaryViewModel);
            }

            public WordsCellBinding getWordsCellBinding() {
                return wordsCellBinding;
            }
        }
    }

    public void favbtn(View view) {
        showandstartone(FavouriteMessages.class);
    }

    public void showandstartone(final Class activity) {
        if (interstitial.isLoaded()) {
            interstitial.show();
        } else {
            startMainActivityone(activity);
        }

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startMainActivityone(activity);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                startMainActivityone(activity);
            }

        });
    }

    public void showandstart(final Class activity, final int pos) {
        startMainActivity(activity, pos);
    }

    private void startMainActivity(Class activity, int pos) {
        Intent intent = new Intent(getApplicationContext(), activity);
        intent.putExtra("word", dictionaryData.get(pos).word);
        intent.putExtra("meaning", dictionaryData.get(pos).meaning);
        startActivity(intent);
        finish();
    }

    private void startMainActivityone(Class activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
        finish();
    }


}
