package english.grammar.app;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdListener;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import english.grammar.app.Manager.Quotes_DBManager;
import english.grammar.app.Receiver.Notification_receiver;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    CardView favCard;
    InterstitialAd interstitial;
    SharedPreferences preferences;
    int flag = 0;
    int alarm_status = 0;
    DrawerLayout drawerLayout;
    private AdView mAdView, adViewone;
    FrameLayout splashLayout;
    RecyclerView mainOptionsLV;
    ListView optionsLV;
    private String currentTitle = "Dictionary";

    String[] mainOpions = {"Dictionary", "English Lesson", "Quote of the Day", "Quiz of the Day", "Moral Stories", "English Exercises"};
    String[] mainOpionsUrdu = {"لغت", "انگریزی سبق", "دن کے حوالے", "دن کا کوئز", "اخلاقی کہانیاں", "انگریزی مشقیں"};
    String[] shorttxt = {"D", "EL", "MD", "QD", "MS", "EE"};

    String[] opions = {"Share", "Rate Us", "About Us", "More Apps"};
    Integer[] shorttxtcolor = {R.color.thirdcard, R.color.firstcard, R.color.secondcard, R.color.fourthcard, R.color.fifthcard, R.color.sixthcard};
    Integer[] opions_icons_white = {R.drawable.share_unfill, R.drawable.star, R.drawable.about_us_unfilled, R.drawable.moreapp};
    Integer[] opions_icons_blue = {R.drawable.share_filled, R.drawable.star_filled, R.drawable.about_us_filled, R.drawable.app_filled};
    MainOptionsAdapter mainOptionsAdapter;
    OptionsAdapter optionsAdapter;
    int drawerpos = 0;
    Quotes_DBManager quotes_dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupLayout();
        GetQuotePreference();

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        adViewone = findViewById(R.id.adViewone);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adViewone.loadAd(adRequest1);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.interstitial_adunit_id));
        interstitial.loadAd(new AdRequest.Builder().build());

        quotes_dbManager = new Quotes_DBManager(this);
        quotes_dbManager.open();
        try {
            if (flag == 0) {
                quotes_dbManager.copyDataBase();
                flag = 1;
                QuotePreference();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        getAlarmStatus();

        if (alarm_status == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE, 10);
            calendar.set(Calendar.SECOND, 10);
            Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            alarm_status = 1;
            saveAlarmStatus();
        } else {
            Log.d("msg", "already scheduled");
        }

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        splashLayout = findViewById(R.id.splashLayout);
        drawerLayout = findViewById(R.id.drawer_layout);

        getSupportActionBar().setTitle(currentTitle);
        mainOptionsLV = findViewById(R.id.mainOptionsLV);
        mainOptionsLV.setLayoutManager(new GridLayoutManager(this, 2));
        mainOptionsAdapter = new MainOptionsAdapter();
        mainOptionsLV.setAdapter(mainOptionsAdapter);

        optionsLV = findViewById(R.id.optionsLV);
        optionsAdapter = new OptionsAdapter();
        optionsLV.setAdapter(optionsAdapter);
        optionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerpos = i;
                optionsAdapter.notifyDataSetChanged();
                if (i == 0) {
                    shareApp();
                }
                if (i == 1) {
                    rateapp();
                }
                if (i == 2) {
                    startActivity(new Intent(getApplicationContext(), ContactUs.class));
                    finish();
                }
                if (i == 3) {
                    moreapps();
                }
            }
        });

        favCard=findViewById(R.id.favCard);
        favCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FavouriteMessages.class);
                startActivity(intent);
            }
        });

    }

    public void showandstart(final Class activity) {
        if (interstitial.isLoaded()) {
            interstitial.show();
        } else {
            startMainActivity(activity);
        }

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startMainActivity(activity);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                startMainActivity(activity);
            }

        });
    }

    private void startMainActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
//        finish();
    }

    private void setupLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.content_description_open_drawer,
                R.string.content_description_close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(getString(R.string.steps));
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(currentTitle);
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public class MainOptionsAdapter extends RecyclerView.Adapter<MainOptionsAdapter.MyAdapter> {

        private LayoutInflater inflater;

        @NonNull
        @Override
        public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }
            View view = inflater.inflate(R.layout.categories_cell, parent, false);
            return new MyAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
            holder.optionname.setText(mainOpions[position]);
            holder.optionnameurdu.setText(mainOpionsUrdu[position]);
            holder.shortTxt.setText(shorttxt[position]);
            holder.shortTxtCard.setCardBackgroundColor(MainActivity.this.getResources().getColor(shorttxtcolor[position]));
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
                                showandstart(EnglishLessons.class);
                                break;
                            case 2:
                                showandstart(MessageOfTheDay.class);
                                break;
                            case 3:
                                showandstart(QuizOfTheDay.class);
                                break;
                            case 4:
                                showandstart(MoralStories.class);
                                break;
                            case 5:
                                showandstart(EnglishExercise.class);
                                break;
                        }
                    }
                });

            }
        }
    }

    public class OptionsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return opions.length;
        }

        @Override
        public Object getItem(int i) {
            return opions[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = View.inflate(MainActivity.this, R.layout.drawer_categories_cell, null);

            LinearLayout cell = rowView.findViewById(R.id.cell);
            TextView optionname = rowView.findViewById(R.id.optionname);
            optionname.setText(opions[i]);
            optionname.setCompoundDrawablesWithIntrinsicBounds(opions_icons_white[i], 0, 0, 0);

            if (drawerpos == i) {
                cell.setBackgroundResource(R.drawable.btn_bg_signin_solid);
                optionname.setTextColor(MainActivity.this.getResources().getColor(R.color.maincolor));
                optionname.setCompoundDrawablesWithIntrinsicBounds(opions_icons_blue[drawerpos], 0, 0, 0);
            }

            return rowView;
        }
    }

    public void QuotePreference() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("flag", flag);
        editor.apply();
        editor.commit();
    }

    public void GetQuotePreference() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        flag = preferences.getInt("flag", 0);
    }

    public void getAlarmStatus() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        alarm_status = preferences.getInt("alarm_status", 0);
    }

    public void saveAlarmStatus() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("alarm_status", alarm_status);
        editor.apply();
        editor.commit();
    }

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logoalpha)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    public void favbtn(View view) {
        showandstart(FavouriteMessages.class);
    }

    private void rateapp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Best English Learning App");
            String shareMessage = "\nEasiest way to Learn English\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
        }
    }

    public void moreapps() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Order+IT+Services")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=English+Learning+App")));
        }
    }

}
