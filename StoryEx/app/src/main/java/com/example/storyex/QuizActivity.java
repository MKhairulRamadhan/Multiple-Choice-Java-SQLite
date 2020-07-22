package com.example.storyex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;

    private TextView textViewQuestions;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;

    private TextView textViewCorrext, textViewWrong;
    private ArrayList<Questions> questionsList;
    private int questionCounter;
    private int questionTotalCount;
    private Questions currentQuestion;
    private boolean answerd;

    private Handler handler = new Handler();

    private ColorStateList buttonLabelColor;

    private int corectAns = 0, wrongAns = 0;

    private FinalScoreDialog finalScoreDialog;

    private int totalSizeofQuiz = 0;
    private CountDownTimer countDownTimer;
    private long timeleftinMillis;

    private static final long COUNTDOWN_IN_MILLIS = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        setupUI();
        fetchDB();

        buttonLabelColor = rb1.getTextColors();

        finalScoreDialog = new FinalScoreDialog(this);
    }

    private void setupUI() {
        textViewCorrext = findViewById(R.id.txtCorrect);
        textViewWrong = findViewById(R.id.txtWrong);
        textViewCountDown = findViewById(R.id.txtTimer);
        textViewQuestionCount = findViewById(R.id.txtTotalQuestion);
        textViewScore = findViewById(R.id.txtScore);
        textViewQuestions = findViewById(R.id.txtQuestion);
        buttonConfirmNext = findViewById(R.id.btnComfirm);

        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radioButton1);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);

    }

    private void fetchDB(){
        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionsList = dbHelper.getAllQuestions();

        startQuiz();
    }

    private void startQuiz(){
        questionTotalCount = questionsList.size();
        Collections.shuffle(questionsList);

        showQuestions();

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {
                switch (checked){
                    case R.id.radioButton1:
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.when_option_selected));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));

                        rb1.setTextColor(Color.WHITE);
                        rb2.setTextColor(Color.BLACK);
                        rb3.setTextColor(Color.BLACK);
                        rb4.setTextColor(Color.BLACK);
                        break;

                    case R.id.radioButton2:
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.when_option_selected));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));

                        rb1.setTextColor(Color.BLACK);
                        rb2.setTextColor(Color.WHITE);
                        rb3.setTextColor(Color.BLACK);
                        rb4.setTextColor(Color.BLACK);
                        break;

                    case R.id.radioButton3:
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.when_option_selected));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
                        rb1.setTextColor(Color.BLACK);
                        rb2.setTextColor(Color.BLACK);
                        rb3.setTextColor(Color.WHITE);
                        rb4.setTextColor(Color.BLACK);
                        break;

                    case R.id.radioButton4:
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.when_option_selected));
                        rb1.setTextColor(Color.BLACK);
                        rb2.setTextColor(Color.BLACK);
                        rb3.setTextColor(Color.BLACK);
                        rb4.setTextColor(Color.WHITE);
                        break;

                }
            }
        });


        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!answerd){
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()){
                        quizOperations();
                    }else{
                        Toast.makeText(QuizActivity.this, "Please Select Options", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void quizOperations() {

        answerd = true;

        countDownTimer.cancel();

        RadioButton rbselected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbselected) + 1;

        checkSolution(answerNr, rbselected);
    }

    private void checkSolution(int answerNr, RadioButton rbselected) {

        switch (currentQuestion.getAnswerNr()){
            case 1:
                if (currentQuestion.getAnswerNr() == answerNr){
                    rb1.setBackground(ContextCompat.getDrawable(this, R.drawable.when_answer_correct));

                    corectAns++;
                    textViewCorrext.setText("Correct: " + String.valueOf(corectAns));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },1000);

                }else{
                    changeIncorrecColor(rbselected);

                    wrongAns++;
                    textViewWrong.setText("Wrong: "+ String.valueOf(wrongAns));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },1000);
                }
                break;
            case 2:
                if (currentQuestion.getAnswerNr() == answerNr){
                    rb2.setBackground(ContextCompat.getDrawable(this, R.drawable.when_answer_correct));

                    corectAns++;
                    textViewCorrext.setText("Correct: " + String.valueOf(corectAns));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },1000);
                }else{
                    changeIncorrecColor(rbselected);

                    wrongAns++;
                    textViewWrong.setText("Wrong: "+ String.valueOf(wrongAns));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },1000);
                }
                break;

            case 3:
                if (currentQuestion.getAnswerNr() == answerNr){
                    rb3.setBackground(ContextCompat.getDrawable(this, R.drawable.when_answer_correct));

                    corectAns++;
                    textViewCorrext.setText("Correct: " + String.valueOf(corectAns));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },1000);
                }else{
                    changeIncorrecColor(rbselected);

                    wrongAns++;
                    textViewWrong.setText("Wrong: "+ String.valueOf(wrongAns));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },1000);
                }
                break;
            case 4:
                if (currentQuestion.getAnswerNr() == answerNr){
                    rb4.setBackground(ContextCompat.getDrawable(this, R.drawable.when_answer_correct));

                    corectAns++;
                    textViewCorrext.setText("Correct: " + String.valueOf(corectAns));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },1000);
                }else{
                    changeIncorrecColor(rbselected);

                    wrongAns++;
                    textViewWrong.setText("Wrong: "+ String.valueOf(wrongAns));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showQuestions();
                        }
                    },1000);
                }
                break;

        } // end of switch statement

        if (questionCounter >= questionTotalCount){
            buttonConfirmNext.setText("Comfirm and Finish");
        }

    }

    private void changeIncorrecColor(RadioButton rbselected) {
        rbselected.setBackground(ContextCompat.getDrawable(this, R.drawable.when_answer_wrong));
        rbselected.setTextColor(Color.WHITE);
    }

    private void showQuestions() {
        rbGroup.clearCheck();

        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));
        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.buttons_background));

        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);


        if (questionCounter < questionTotalCount){
            currentQuestion = questionsList.get(questionCounter);
            textViewQuestions.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;
            answerd = false;

            buttonConfirmNext.setText("Confirm");

            textViewQuestionCount.setText("Questions : " +questionCounter+"/"+questionTotalCount);

            timeleftinMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();

        }else {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
//                    startActivity(intent);
//                }
//            }, 500);

            totalSizeofQuiz = questionsList.size();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finalScoreDialog.finalScoreDialog(corectAns,wrongAns,totalSizeofQuiz);
                }
            },1000);

        }

    }

    //timer

    private void startCountDown(){
        countDownTimer = new CountDownTimer(timeleftinMillis, 1000) {
            @Override
            public void onTick(long l) {
                timeleftinMillis = l;

                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeleftinMillis = 0;
                updateCountDownText();

            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeleftinMillis / 1000) / 60;
        int seconds = (int) (timeleftinMillis / 1000) % 60;

        //String timeFormatted = String.format(Locale.getDefault(), "02d:%02d", minutes, seconds);
        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);

        if (timeleftinMillis < 10000){
            textViewCountDown.setTextColor(Color.RED);


        }else {
            textViewCountDown.setTextColor(Color.BLACK);
        }

        if (timeleftinMillis == 0){
            Toast.makeText(this, "Times Up!", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    startActivity(intent);
                }
            },1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }
}
