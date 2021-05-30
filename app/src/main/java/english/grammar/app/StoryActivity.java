package english.grammar.app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import english.grammar.app.SharedData.SharedClass;

public class StoryActivity extends AppCompatActivity {

    private AdView mAdView;
    Typeface alvi_Nastaleeq_Lahori;
    ImageButton backBtn;
    TextView txtDesc, txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        txtDesc=findViewById(R.id.txtDesc);
        txtTitle=findViewById(R.id.txtTitle);

        txtTitle.setText(SharedClass.storyTitle);
        txtDesc.setText(SharedClass.storyDesc);

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        alvi_Nastaleeq_Lahori = Typeface.createFromAsset(this.getAssets(), "fonts/alvi_Nastaleeq_Lahori.ttf");
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void favbtn(View view){
        startActivity(new Intent(getApplicationContext(), FavouriteMessages.class));
    }

}
