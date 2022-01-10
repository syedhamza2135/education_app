package com.hamza.app.spellchecker;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SpellCheckerActivity extends Activity implements SpellCheckerSessionListener {
    Button b1;
    TextView tv1;
    EditText ed1;
    private SpellCheckerSession mScs;
    String TAG = "request";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spellchecker);

        b1 = (Button) findViewById(R.id.button);
        tv1 = (TextView) findViewById(R.id.textView3);

        ed1 = (EditText) findViewById(R.id.editText);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        ed1.getText().toString(), Toast.LENGTH_SHORT).show();
                String textt = ed1.getText().toString().trim();
//                mScs.getSentenceSuggestions(new TextInfo[]{new TextInfo(textt)}, 3);
                // mScs.getSuggestions(new TextInfo(textt), 3);

                String key = "9w3cxHS7uYmabq80";
                String url = "https://api.textgears.com/spelling?text=" + textt + "&key=9w3cxHS7uYmabq80";
//                https://api.textgears.com/spelling?text=i m engineeer and what is ur name&key=9w3cxHS7uYmabq80

                queue = Volley.newRequestQueue(SpellCheckerActivity.this);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
//                                tv1.setText("Response is: " + response.toString());
                                StringBuilder sb = new StringBuilder();

                                Log.e("okkk", response.toString());
                                try {
                                    JSONObject respObj = new JSONObject(response);
                                    JSONObject resObj2 = respObj.getJSONObject("response");
                                    JSONArray resArray1 = resObj2.getJSONArray("errors");

                                    if(resArray1.length()==0){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
//                                                tv1.append( " ");
                                                tv1.setText( "Congrates Spelling is right  ");
                                            }
                                        });
                                        return;
                                    }
                                    for (int j = 0; j < resArray1.length(); j++) {
                                        JSONObject resObj3 = resArray1.getJSONObject(j);
//                                    tv1.setText(resObj3.get("bad")+"  new");

                                        sb.append('\n').append("Wrong Spelling: ").append(resObj3.get("bad").toString());
                                        JSONArray arrayBetter = resObj3.getJSONArray("better");
//                                    Toast.makeText(SpellCheckerActivity.this, bad+" yes", Toast.LENGTH_SHORT).show();
//                                    tv1.setText("    ");

                                        sb.append('\n');
                                        sb.append("Suggested Words: ");

                                        for (int i = 0; i < arrayBetter.length(); i++) {
//                                        tv1.setText(arrayBetter.get(i).toString()+"  ok");

                                            sb.append(arrayBetter.get(i).toString()).append(", ");//.append(" 3 ");
                                        }
//                                        tv1.append(sb.toString()+"new ");
                                    }
                                    tv1.setText(" ");
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            tv1.append(sb.toString() + "  ");
                                        }
                                    });

                                } catch (JSONException e) {
                                    Toast.makeText(SpellCheckerActivity.this, "adsf" + e.toString(), Toast.LENGTH_SHORT).show();

                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tv1.setText("That didn't work!");
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);


            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }


    public void onResume() {
        super.onResume();
        final TextServicesManager tsm = (TextServicesManager)
                getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        mScs = tsm.newSpellCheckerSession(null, null, this, true);


        if (mScs == null) {
            Toast.makeText(this, "Please turn on the spell checker from setting", Toast.LENGTH_LONG).show();
            // You can even open the settings page for user to turn it ON
            ComponentName componentToLaunch = new ComponentName("com.android.settings",
                    "com.android.settings.Settings$SpellCheckersSettingsActivity");
            Intent intent = new Intent();
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(componentToLaunch);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                this.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Error
            }
        } else {

        }

    }

    public void onPause() {
        super.onPause();
        if (mScs != null) {
            mScs.close();
        }
    }

    public void onGetSuggestions(final SuggestionsInfo[] arg0) {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arg0.length; ++i) {
            // Returned suggestions are contained in SuggestionsInfo
            final int len = arg0[i].getSuggestionsCount();
            sb.append('\n');

            for (int j = 0; j < len; ++j) {
                sb.append("," + arg0[i].getSuggestionAt(j));
            }

            sb.append(" (" + len + ")");
        }
        Toast.makeText(this, "on get ", Toast.LENGTH_SHORT).show();
        runOnUiThread(new Runnable() {
            public void run() {
                tv1.append(sb.toString());
            }
        });
    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] arg0) {
        // TODO Auto-generated method stub

        for (int i = 0; i < arg0.length; ++i) {
            // Returned suggestions are contained in SuggestionsInfo
            Log.e("okkkkk 0", "EX" + arg0[i]);
            for (int j = 0; j < arg0[i].getSuggestionsCount(); ++j) {
                SuggestionsInfo si = arg0[i].getSuggestionsInfoAt(j);
                int a = si.getSuggestionsCount();


                if ((si.getSuggestionsAttributes() & SuggestionsInfo.RESULT_ATTR_IN_THE_DICTIONARY) != 0) {
                    Log.e("okkkkk", "EXACT MATCH FOUND " + a);

                }
            }
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arg0.length; ++i) {
            // Returned suggestions are contained in SuggestionsInfo
            final int len = arg0[i].getSuggestionsCount();
            sb.append('\n');

            for (int j = 0; j < len; ++j) {
                sb.append(" " + arg0[i].getSuggestionsInfoAt(j));
                Log.e("okkkkk2", "EXACT MATCH FOUND 2 " + sb);
            }

            sb.append(" (" + len + ")");
        }

        Toast.makeText(this, "on get sentence", Toast.LENGTH_SHORT).show();
//        runOnUiThread(new Runnable() {
//            public void run() {
//                tv1.append(sb.toString());
//            }
//        });


    }
}
