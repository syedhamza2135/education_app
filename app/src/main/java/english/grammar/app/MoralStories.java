package english.grammar.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import english.grammar.app.Customization.AutoResizeTextView;
import english.grammar.app.Manager.Stories_DBManager;
import english.grammar.app.Models.StoriesModel;
import english.grammar.app.SharedData.SharedClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoralStories extends AppCompatActivity {

    private AdView mAdView;
    List<StoriesModel> phrasesData = new ArrayList<>();
    Typeface alvi_Nastaleeq_Lahori;
    Stories_DBManager dbManager;
    ImageButton backBtn;
    RecyclerView phrasesRV;
    StoriesAdapter mainOptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moral_stories);

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        dbManager = new Stories_DBManager(this);
        dbManager.open();
        try {
            dbManager.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        phrasesData = dbManager.getAllStories();

        alvi_Nastaleeq_Lahori = Typeface.createFromAsset(this.getAssets(), "fonts/alvi_Nastaleeq_Lahori.ttf");
        backBtn = findViewById(R.id.backBtn);
        phrasesRV = findViewById(R.id.phrasesRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        phrasesRV.setLayoutManager(linearLayoutManager);
        mainOptionsAdapter = new StoriesAdapter(MoralStories.this, phrasesData);
        phrasesRV.setAdapter(mainOptionsAdapter);
        phrasesRV.setNestedScrollingEnabled(false);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.MyAdapter> {

        Context context;
        List<StoriesModel> phrasesData;
        private LayoutInflater inflater;

        public StoriesAdapter(Context context, List<StoriesModel> phrasesData) {
            this.context = context;
            this.phrasesData = phrasesData;
        }

        @NonNull
        @Override
        public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (inflater==null){
                inflater=LayoutInflater.from(parent.getContext());
            }
            View view = inflater.inflate(R.layout.stories_cell, parent, false);
            return new MyAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
            holder.optionname.setText(phrasesData.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return phrasesData.size();
        }

        public class MyAdapter extends RecyclerView.ViewHolder {
            AutoResizeTextView optionname;
            public MyAdapter(@NonNull View itemView) {
                super(itemView);
                optionname=itemView.findViewById(R.id.optionname);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedClass.storyTitle = phrasesData.get(getAdapterPosition()).getTitle();
                        SharedClass.storyDesc= phrasesData.get(getAdapterPosition()).getStory();
                        context.startActivity(new Intent(context, StoryActivity.class));
                        finish();
                    }
                });
            }
        }
    }

    public void favbtn(View view){
        startActivity(new Intent(getApplicationContext(), FavouriteMessages.class));
    }

}
