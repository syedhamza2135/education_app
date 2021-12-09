package com.hamza.app.firebasedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamza.app.R;
import com.hamza.app.firebasedata.model.QuoteModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuoteActivity extends AppCompatActivity {

    private List<QuoteModel> stories=new ArrayList<>();

    private RecyclerView phrasesRV;
    private QuoteAdapter mainOptionsAdapter;
    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_message_of_day);
            phrasesRV=findViewById(R.id.messageRV);
            firebaseExtractData();
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


        }


        public void  firebaseExtractData(){
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("quotes");
            // DatabaseReference myRef = database.child("stories");

            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                user = dataSnapshot.getValue(User.class);

                    Log.e("data",dataSnapshot.toString()) ;
                    stories.clear();

                    for(DataSnapshot db:dataSnapshot.getChildren()){
                        QuoteModel storiesS=db.getValue(QuoteModel.class);
                        stories.add(storiesS);
                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QuoteActivity.this);
                    phrasesRV.setLayoutManager(linearLayoutManager);
                    mainOptionsAdapter = new QuoteAdapter(QuoteActivity.this, stories);
                    phrasesRV.setAdapter(mainOptionsAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}

            });

        }



        public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.MyAdapter> {

            Context context;
            List<QuoteModel> phrasesData;
            private LayoutInflater inflater;

            public QuoteAdapter(Context context, List<QuoteModel> phrasesData) {
                this.context = context;
                this.phrasesData = phrasesData;
            }

            @NonNull
            @Override
            public QuoteAdapter.MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (inflater==null){
                    inflater=LayoutInflater.from(parent.getContext());
                }
                View view = inflater.inflate(R.layout.message_custom_cell, parent, false);
                return new QuoteAdapter.MyAdapter(view);
            }

            @Override
            public void onBindViewHolder(@NonNull QuoteAdapter.MyAdapter holder, int position) {
                holder.quoteauthor.setText(phrasesData.get(position).getAuthor());
                holder.optionname.setText(phrasesData.get(position).getEng_quote());
            }

            @Override
            public int getItemCount() {
                return phrasesData.size();
            }

            public class MyAdapter extends RecyclerView.ViewHolder {
                TextView optionname,quoteauthor;
                ImageButton imgSpk;

                public MyAdapter(@NonNull View itemView) {
                    super(itemView);
                    optionname=itemView.findViewById(R.id.msgTxt);
                    quoteauthor=itemView.findViewById(R.id.urduMeaningTxt);
                    imgSpk=itemView.findViewById(R.id.speakBtn);
                    imgSpk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String storyActivityStory=phrasesData.get(getAdapterPosition()).getEng_quote();
                            String storyActivityTitle=phrasesData.get(getAdapterPosition()).getAuthor();
//                            String data = wordTxt.getText().toString();
                            textToSpeech.speak(storyActivityStory+ " by "+storyActivityTitle, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    });

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                        SharedClass.storyTitle = phrasesData.get(getAdapterPosition()).getTitle();
//                        SharedClass.storyDesc= phrasesData.get(getAdapterPosition()).getStory();
//                            Intent i=  new Intent(context, StoryActivity.class);
//                            String storyActivityTitle=phrasesData.get(getAdapterPosition()).getAuthor();
//                            String storyActivityStory=phrasesData.get(getAdapterPosition()).getEng_quote();
//                            i.putExtra("title",storyActivityTitle);
//                            i.putExtra("story",storyActivityStory);
//                            context.startActivity(i);
//
                        }
                    });
                }
            }
        }


}