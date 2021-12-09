package com.hamza.app.firebasedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamza.app.R;
import com.hamza.app.firebasedata.model.StoriesModel;

import java.util.ArrayList;
import java.util.List;

public class Stories extends AppCompatActivity {

    private List<StoriesModel> stories=new ArrayList<>();
    private RecyclerView phrasesRV;
  private  StoriesAdapter mainOptionsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        phrasesRV=findViewById(R.id.recycler1);
    firebaseExtractData();


}


    public void  firebaseExtractData(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("stories");
       // DatabaseReference myRef = database.child("stories");

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                user = dataSnapshot.getValue(User.class);

                Log.e("data",dataSnapshot.toString()) ;
                stories.clear();

                for(DataSnapshot db:dataSnapshot.getChildren()){
                    StoriesModel storiesS=db.getValue(StoriesModel.class);
                    stories.add(storiesS);
                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Stories.this);
                phrasesRV.setLayoutManager(linearLayoutManager);
                mainOptionsAdapter = new StoriesAdapter(Stories.this, stories);
                phrasesRV.setAdapter(mainOptionsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

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
            View view = inflater.inflate(R.layout.stories_cardview, parent, false);
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
            TextView optionname;
            public MyAdapter(@NonNull View itemView) {
                super(itemView);
                optionname=itemView.findViewById(R.id.optionname);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        SharedClass.storyTitle = phrasesData.get(getAdapterPosition()).getTitle();
//                        SharedClass.storyDesc= phrasesData.get(getAdapterPosition()).getStory();
                      Intent i=  new Intent(context, StoryActivity.class);
                        String storyActivityTitle=phrasesData.get(getAdapterPosition()).getTitle();
                        String storyActivityStory=phrasesData.get(getAdapterPosition()).getStory();
                        i.putExtra("title",storyActivityTitle);
                        i.putExtra("story",storyActivityStory);
                        context.startActivity(i);
//
                    }
                });
            }
        }
    }

}