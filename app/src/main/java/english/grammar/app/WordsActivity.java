package english.grammar.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import english.grammar.app.SharedData.SharedClass;

public class WordsActivity extends AppCompatActivity {

    String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    ImageButton backBtn;
    ListView exampleLV;
    ExampleAdapter exampleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        backBtn = findViewById(R.id.backBtn);
        exampleLV = findViewById(R.id.exampleLV);
        exampleAdapter = new ExampleAdapter();
        exampleLV.setAdapter(exampleAdapter);
        exampleLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedClass.alphabet = words[i];
                startActivity(new Intent(getApplicationContext(), DictionaryActivity.class));
                finish();
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

    public class ExampleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return words.length;
        }

        @Override
        public Object getItem(int i) {
            return words[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = View.inflate(WordsActivity.this, R.layout.words_cell, null);

            TextView wordsTxt = rowView.findViewById(R.id.wordsTxt);

            wordsTxt.setText(words[i]);

            return rowView;
        }
    }

    public void favbtn(View view){
        startActivity(new Intent(getApplicationContext(), FavouriteMessages.class));
    }

}
