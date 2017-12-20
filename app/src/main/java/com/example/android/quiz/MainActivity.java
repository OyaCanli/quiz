package com.example.android.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.app.AlertDialog;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView question, hint, time, showHint, half;
    RadioButton optionA, optionB, optionC, optionD, correctOption, wrongOption;
    RadioGroup options;
    Button nextButton, backToCategories;
    CountDownTimer timer1, timer2;
    RelativeLayout root;
    int questionNumber, counter, hintCounter, halfLifelineCounter;
    long millis;
    boolean isTimerOn, firstTimerCancelled, isNextEnabled, correctOptionIsShown, wrongOptionIsShown;
    boolean OptAIsYellow, OptBIsYellow, OptCIsYellow, OptDIsYellow;

    protected static final String KEY_COUNTER = "SavedStateOfCounter";
    protected static final String KEY_QUESTIONNUMBER = "SavedStateOfQuestionNumber";
    protected static final String KEY_HINT_COUNTER = "SavedStateOfHintCounter";
    protected static final String KEY_HALF_COUNTER = "SavedStateOfHalfCounter";
    protected static final String KEY_IS_TIMER_ON = "SavedStateOfIsTimerOn";
    protected static final String KEY_OPTA_IS_YELLOW = "SavedStateOfOptAIsYellow";
    protected static final String KEY_OPTB_IS_YELLOW = "SavedStateOfOptBIsYellow";
    protected static final String KEY_OPTC_IS_YELLOW = "SavedStateOfOptCIsYellow";
    protected static final String KEY_OPTD_IS_YELLOW = "SavedStateOfOptDIsYellow";
    protected static final String KEY_CORRECT_OPTION_IS_SHOWN = "SavedStateOfCorrectOptionIsShown";
    protected static final String KEY_WRONG_OPTION_IS_SHOWN = "SavedStateOfWrongOptionIsShown";
    protected static final String KEY_IS_NEXT_ENABLED = "SavedStateOfIsNextEnabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize views
        question = (TextView) findViewById(R.id.question);
        hint = (TextView) findViewById(R.id.hint);
        optionA = (RadioButton) findViewById(R.id.optionA);
        optionB = (RadioButton) findViewById(R.id.optionB);
        optionC = (RadioButton) findViewById(R.id.optionC);
        optionD = (RadioButton) findViewById(R.id.optionD);
        options = (RadioGroup) findViewById(R.id.options);
        nextButton = (Button) findViewById(R.id.next);
        root = findViewById(R.id.root);
        showHint = findViewById(R.id.showHint);
        half = (TextView) findViewById(R.id.half);
        time = (TextView) findViewById(R.id.time);
        //Set the first question
        question.setText(WelcomeActivity.questions[questionNumber][0]);
        hint.setText(WelcomeActivity.questions[questionNumber][1]);
        optionA.setText(WelcomeActivity.questions[questionNumber][2]);
        optionB.setText(WelcomeActivity.questions[questionNumber][3]);
        optionC.setText(WelcomeActivity.questions[questionNumber][4]);
        optionD.setText(WelcomeActivity.questions[questionNumber][5]);
        //Initialiwe variables
        if(savedInstanceState != null){
            millis = savedInstanceState.getLong("KEY_COUNTER");
            isTimerOn = savedInstanceState.getBoolean("KEY_IS_TIMER_ON");
            halfLifelineCounter = savedInstanceState.getInt("KEY_HALF_COUNTER");
            hintCounter = savedInstanceState.getInt("KEY_HINT_COUNTER");
        } else {
            hintCounter = 0;
            halfLifelineCounter = 0;
            millis = 60000;
            isTimerOn = true;
        }
            //Set a countdown timer
            timer1 = new CountDownTimer(millis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    time.setText((String.valueOf(millisUntilFinished / 1000)));
                    if (millisUntilFinished < 6000) {
                        time.setTextColor(Color.RED);
                    }
                    millis -= 1000;
                    if(millis<=5 && !firstTimerCancelled){
                        String message = "1st timer - Time expired. Game is over. You can click new game to restart the game.";
                        createAlertDialog(message);
                    }
                }

                @Override
                public void onFinish() {

                }
            };

        timer2 = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText((String.valueOf(millisUntilFinished / 1000)));
                if (millisUntilFinished < 6000) {
                    time.setTextColor(Color.RED);
                    millis -= 1000;
                }
                if(millisUntilFinished<=5){
                    String message = "2nd timer - Time expired. Game is over. You can click new game to restart the game.";
                    createAlertDialog(message);
                }
            }
            @Override
            public void onFinish() {

            }
        };
        if(isTimerOn) timer1.start();
        else time.setText((String.valueOf(millis / 1000)));
        //Set the background theme according to the category chosen
        if (WelcomeActivity.category.equals("literature")) {
            root.setBackgroundColor(getResources().getColor(R.color.literature));
            half.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.lifeline_literature));
            showHint.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.lifeline_literature));
        } else if (WelcomeActivity.category.equals("cinema")) {
            root.setBackgroundColor(getResources().getColor(R.color.cinema));
            half.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.lifeline_cinema));
            showHint.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.lifeline_cinema));
        } else if (WelcomeActivity.category.equals("science")) {
            root.setBackgroundColor(getResources().getColor(R.color.science));
            half.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.lifeline_science));
            showHint.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.lifeline_science));
        }
        //Set the button which returns back to categories after poping up an alert dialog.
        backToCategories = findViewById(R.id.categories);
        backToCategories.setOnClickListener(new OnClickListener() {
            // The code in this method will be executed when the family category is clicked on.
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                builder.setMessage("Are you sure you want to quit the game?");
                builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent categoriesIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                        startActivity(categoriesIntent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.create();
                builder.show();

            }
        });
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("KEY_COUNTER", millis);
        outState.putInt("KEY_HINT_COUNTER", hintCounter);
        outState.putInt("KEY_HALF_COUNTER", halfLifelineCounter);
        outState.putInt("KEY_QUESTIONNUMBER", questionNumber);
        outState.putBoolean("KEY_IS_TIMER_ON", isTimerOn);
        outState.putBoolean("KEY_OPTA_IS_YELLOW", OptAIsYellow);
        outState.putBoolean("KEY_OPTB_IS_YELLOW", OptBIsYellow);
        outState.putBoolean("KEY_OPTC_IS_YELLOW", OptCIsYellow);
        outState.putBoolean("KEY_OPTD_IS_YELLOW", OptDIsYellow);
        outState.putBoolean("KEY_CORRECT_OPTION_IS_SHOWN", correctOptionIsShown);
        outState.putBoolean("KEY_WRONG_OPTION_IS_SHOWN", wrongOptionIsShown);
        outState.putBoolean("KEY_IS_NEXT_ENABLED", isNextEnabled);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        questionNumber = savedInstanceState.getInt("KEY_QUESTIONNUMBER");
        question.setText(WelcomeActivity.questions[questionNumber][0]);
        hint.setText(WelcomeActivity.questions[questionNumber][1]);
        optionA.setText(WelcomeActivity.questions[questionNumber][2]);
        optionB.setText(WelcomeActivity.questions[questionNumber][3]);
        optionC.setText(WelcomeActivity.questions[questionNumber][4]);
        optionD.setText(WelcomeActivity.questions[questionNumber][5]);
        halfLifelineCounter = savedInstanceState.getInt("KEY_HALF_COUNTER");
        hintCounter = savedInstanceState.getInt("KEY_HINT_COUNTER");
        millis = savedInstanceState.getLong("KEY_COUNTER");
        isTimerOn = savedInstanceState.getBoolean("KEY_IS_TIMER_ON");
        isNextEnabled = savedInstanceState.getBoolean("KEY_IS_NEXT_ENABLED");
        if(isNextEnabled) nextButton.setEnabled(true);
        OptAIsYellow = savedInstanceState.getBoolean("KEY_OPTA_IS_YELLOW");
        if( OptAIsYellow) optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
        OptBIsYellow = savedInstanceState.getBoolean("KEY_OPTB_IS_YELLOW");
        if( OptBIsYellow) optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
        OptCIsYellow = savedInstanceState.getBoolean("KEY_OPTC_IS_YELLOW");
        if( OptCIsYellow) optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
        OptDIsYellow = savedInstanceState.getBoolean("KEY_OPTD_IS_YELLOW");
        if( OptDIsYellow) optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
        correctOptionIsShown = savedInstanceState.getBoolean("KEY_CORRECT_OPTION_IS_SHOWN");
        if(correctOptionIsShown) {
            correctOption = (RadioButton) findViewById(WelcomeActivity.answers[questionNumber]);
            correctOption.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turngreen));
        }
        wrongOptionIsShown = savedInstanceState.getBoolean("KEY_WRONG_OPTION_IS_SHOWN");
        if(wrongOptionIsShown){
            wrongOption = (RadioButton) findViewById(options.getCheckedRadioButtonId());
            wrongOption.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnred));
        }
    }

    protected void showHint(View view) {
        if (hintCounter >= 1) {
            Toast.makeText(this, "You have already used hint.", Toast.LENGTH_SHORT).show();
            return;
        }
        hintCounter++;
        TextView hint = (TextView) findViewById(R.id.hint);
        hint.setVisibility(View.VISIBLE);
    }

    protected void halfTheOptions(View view) {
        if (halfLifelineCounter >= 1) {
            Toast.makeText(this, "You have already used 1/2 lifeline.", Toast.LENGTH_SHORT).show();
            return;
        }
        halfLifelineCounter++;
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            list.add(WelcomeActivity.wrongAnswers[questionNumber][i]);
        }
        Random rand = new Random();
        int index = rand.nextInt(list.size());
        int option_to_erase_1 = list.get(index);
        list.remove(index);
        int option_to_erase_2 = list.get(rand.nextInt(list.size()));
        RadioButton but1 = (RadioButton) findViewById(option_to_erase_1);
        RadioButton but2 = (RadioButton) findViewById(option_to_erase_2);
        but1.setVisibility(View.INVISIBLE);
        but2.setVisibility(View.INVISIBLE);

    }

    protected void turnYellow(View view) {
        if (optionA.isChecked()) {
            optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
            OptAIsYellow = true;
            optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
            OptBIsYellow = false;
            optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
            OptCIsYellow = false;
            optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
            OptDIsYellow = false;

        }
        if (optionB.isChecked()) {
            optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
            OptBIsYellow = true;
            optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
            OptAIsYellow = false;
            optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
            OptCIsYellow = false;
            optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
            OptDIsYellow = false;
        }
        if (optionC.isChecked()) {
            optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
            OptCIsYellow = true;
            optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
            OptAIsYellow = false;
            optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
            OptBIsYellow = false;
            optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
            OptDIsYellow = false;
        }
        if (optionD.isChecked()) {
            optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
            OptDIsYellow = true;
            optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
            OptAIsYellow = false;
            optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
            OptBIsYellow = false;
            optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
            OptCIsYellow = false;
        }

    }

    protected void checkTheAnswer(View view) {
        if (!(optionA.isChecked()) && !(optionB.isChecked()) && !(optionC.isChecked()) && !(optionD.isChecked())) {
            Toast.makeText(this, "You didn't chose any of the options.", Toast.LENGTH_SHORT).show();
            return;
        }
        correctOption = (RadioButton) findViewById(WelcomeActivity.answers[questionNumber]);
        correctOption.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turngreen));
        correctOptionIsShown = true;
        timer1.cancel();
        timer2.cancel();
        firstTimerCancelled = true;
        isTimerOn = false;
        if (options.getCheckedRadioButtonId() == WelcomeActivity.answers[questionNumber]) {
            if (questionNumber == 4) {
                String message = "Congratulations! You successfully completed the quiz. If you would like compete in another category click new game.";
                createAlertDialog(message);
            } else {
                nextButton.setEnabled(true);
                isNextEnabled = true;
                Toast.makeText(this, "Correct! Click next to continue.", Toast.LENGTH_SHORT).show();
            }
        } else {
            wrongOption = (RadioButton) findViewById(options.getCheckedRadioButtonId());
            wrongOption.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnred));
            wrongOptionIsShown = true;
            //Wait two seconds before opening the dialog so that user sees the right answer
            CountDownTimer waitTwoSeconds = new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    String message = "The answer was wrong. Game is over.";
                    createAlertDialog(message);
                }
            };
            waitTwoSeconds.start();
        }
    }

    public void createAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        builder.setMessage(message);
        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent categoriesIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                // Start the new activity
                startActivity(categoriesIntent);
            }
        });
        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent goToHome = new Intent(Intent.ACTION_MAIN);
                goToHome.addCategory(Intent.CATEGORY_HOME);
                startActivity(goToHome);
            }
        });
        builder.create();
        builder.show();
    }

    protected void setNextQuestion(View view) {
        questionNumber++;
        question.setText(WelcomeActivity.questions[questionNumber][0]);
        hint.setText(WelcomeActivity.questions[questionNumber][1]);
        optionA.setText(WelcomeActivity.questions[questionNumber][2]);
        optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
        optionA.setVisibility(View.VISIBLE);
        optionB.setText(WelcomeActivity.questions[questionNumber][3]);
        optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
        optionB.setVisibility(View.VISIBLE);
        optionC.setText(WelcomeActivity.questions[questionNumber][4]);
        optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
        optionC.setVisibility(View.VISIBLE);
        optionD.setText(WelcomeActivity.questions[questionNumber][5]);
        optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
        optionD.setVisibility(View.VISIBLE);
        hint.setVisibility(View.INVISIBLE);
        time.setTextColor(Color.WHITE);
        options.clearCheck();
        nextButton.setEnabled(false);
        isNextEnabled = false;
        millis = 60000;
        timer2.start();
        isTimerOn = true;
        correctOptionIsShown = false;
        wrongOptionIsShown = false;
    }


}
