package com.hamza.app.dictionary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamza.app.R;

import java.util.List;



public class RecentWordsAdapter extends RecyclerView.Adapter<RecentWordsAdapter.MyAdapter> {

    Context context;
    List<String> words;
    List<String> meanings;
    private LayoutInflater inflater;

    public RecentWordsAdapter(Context context, List<String> words, List<String> meanings) {
        this.context = context;
        this.words = words;
        this.meanings = meanings;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        View view = inflater.inflate(R.layout.recent_words_cell, viewGroup, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter myAdapter, int i) {
        myAdapter.wordsTxt.setText(words.get(i));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView wordsTxt;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            wordsTxt = itemView.findViewById(R.id.wordsTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, WordDetailActivity.class);
//                    intent.putExtra("word", words.get(getAdapterPosition()));
//                    intent.putExtra("meaning", meanings.get(getAdapterPosition()));
//                    context.startActivity(intent);
//                    ((Activity) context).finish();
                }
            });

        }
    }
}
