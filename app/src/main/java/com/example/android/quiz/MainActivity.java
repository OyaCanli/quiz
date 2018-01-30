package com.example.android.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private String[][] questions;
    private int[] correctAnswers;
    private int[][] wrongAnswers;
    private TextView question, hint, time;
    private RadioButton optionA, optionB, optionC, optionD, correctOption, wrongOption;
    private RadioGroup options;
    private RadioButton butToErase1, butToErase2;
    private Button nextButton, showHint, half;
    private CountDownTimer timer;
    private int questionNumber, hintCounter, halfLifelineCounter, option_to_erase_1, option_to_erase_2 ;
    private long currentMillis;
    private boolean isPaused, isNextEnabled, correctOptionIsShown, wrongOptionIsShown;
    private boolean isHalfLifeLineActif, isHintVisible;
    RippleDrawable rippleHalf, rippleHint;

    final static String CURRENT_MILLIS = "SavedStateOfCurrentMillis";
    final static String QUESTIONNUMBER = "SavedStateOfQuestionNumber";
    final static String HINT_COUNTER = "SavedStateOfHintCounter";
    final static String HALF_COUNTER = "SavedStateOfHalfCounter";
    final static String IS_NEXT_ENABLED= "SavedStateOfIsNextEnabled";
    final static String IS_HALF_ACTIF = "SavedStateOfIsHalfLifeLineActif";
    final static String OPT_TO_ERASE_1 = "SavedStateOfOptToErase1";
    final static String OPT_TO_ERASE_2 = "SavedStateOfOptToErase2";
    final static String CORRECT_OPTION_IS_SHOWN = "SavedStateOfCorrectOptionIsShown";
    final static String WRONG_OPTION_IS_SHOWN = "SavedStateOfWrongOptionIsShown";
    final static String IS_PAUSED = "SavedStateOfIsPaused";
    final static String IS_HINT_VISIBLE = "SavedStateOfIsHintVisible";
    final static String WRONG_OPTION_ID = "wrongOptionId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hide action bar
        if(Build.VERSION.SDK_INT >= 21){
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_main));
        }
        getSupportActionBar().hide();
        //Initialize views
        question = findViewById(R.id.question);
        hint = findViewById(R.id.hint);
        optionA = findViewById(R.id.optionA);
        optionB =  findViewById(R.id.optionB);
        optionC =  findViewById(R.id.optionC);
        optionD =  findViewById(R.id.optionD);
        options = findViewById(R.id.options);
        time = findViewById(R.id.time);
        //Initialize buttons
        showHint = findViewById(R.id.showHint);
        half = findViewById(R.id.half);
        Button backToCategories = findViewById(R.id.categories);
        Button submit = findViewById(R.id.submit);
        nextButton = findViewById(R.id.next);
        //set clicklisteners on these buttons
        showHint.setOnClickListener(this);
        half.setOnClickListener(this);
        backToCategories.setOnClickListener(this);
        submit.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        //get questions from welcome activity
        questions = WelcomeActivity.getQuestions();
        correctAnswers = WelcomeActivity.getCorrectAnswers();
        wrongAnswers = WelcomeActivity.getWrongAnswers();
        String category = WelcomeActivity.getCategory();
        //Set the background theme according to the category chosen
        RelativeLayout root = findViewById(R.id.root);
        ShapeDrawable roundButton = new ShapeDrawable();
        roundButton.setShape(new OvalShape());
        if (category.equals(getString(R.string.literature))) {
            root.setBackgroundColor(getResources().getColor(R.color.literature));
            roundButton.getPaint().setColor(getResources().getColor(R.color.literature));
            //Add ripple effect for new phones and simple circles for old phones
            if (Build.VERSION.SDK_INT >= 21){
                rippleHalf = new RippleDrawable(ColorStateList.valueOf(getResources().getColor(R.color.ripplecolor)), roundButton, null);
                rippleHint = new RippleDrawable(ColorStateList.valueOf(getResources().getColor(R.color.ripplecolor)), roundButton, null);
                half.setBackground(rippleHalf);
                showHint.setBackground(rippleHint);
            } else {
                half.setBackground(roundButton);
                showHint.setBackground(roundButton);
            }
        } else if (category.equals(getString(R.string.cinema))) {
            root.setBackgroundColor(getResources().getColor(R.color.cinema));;
            roundButton.getPaint().setColor(getResources().getColor(R.color.cinema));
            if (Build.VERSION.SDK_INT >= 21){
                rippleHalf = new RippleDrawable(ColorStateList.valueOf(getResources().getColor(R.color.ripplecolor)), roundButton, null);
                rippleHint = new RippleDrawable(ColorStateList.valueOf(getResources().getColor(R.color.ripplecolor)), roundButton, null);
                half.setBackground(rippleHalf);
                showHint.setBackground(rippleHint);
            } else {
                half.setBackground(roundButton);
                showHint.setBackground(roundButton);
            }

        } else if (category.equals(getString(R.string.science))) {
            root.setBackgroundColor(getResources().getColor(R.color.science));;
            roundButton.getPaint().setColor(getResources().getColor(R.color.science));
            if (Build.VERSION.SDK_INT >= 21) {
                rippleHalf = new RippleDrawable(ColorStateList.valueOf(getResources().getColor(R.color.ripplecolor)), roundButton, null);
                rippleHint = new RippleDrawable(ColorStateList.valueOf(getResources().getColor(R.color.ripplecolor)), roundButton, null);
                half.setBackground(rippleHalf);
                showHint.setBackground(rippleHint);
            } else {
                half.setBackground(roundButton);
                showHint.setBackground(roundButton);
            }
        }
        //Set the first question
        question.setText(questions[questionNumber][0]);
        hint.setText(questions[questionNumber][1]);
        optionA.setText(questions[questionNumber][2]);
        optionB.setText(questions[questionNumber][3]);
        optionC.setText(questions[questionNumber][4]);
        optionD.setText(questions[questionNumber][5]);
        //Initialize variables
        if(savedInstanceState == null) {
            questionNumber = 0;
            hintCounter = 0;
            halfLifelineCounter = 0;
            currentMillis = 60000;
            setTimer(currentMillis);
        } else {
            questionNumber = savedInstanceState.getInt(QUESTIONNUMBER);
            question.setText(questions[questionNumber][0]);
            hint.setText(questions[questionNumber][1]);
            optionA.setText(questions[questionNumber][2]);
            optionB.setText(questions[questionNumber][3]);
            optionC.setText(questions[questionNumber][4]);
            optionD.setText(questions[questionNumber][5]);
            halfLifelineCounter = savedInstanceState.getInt(HALF_COUNTER);
            hintCounter = savedInstanceState.getInt(HINT_COUNTER);
            currentMillis = savedInstanceState.getLong(CURRENT_MILLIS);
            isPaused = savedInstanceState.getBoolean(IS_PAUSED);
            if(isPaused){
                hint.setEnabled(false);
                half.setEnabled(false);
                for(int i = 0; i<4; i++){
                    options.getChildAt(i).setEnabled(false);
                }
                time.setText((String.valueOf(currentMillis / 1000)));
            } else {
                setTimer(currentMillis);
            }
            isNextEnabled = savedInstanceState.getBoolean(IS_NEXT_ENABLED);
            if(isNextEnabled) nextButton.setEnabled(true);
            isHintVisible = savedInstanceState.getBoolean(IS_HINT_VISIBLE);
            if(isHintVisible) hint.setVisibility(View.VISIBLE);
            correctOptionIsShown = savedInstanceState.getBoolean(CORRECT_OPTION_IS_SHOWN);
             if (correctOptionIsShown) {
                correctOption = findViewById(correctAnswers[questionNumber]);
                ShapeDrawable greenBackground = new ShapeDrawable();
                float[] radius= {30,30,30,30,30,30,30,30};
                greenBackground.setShape(new RoundRectShape(radius, null, null));
                greenBackground.getPaint().setColor(Color.GREEN);
                correctOption.setBackgroundDrawable(greenBackground);
            }
            wrongOptionIsShown = savedInstanceState.getBoolean(WRONG_OPTION_IS_SHOWN);
            if (wrongOptionIsShown) {
                wrongOption = findViewById(savedInstanceState.getInt(WRONG_OPTION_ID));
                ShapeDrawable redBackground = new ShapeDrawable();
                float[] radius= {30,30,30,30,30,30,30,30};
                redBackground.setShape(new RoundRectShape(radius, null, null));
                redBackground.getPaint().setColor(Color.RED);
                wrongOption.setBackgroundDrawable(redBackground);
            }
            isHalfLifeLineActif = savedInstanceState.getBoolean(IS_HALF_ACTIF);
            option_to_erase_1 = savedInstanceState.getInt(OPT_TO_ERASE_1);
            option_to_erase_2 = savedInstanceState.getInt(OPT_TO_ERASE_2);
            if (isHalfLifeLineActif) {
                butToErase1 =  findViewById(option_to_erase_1);
                butToErase2 = findViewById(option_to_erase_2);
                butToErase1.setVisibility(View.INVISIBLE);
                butToErase2.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.showHint:{
                showHint();
                break;
            }
            case R.id.half:{
                halfTheOptions();
                break;
            }
            case R.id.categories:{
                goBackToCategories();
                break;
            }
            case R.id.submit: {
                checkTheAnswer();
                break;
            }
            case R.id.next: {
                setNextQuestion();
                break;
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(CURRENT_MILLIS, currentMillis);
        outState.putInt(HINT_COUNTER, hintCounter);
        outState.putInt(HALF_COUNTER, halfLifelineCounter);
        outState.putInt(QUESTIONNUMBER, questionNumber);
        outState.putBoolean(IS_HALF_ACTIF, isHalfLifeLineActif);
        outState.putInt(OPT_TO_ERASE_1, option_to_erase_1);
        outState.putInt(OPT_TO_ERASE_2, option_to_erase_2);
        outState.putBoolean(CORRECT_OPTION_IS_SHOWN, correctOptionIsShown);
        outState.putBoolean(WRONG_OPTION_IS_SHOWN, wrongOptionIsShown);
        outState.putInt(WRONG_OPTION_ID, options.getCheckedRadioButtonId());
        outState.putBoolean(IS_PAUSED, isPaused);
        outState.putBoolean(IS_NEXT_ENABLED, isNextEnabled);
        outState.putBoolean(IS_HINT_VISIBLE, isHintVisible);
    }

    public void setTimer(long millis){
        if(timer != null) timer.cancel();
        timer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText((String.valueOf(millisUntilFinished / 1000)));
                currentMillis = millisUntilFinished;
                if (millisUntilFinished < 6000) {
                    time.setTextColor(Color.RED);
                }
            }
            @Override
            public void onFinish() {
                String message = getString(R.string.timeoutwarning);
                createAlertDialog(message);
            }
        }.start();
    }

    public void goBackToCategories(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        builder.setMessage(R.string.exitwarning);
        builder.setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent categoriesIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(categoriesIntent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create();
        builder.show();
    }

    public void showHint() {
        //can use hint only once
        if (hintCounter >= 1) {
            Toast.makeText(this, R.string.hintwarning, Toast.LENGTH_SHORT).show();
            return;
        }
        hintCounter++;
        TextView hint =  findViewById(R.id.hint);
        hint.setVisibility(View.VISIBLE);
        isHintVisible = true;
    }

    public void halfTheOptions() {
        //can use this lifeline only once
        if (halfLifelineCounter >= 1) {
            Toast.makeText(this, R.string.halfwarning, Toast.LENGTH_SHORT).show();
            return;
        }
        halfLifelineCounter++;
        //I make a temporary arraylist from the wrong answers of the question.
        //Pick one random, remove it from the list.
        //Pick another. Then make those two options invisible
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            list.add(wrongAnswers[questionNumber][i]);
        }
        Random rand = new Random();
        int index = rand.nextInt(list.size());
        option_to_erase_1 = list.get(index);
        list.remove(index);
        option_to_erase_2 = list.get(rand.nextInt(list.size()));
        butToErase1 = findViewById(option_to_erase_1);
        butToErase2 = findViewById(option_to_erase_2);
        butToErase1.setVisibility(View.INVISIBLE);
        butToErase2.setVisibility(View.INVISIBLE);
        isHalfLifeLineActif = true;
    }

    public void checkTheAnswer() {
        //warn if nothing is chosen
        if (!(optionA.isChecked()) && !(optionB.isChecked()) && !(optionC.isChecked()) && !(optionD.isChecked())) {
            Toast.makeText(this, R.string.chosenothingwarning, Toast.LENGTH_SHORT).show();
            return;
        }
        //show correct option in green
        correctOption = findViewById(correctAnswers[questionNumber]);
        ShapeDrawable greenBackground = new ShapeDrawable();
        float[] radius= {30,30,30,30,30,30,30,30};
        greenBackground.setShape(new RoundRectShape(radius, null, null));
        greenBackground.getPaint().setColor(Color.GREEN);
        correctOption.setBackgroundDrawable(greenBackground);
        correctOptionIsShown = true;
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking_animation);
        correctOption.startAnimation(animation);
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                correctOption.clearAnimation();
            }
        }, 2000);
        //disable lifelines temporarily until the next question
        showHint.setEnabled(false);
        isPaused = true;
        half.setEnabled(false);
        for(int i = 0; i<4; i++){
            options.getChildAt(i).setEnabled(false);
        }
        //cancel timer
        timer.cancel();
        //if the answer is correct
        if (options.getCheckedRadioButtonId() == correctAnswers[questionNumber]) {
            if (questionNumber == 4) { //if it was the last question
                String message = getString(R.string.congratulations);
                createAlertDialog(message);
            } else { //if it was not the last question
                nextButton.setEnabled(true);
                isNextEnabled = true;
                Toast.makeText(this, R.string.correcttoastmessage, Toast.LENGTH_SHORT).show();
            }
        } else { //if it was a wrong answer
            wrongOption =  findViewById(options.getCheckedRadioButtonId());
            ShapeDrawable redBackground = new ShapeDrawable();
            redBackground.setShape(new RoundRectShape(radius, null, null));
            redBackground.getPaint().setColor(Color.RED);
            wrongOption.setBackgroundDrawable(redBackground);
            wrongOptionIsShown = true;
            //Wait two seconds before opening the dialog so that user sees the right answer
            CountDownTimer waitTwoSeconds = new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    String message = getString(R.string.wronganswer);
                    createAlertDialog(message);
                }
            };
            waitTwoSeconds.start();
        }
    }

    public void createAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.newgame, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent categoriesIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                // Start the new activity
                startActivity(categoriesIntent);
            }
        });
        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent goToHome = new Intent(Intent.ACTION_MAIN);
                goToHome.addCategory(Intent.CATEGORY_HOME);
                startActivity(goToHome);
            }
        });
        builder.create();
        builder.show();
    }

    public void setNextQuestion() {
        questionNumber++;
        question.setText(questions[questionNumber][0]);
        hint.setText(questions[questionNumber][1]);
        optionA.setText(questions[questionNumber][2]);
        optionB.setText(questions[questionNumber][3]);
        optionC.setText(questions[questionNumber][4]);
        optionD.setText(questions[questionNumber][5]);
        for(int i = 0; i<4; i++){
            options.getChildAt(i).setVisibility(View.VISIBLE);
            options.getChildAt(i).setEnabled(true);
            options.getChildAt(i).setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selector));
        }
        hint.setVisibility(View.INVISIBLE);
        isHintVisible = false;
        time.setTextColor(Color.WHITE);
        options.clearCheck();
        nextButton.setEnabled(false);
        isNextEnabled = false;
        showHint.setEnabled(true);
        isPaused = true;
        half.setEnabled(true);
        setTimer(60000);
        correctOption.clearAnimation();
        correctOptionIsShown = false;
        wrongOptionIsShown = false;
        isHalfLifeLineActif = false;
    }

    public void onDestroy(){
        if(timer != null) timer.cancel();
        super.onDestroy();
    }
}
