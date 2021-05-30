package english.grammar.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import english.grammar.app.databinding.PhrasesCellBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import english.grammar.app.Manager.Conversation_DBManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Phrases extends AppCompatActivity {

    private AdView mAdView;
    List<PhrasesViewModel> phrasesData = new ArrayList<>();
    Typeface alvi_Nastaleeq_Lahori;
    Conversation_DBManager dbManager;
    ImageButton backBtn;
    RecyclerView phrasesRV;
    MainOptionsAdapter mainOptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases);

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        dbManager = new Conversation_DBManager(this);
        dbManager.open();
        try {
            dbManager.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        phrasesData = dbManager.getPhrases();

        alvi_Nastaleeq_Lahori = Typeface.createFromAsset(this.getAssets(), "fonts/alvi_Nastaleeq_Lahori.ttf");
        backBtn = findViewById(R.id.backBtn);
        phrasesRV = findViewById(R.id.phrasesRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        phrasesRV.setLayoutManager(linearLayoutManager);
        mainOptionsAdapter = new MainOptionsAdapter(Phrases.this, phrasesData);
        phrasesRV.setAdapter(mainOptionsAdapter);
        phrasesRV.setNestedScrollingEnabled(false);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EnglishLessons.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), EnglishLessons.class));
        finish();
    }

    public class MainOptionsAdapter extends RecyclerView.Adapter<MainOptionsAdapter.MyAdapter> {

        Context context;
        List<PhrasesViewModel> phrasesData;
        private LayoutInflater inflater;

        public MainOptionsAdapter(Context context, List<PhrasesViewModel> phrasesData) {
            this.context = context;
            this.phrasesData = phrasesData;
        }

        @NonNull
        @Override
        public MyAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if (inflater == null) {
                inflater = LayoutInflater.from(viewGroup.getContext());
            }
            PhrasesCellBinding phrasesCellBinding = PhrasesCellBinding.inflate(inflater, viewGroup, false);
            return new MyAdapter(phrasesCellBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter myAdapter, int i) {
            myAdapter.bind(phrasesData.get(i));
        }

        @Override
        public int getItemCount() {
            return phrasesData.size();
        }

        public class MyAdapter extends RecyclerView.ViewHolder {
            private PhrasesCellBinding phrasesCellBinding;

            public MyAdapter(PhrasesCellBinding phrasesCellBinding) {
                super(phrasesCellBinding.getRoot());
                this.phrasesCellBinding = phrasesCellBinding;
            }

            public void bind(PhrasesViewModel phrasesViewModel) {
                this.phrasesCellBinding.setPhrasesViewModel(phrasesViewModel);
            }

            public PhrasesCellBinding getPhrasesCellBinding() {
                return phrasesCellBinding;
            }
        }
    }

    public void favbtn(View view){
        startActivity(new Intent(getApplicationContext(), FavouriteMessages.class));
    }

}
