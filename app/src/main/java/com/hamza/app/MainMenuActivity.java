package com.hamza.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hamza.app.dictionary.DictionaryActivity;
import com.hamza.app.english.EnglishExercise;
import com.hamza.app.firebasedata.QuoteActivity;
import com.hamza.app.firebasedata.Stories;
import com.hamza.app.firebasedata.StoryActivity;
import com.hamza.app.ocr.MainActivityOcr;
import com.hamza.app.spellchecker.SpellCheckerActivity;
import com.hamza.app.summary.HomeActivity;

public class MainMenuActivity extends AppCompatActivity {

    RecyclerView mainOptionsLV;
    private String currentTitle = "English Learning";

    String[] mainOpions = {"Dictionary", "Text Recognition", "Summarizer", "Spell Checker", "Moral Stories", "Quotes", "English Lesson"};
    String[] mainOpionsUrdu = {"لغت", "متن کی شناخت", "خلاصہ کرنے والا", "ہجے چیکر", "اخلاقی کہانیاں", "اقتباسات", "انگریزی سبق"};
    String[] shorttxt = {"D", "T", "S", "SC", "MS", "Q", "EL"};
    MainOptionsAdapter mainOptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().setTitle(currentTitle);
        mainOptionsLV = findViewById(R.id.mainOptionsLV);
        mainOptionsLV.setLayoutManager(new GridLayoutManager(this, 2));
        mainOptionsAdapter = new MainOptionsAdapter();
        mainOptionsLV.setAdapter(mainOptionsAdapter);

    }

    public void showandstart(final Class activity) {

        startMainActivity(activity);


    }

    private void startMainActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
//        finish();
    }


    public class MainOptionsAdapter extends RecyclerView.Adapter<MainOptionsAdapter.MyAdapter> {

        private LayoutInflater inflater;

        @NonNull
        @Override
        public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }
            View view = inflater.inflate(R.layout.activity_main_menu_cell, parent, false);
            return new MyAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
            holder.optionname.setText(mainOpions[position]);
            holder.optionnameurdu.setText(mainOpionsUrdu[position]);
            holder.shortTxt.setText(shorttxt[position]);
//            holder.shortTxtCard.setCardBackgroundColor(MainMenuActivity.this.getResources().getColor(shorttxtcolor[position]));
        }

        @Override
        public int getItemCount() {
            return mainOpions.length;
        }

        public class MyAdapter extends RecyclerView.ViewHolder {

            LinearLayout cell;
            TextView optionname, optionnameurdu, shortTxt;
            CardView shortTxtCard;

            public MyAdapter(@NonNull View itemView) {
                super(itemView);

                cell = itemView.findViewById(R.id.cell);
                optionname = itemView.findViewById(R.id.optionname);
                optionnameurdu = itemView.findViewById(R.id.optionnameurdu);
                shortTxt = itemView.findViewById(R.id.shortTxt);
                shortTxtCard = itemView.findViewById(R.id.shortTxtCard);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (getAdapterPosition()) {
                            case 0:
                                showandstart(DictionaryActivity.class);

                                break;
                            case 1:
                                showandstart(MainActivityOcr.class);
                                break;
                            case 2:

                                showandstart(HomeActivity.class);
                                break;
                            case 3:
                                showandstart(SpellCheckerActivity.class);

                                break;
                            case 4:
                                showandstart(Stories.class);
                                break;
                            case 5:
                                showandstart(QuoteActivity.class);
                                break;
                            case 6:
                                showandstart(EnglishExercise.class);
                                break;
                        }
                    }
                });

            }
        }
    }


}
