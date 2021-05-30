package english.grammar.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import english.grammar.app.Manager.Conversation_DBManager;
import english.grammar.app.Models.QuizModel;
import english.grammar.app.SharedData.SharedClass;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class QuizOfTheDay extends AppCompatActivity {

    private AdView mAdView;
    View view;
    Conversation_DBManager dbManager;
    ImageButton backBtn;
    RecyclerView quizRV;
    Handler checkHandler;
    int flag = 0;
    Runnable checkRunnable;

    TextView questionTxt, total, right, wrong, remarksTxt;
    TextView optA, optB, optC, optD;
    ImageButton wrongA, wrongB, wrongC, wrongD, check1, check2, check3, check4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_of_day);
        view = getWindow().getDecorView().getRootView();

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        dbManager = new Conversation_DBManager(this);
        dbManager.open();
        try {
            dbManager.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }


        init();

        quizRV.setLayoutManager(new LinearLayoutManager(this));
        setCheckSrc();
        SharedClass.total = dbManager.getAllQuiz().size();
        total.setText(String.valueOf(SharedClass.total));
        right.setText(String.valueOf(SharedClass.right));
        wrong.setText(String.valueOf(SharedClass.wrong));

        questionTxt.setText(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getQuestion());
        optA.setText(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getOpt1());
        optB.setText(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getOpt2());
        optC.setText(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getOpt3());
        optD.setText(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getOpt4());

        checkHandler = new Handler();
        checkRunnable = new Runnable() {
            @Override
            public void run() {
                if ((Integer.parseInt(right.getText().toString()) + Integer.parseInt(wrong.getText().toString())) == SharedClass.total) {
                    if (flag == 0) {
                        disablebtn();
                        Snackbar.make(view, "Quiz Over", Snackbar.LENGTH_LONG).show();
                        if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 50 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 30) {
                            remarksTxt.setText("You Need Improvement");
                            remarksTxt.setTextColor(getResources().getColor(R.color.future));
                        } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 30) {
                            remarksTxt.setText("Failed, better luck next time");
                            remarksTxt.setTextColor(getResources().getColor(R.color.future));
                        } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 50 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 60) {
                            remarksTxt.setText("You are good");
                            remarksTxt.setTextColor(getResources().getColor(R.color.present));
                        } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 60 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 70) {
                            remarksTxt.setText("You are very good");
                            remarksTxt.setTextColor(getResources().getColor(R.color.present));
                        } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 70 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 90) {
                            remarksTxt.setText("You've done a great job");
                            remarksTxt.setTextColor(getResources().getColor(R.color.present));
                        } else {
                            remarksTxt.setText("Very Good");
                            remarksTxt.setTextColor(getResources().getColor(R.color.present));
                        }
                        flag = 1;
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedClass.question_position = 0;
                            SharedClass.right = 0;
                            SharedClass.wrong = 0;
                            SharedClass.total = 0;
                            SharedClass.attempted = 0;
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }, 2000);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Future longRunningTaskFuture = executorService.submit(checkRunnable);
                    longRunningTaskFuture.cancel(true);
                }
                checkHandler.postDelayed(checkRunnable, 500);
            }
        };
        checkRunnable.run();

        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Integer.parseInt(right.getText().toString()) + Integer.parseInt(wrong.getText().toString())) == SharedClass.total) {
                    Snackbar.make(view, "Quiz Over", Snackbar.LENGTH_LONG).show();
                    if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 50 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 30) {
                        remarksTxt.setText("You Need Improvement");
                        remarksTxt.setTextColor(getResources().getColor(R.color.future));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 30) {
                        remarksTxt.setText("Failed, better luck next time");
                        remarksTxt.setTextColor(getResources().getColor(R.color.future));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 50 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 60) {
                        remarksTxt.setText("You are good");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 60 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 70) {
                        remarksTxt.setText("You are very good");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 70 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 90) {
                        remarksTxt.setText("You've done a great job");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    } else {
                        remarksTxt.setText("Very Good");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedClass.question_position = 0;
                            SharedClass.right = 0;
                            SharedClass.wrong = 0;
                            SharedClass.total = 0;
                            SharedClass.attempted = 0;
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }, 2000);
                }
                SharedClass.attempted = SharedClass.attempted + 1;
                if (SharedClass.question_position > dbManager.getAllQuiz().size()) {
                    Toast.makeText(QuizOfTheDay.this, "quiz over", Toast.LENGTH_SHORT).show();
                    disablebtn();
                } else {
                    if (dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getOpt1().equals(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getAnswer())) {
                        SharedClass.right = SharedClass.right + 1;
                        right.setText(String.valueOf(SharedClass.right));
                        wrong.setText(String.valueOf(SharedClass.wrong));
                        check1.setBackgroundResource(R.drawable.rightcheck);
                        optA.setTextColor(getResources().getColor(R.color.txtcolor));
                        disablebtn();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedClass.question_position = SharedClass.question_position + 1;
                                enablebtn();
                                check1.setBackgroundResource(R.drawable.simplecheck);
                                if (SharedClass.question_position <= dbManager.getAllQuiz().size()) {
                                    newSetOfQuestion();
                                }
                            }
                        }, 1000);
                    } else {
                        SharedClass.wrong = SharedClass.wrong + 1;
                        right.setText(String.valueOf(SharedClass.right));
                        wrong.setText(String.valueOf(SharedClass.wrong));
                        check1.setBackgroundResource(R.drawable.wrongcheck);
                        wrongA.setVisibility(View.VISIBLE);

                        disablebtn();

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedClass.question_position = SharedClass.question_position + 1;
                                enablebtn();
                                check1.setBackgroundResource(R.drawable.simplecheck);
                                wrongA.setVisibility(View.GONE);
                                if (SharedClass.question_position <= dbManager.getAllQuiz().size()) {
                                    newSetOfQuestion();
                                }
                            }
                        }, 1000);
                    }
                }
            }
        });

        check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Integer.parseInt(right.getText().toString()) + Integer.parseInt(wrong.getText().toString())) == SharedClass.total) {
                    Snackbar.make(view, "Quiz Over", Snackbar.LENGTH_LONG).show();
                    if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 50 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 30) {
                        remarksTxt.setText("You Need Improvement");
                        remarksTxt.setTextColor(getResources().getColor(R.color.future));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 30) {
                        remarksTxt.setText("Failed, better luck next time");
                        remarksTxt.setTextColor(getResources().getColor(R.color.future));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 50 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 60) {
                        remarksTxt.setText("You are good");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 60 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 70) {
                        remarksTxt.setText("You are very good");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 70 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 90) {
                        remarksTxt.setText("You've done a great job");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    } else {
                        remarksTxt.setText("Very Good");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedClass.question_position = 0;
                            SharedClass.right = 0;
                            SharedClass.wrong = 0;
                            SharedClass.total = 0;
                            SharedClass.attempted = 0;
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }, 2000);
                }
                SharedClass.attempted = SharedClass.attempted + 1;
                if (SharedClass.question_position > dbManager.getAllQuiz().size()) {
                    Toast.makeText(QuizOfTheDay.this, "quiz over", Toast.LENGTH_SHORT).show();
                    disablebtn();
                } else {
                    if (dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getOpt2().equals(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getAnswer())) {
                        SharedClass.right = SharedClass.right + 1;
                        right.setText(String.valueOf(SharedClass.right));
                        wrong.setText(String.valueOf(SharedClass.wrong));
                        check2.setBackgroundResource(R.drawable.rightcheck);
                        optB.setTextColor(getResources().getColor(R.color.txtcolor));
                        disablebtn();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedClass.question_position = SharedClass.question_position + 1;
                                enablebtn();
                                check2.setBackgroundResource(R.drawable.simplecheck);
                                if (SharedClass.question_position <= dbManager.getAllQuiz().size()) {
                                    newSetOfQuestion();
                                }
                            }
                        }, 1000);
                    } else {
                        SharedClass.wrong = SharedClass.wrong + 1;
                        right.setText(String.valueOf(SharedClass.right));
                        wrong.setText(String.valueOf(SharedClass.wrong));
                        check2.setBackgroundResource(R.drawable.wrongcheck);
                        wrongB.setVisibility(View.VISIBLE);

                        disablebtn();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedClass.question_position = SharedClass.question_position + 1;
                                enablebtn();
                                check2.setBackgroundResource(R.drawable.simplecheck);
                                wrongB.setVisibility(View.GONE);
                                if (SharedClass.question_position <= dbManager.getAllQuiz().size()) {
                                    newSetOfQuestion();
                                }
                            }
                        }, 1000);
                    }
                }
            }
        });

        check3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Integer.parseInt(right.getText().toString()) + Integer.parseInt(wrong.getText().toString())) == SharedClass.total) {
                    Snackbar.make(view, "Quiz Over", Snackbar.LENGTH_LONG).show();
                    if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 50 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 30) {
                        remarksTxt.setText("You Need Improvement");
                        remarksTxt.setTextColor(getResources().getColor(R.color.future));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 30) {
                        remarksTxt.setText("Failed, better luck next time");
                        remarksTxt.setTextColor(getResources().getColor(R.color.future));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 50 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 60) {
                        remarksTxt.setText("You are good");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 60 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 70) {
                        remarksTxt.setText("You are very good");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 70 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 90) {
                        remarksTxt.setText("You've done a great job");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    } else {
                        remarksTxt.setText("Very Good");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedClass.question_position = 0;
                            SharedClass.right = 0;
                            SharedClass.wrong = 0;
                            SharedClass.total = 0;
                            SharedClass.attempted = 0;
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }, 2000);
                }
                SharedClass.attempted = SharedClass.attempted + 1;
                if (SharedClass.question_position > dbManager.getAllQuiz().size()) {
                    Toast.makeText(QuizOfTheDay.this, "quiz over", Toast.LENGTH_SHORT).show();
                    disablebtn();
                } else {
                    if (dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getOpt3().equals(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getAnswer())) {
                        SharedClass.right = SharedClass.right + 1;
                        right.setText(String.valueOf(SharedClass.right));
                        wrong.setText(String.valueOf(SharedClass.wrong));
                        check3.setBackgroundResource(R.drawable.rightcheck);
                        optC.setTextColor(getResources().getColor(R.color.txtcolor));
                        disablebtn();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedClass.question_position = SharedClass.question_position + 1;
                                enablebtn();
                                check3.setBackgroundResource(R.drawable.simplecheck);
                                if (SharedClass.question_position <= dbManager.getAllQuiz().size()) {
                                    newSetOfQuestion();
                                }
                            }
                        }, 1000);
                    } else {
                        SharedClass.wrong = SharedClass.wrong + 1;
                        right.setText(String.valueOf(SharedClass.right));
                        wrong.setText(String.valueOf(SharedClass.wrong));
                        check3.setBackgroundResource(R.drawable.wrongcheck);
                        wrongC.setVisibility(View.VISIBLE);

                        disablebtn();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedClass.question_position = SharedClass.question_position + 1;
                                enablebtn();
                                check3.setBackgroundResource(R.drawable.simplecheck);
                                wrongC.setVisibility(View.GONE);
                                if (SharedClass.question_position <= dbManager.getAllQuiz().size()) {
                                    newSetOfQuestion();
                                }
                            }
                        }, 1000);
                    }
                }
            }
        });

        check4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Integer.parseInt(right.getText().toString()) + Integer.parseInt(wrong.getText().toString())) == SharedClass.total) {
                    Snackbar.make(view, "Quiz Over", Snackbar.LENGTH_LONG).show();
                    if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 50 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 30) {
                        remarksTxt.setText("You Need Improvement");
                        remarksTxt.setTextColor(getResources().getColor(R.color.future));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 30) {
                        remarksTxt.setText("Failed, better luck next time");
                        remarksTxt.setTextColor(getResources().getColor(R.color.future));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 50 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 60) {
                        remarksTxt.setText("You are good");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 60 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 70) {
                        remarksTxt.setText("You are very good");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    } else if (((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) > 70 && ((Float.parseFloat(right.getText().toString()) * 100) / SharedClass.total) < 90) {
                        remarksTxt.setText("You've done a great job");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    } else {
                        remarksTxt.setText("Very Good");
                        remarksTxt.setTextColor(getResources().getColor(R.color.present));
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedClass.question_position = 0;
                            SharedClass.right = 0;
                            SharedClass.wrong = 0;
                            SharedClass.total = 0;
                            SharedClass.attempted = 0;
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }, 2000);
                }
                SharedClass.attempted = SharedClass.attempted + 1;
                if (SharedClass.question_position > dbManager.getAllQuiz().size()) {
                    Toast.makeText(QuizOfTheDay.this, "quiz over", Toast.LENGTH_SHORT).show();
                    disablebtn();
                } else {
                    if (dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getOpt3().equals(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getAnswer())) {
                        SharedClass.right = SharedClass.right + 1;
                        right.setText(String.valueOf(SharedClass.right));
                        wrong.setText(String.valueOf(SharedClass.wrong));
                        check4.setBackgroundResource(R.drawable.rightcheck);
                        optD.setTextColor(getResources().getColor(R.color.txtcolor));
                        disablebtn();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedClass.question_position = SharedClass.question_position + 1;
                                enablebtn();
                                check4.setBackgroundResource(R.drawable.simplecheck);
                                if (SharedClass.question_position <= dbManager.getAllQuiz().size()) {
                                    newSetOfQuestion();
                                }
                            }
                        }, 1000);
                    } else {
                        SharedClass.wrong = SharedClass.wrong + 1;
                        right.setText(String.valueOf(SharedClass.right));
                        wrong.setText(String.valueOf(SharedClass.wrong));
                        check4.setBackgroundResource(R.drawable.wrongcheck);
                        wrongD.setVisibility(View.VISIBLE);

                        disablebtn();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedClass.question_position = SharedClass.question_position + 1;
                                enablebtn();
                                check4.setBackgroundResource(R.drawable.simplecheck);
                                wrongD.setVisibility(View.GONE);
                                if (SharedClass.question_position <= dbManager.getAllQuiz().size()) {
                                    newSetOfQuestion();
                                }
                            }
                        }, 1000);
                    }
                }
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

    public void setCheckSrc() {
        check1.setBackgroundResource(R.drawable.simplecheck);
        check2.setBackgroundResource(R.drawable.simplecheck);
        check3.setBackgroundResource(R.drawable.simplecheck);
        check4.setBackgroundResource(R.drawable.simplecheck);
    }

    public void newSetOfQuestion() {
        questionTxt = findViewById(R.id.questionTxt);
        optA = findViewById(R.id.optA);
        optB = findViewById(R.id.optB);
        optC = findViewById(R.id.optC);
        optD = findViewById(R.id.optD);
        if (SharedClass.question_position > dbManager.getAllQuiz().size()) {
            Toast.makeText(this, "quiz over", Toast.LENGTH_SHORT).show();
        } else {
            questionTxt.setText(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getQuestion());
            optA.setText(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getOpt1());
            optB.setText(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getOpt2());
            optC.setText(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getOpt3());
            optD.setText(dbManager.getQuiz(String.valueOf(SharedClass.question_position)).get(0).getOpt4());
        }
    }

    private void init() {
        questionTxt = findViewById(R.id.questionTxt);
        optA = findViewById(R.id.optA);
        optB = findViewById(R.id.optB);
        optC = findViewById(R.id.optC);
        optD = findViewById(R.id.optD);
        wrongA = findViewById(R.id.wrongA);
        wrongB = findViewById(R.id.wrongB);
        wrongC = findViewById(R.id.wrongC);
        wrongD = findViewById(R.id.wrongD);
        check1 = findViewById(R.id.check1);
        check2 = findViewById(R.id.check2);
        check3 = findViewById(R.id.check3);
        check4 = findViewById(R.id.check4);
        backBtn = findViewById(R.id.backBtn);
        quizRV = findViewById(R.id.quizRV);
        total = findViewById(R.id.total);
        right = findViewById(R.id.right);
        wrong = findViewById(R.id.wrong);
        remarksTxt = findViewById(R.id.remarksTxt);
    }

    private void disablebtn() {
        check1.setEnabled(false);
        check2.setEnabled(false);
        check3.setEnabled(false);
        check4.setEnabled(false);
    }

    private void enablebtn() {
        check1.setEnabled(true);
        check2.setEnabled(true);
        check3.setEnabled(true);
        check4.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.MyAdapter> {

        Context context;
        List<QuizModel> data;
        private LayoutInflater inflater;

        public ExampleAdapter(Context context, List<QuizModel> data) {
            this.context = context;
            this.data = data;
        }

        @NonNull
        @Override
        public MyAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if (inflater == null) {
                inflater = LayoutInflater.from(viewGroup.getContext());
            }
            View view = inflater.inflate(R.layout.quiz_custom_cell, viewGroup, false);
            return new MyAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter myAdapter, int i) {
            myAdapter.questionTxt.setText(data.get(SharedClass.question_position).getQuestion());
            myAdapter.optA.setText(data.get(SharedClass.question_position).getOpt1());
            myAdapter.optB.setText(data.get(SharedClass.question_position).getOpt2());
            myAdapter.optC.setText(data.get(SharedClass.question_position).getOpt3());
            myAdapter.optD.setText(data.get(SharedClass.question_position).getOpt4());
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public class MyAdapter extends RecyclerView.ViewHolder {
            TextView questionTxt;
            AppCompatCheckBox optA, optB, optC, optD;
            ImageButton wrongA, wrongB, wrongC, wrongD;

            public MyAdapter(@NonNull View itemView) {
                super(itemView);
                questionTxt = itemView.findViewById(R.id.questionTxt);
                optA = itemView.findViewById(R.id.optA);
                optB = itemView.findViewById(R.id.optB);
                optC = itemView.findViewById(R.id.optC);
                optD = itemView.findViewById(R.id.optD);
                wrongA = itemView.findViewById(R.id.wrongA);
                wrongB = itemView.findViewById(R.id.wrongB);
                wrongC = itemView.findViewById(R.id.wrongC);
                wrongD = itemView.findViewById(R.id.wrongD);

                optA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (data.get(getAdapterPosition()).getOpt1().equals(data.get(getAdapterPosition()).getAnswer())) {
                            optA.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                            optA.setTextColor(context.getResources().getColor(R.color.txtcolor));
                            disablebtn();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SharedClass.question_position = SharedClass.question_position + 1;
                                    notifyDataSetChanged();
                                    enablebtn();
                                }
                            }, 1000);
                        } else {
                            optA.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.future)));
                            optA.setChecked(true);
                            wrongA.setVisibility(View.VISIBLE);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (data.get(getAdapterPosition()).getOpt2().equals(data.get(getAdapterPosition()).getAnswer())) {
                                        optB.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                                        optB.setChecked(true);
                                        optB.setTextColor(context.getResources().getColor(R.color.txtcolor));
                                    }
                                    if (data.get(getAdapterPosition()).getOpt3().equals(data.get(getAdapterPosition()).getAnswer())) {
                                        optC.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                                        optC.setChecked(true);
                                        optC.setTextColor(context.getResources().getColor(R.color.txtcolor));
                                    }
                                    if (data.get(getAdapterPosition()).getOpt4().equals(data.get(getAdapterPosition()).getAnswer())) {
                                        optD.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                                        optD.setChecked(true);
                                        optD.setTextColor(context.getResources().getColor(R.color.txtcolor));
                                    }
                                }
                            }, 1000);
                            disablebtn();
                        }
//                    }
                    }
                });
                optB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (data.get(getAdapterPosition()).getOpt2().equals(data.get(getAdapterPosition()).getAnswer())) {
                            optB.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                            optB.setTextColor(context.getResources().getColor(R.color.txtcolor));
                            disablebtn();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SharedClass.question_position = SharedClass.question_position + 1;
                                    notifyDataSetChanged();
                                    enablebtn();
                                }
                            }, 1000);
                        } else {
                            optB.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.future)));
                            optB.setChecked(true);
                            wrongB.setVisibility(View.VISIBLE);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (data.get(getAdapterPosition()).getOpt1().equals(data.get(getAdapterPosition()).getAnswer())) {
                                        optA.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                                        optA.setChecked(true);
                                        optA.setTextColor(context.getResources().getColor(R.color.txtcolor));
                                    }
                                    if (data.get(getAdapterPosition()).getOpt3().equals(data.get(getAdapterPosition()).getAnswer())) {
                                        optC.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                                        optC.setChecked(true);
                                        optC.setTextColor(context.getResources().getColor(R.color.txtcolor));
                                    }
                                    if (data.get(getAdapterPosition()).getOpt4().equals(data.get(getAdapterPosition()).getAnswer())) {
                                        optD.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                                        optD.setChecked(true);
                                        optD.setTextColor(context.getResources().getColor(R.color.txtcolor));
                                    }
                                }
                            }, 1000);
                            disablebtn();
                        }
                    }
                });
                optC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (data.get(getAdapterPosition()).getOpt3().equals(data.get(getAdapterPosition()).getAnswer())) {
                            optC.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                            optC.setTextColor(context.getResources().getColor(R.color.txtcolor));
                            disablebtn();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SharedClass.question_position = SharedClass.question_position + 1;
                                    notifyDataSetChanged();
                                    enablebtn();
                                }
                            }, 1000);
                        } else {
                            optC.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.future)));
                            optC.setChecked(true);
                            wrongC.setVisibility(View.VISIBLE);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (data.get(getAdapterPosition()).getOpt1().equals(data.get(getAdapterPosition()).getAnswer())) {
                                        optA.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                                        optA.setChecked(true);
                                        optA.setTextColor(context.getResources().getColor(R.color.txtcolor));
                                    }
                                    if (data.get(getAdapterPosition()).getOpt2().equals(data.get(getAdapterPosition()).getAnswer())) {
                                        optB.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                                        optB.setChecked(true);
                                        optB.setTextColor(context.getResources().getColor(R.color.txtcolor));
                                    }
                                    if (data.get(getAdapterPosition()).getOpt4().equals(data.get(getAdapterPosition()).getAnswer())) {
                                        optD.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                                        optD.setChecked(true);
                                        optD.setTextColor(context.getResources().getColor(R.color.txtcolor));
                                    }
                                }
                            }, 1000);
                            disablebtn();
                        }
                    }
                });
                optD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (data.get(getAdapterPosition()).getOpt4().equals(data.get(getAdapterPosition()).getAnswer())) {
                            optD.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                            optD.setTextColor(context.getResources().getColor(R.color.txtcolor));
                            disablebtn();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SharedClass.question_position = SharedClass.question_position + 1;
                                    notifyDataSetChanged();
                                    enablebtn();
                                }
                            }, 1000);
                        } else {
                            optD.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.future)));
                            optD.setChecked(true);
                            wrongD.setVisibility(View.VISIBLE);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (data.get(getAdapterPosition()).getOpt1().equals(data.get(getAdapterPosition()).getAnswer())) {
                                        optA.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                                        optA.setChecked(true);
                                        optA.setTextColor(context.getResources().getColor(R.color.txtcolor));
                                    }
                                    if (data.get(getAdapterPosition()).getOpt2().equals(data.get(getAdapterPosition()).getAnswer())) {
                                        optB.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                                        optB.setChecked(true);
                                        optB.setTextColor(context.getResources().getColor(R.color.txtcolor));
                                    }
                                    if (data.get(getAdapterPosition()).getOpt3().equals(data.get(getAdapterPosition()).getAnswer())) {
                                        optC.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.present)));
                                        optC.setChecked(true);
                                        optC.setTextColor(context.getResources().getColor(R.color.txtcolor));
                                    }
                                }
                            }, 1000);
                            disablebtn();
                        }
                    }
                });
            }

            private void disablebtn() {
                optA.setEnabled(false);
                optB.setEnabled(false);
                optC.setEnabled(false);
                optD.setEnabled(false);
            }

            private void enablebtn() {
                optA.setEnabled(true);
                optB.setEnabled(true);
                optC.setEnabled(true);
                optD.setEnabled(true);
            }

        }

    }

    public void favbtn(View view) {
        startActivity(new Intent(getApplicationContext(), FavouriteMessages.class));
    }

}
