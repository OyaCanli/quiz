package com.example.android.quiz;

import android.content.DialogInterface;
import android.content.Intent;
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
    private boolean isTimerOn, isNextEnabled, correctOptionIsShown, wrongOptionIsShown;
    private boolean OptAIsYellow, OptBIsYellow, OptCIsYellow, OptDIsYellow;
    private boolean isHalfLifeLineActif, isHintVisible, isHintEnabled, isHalfEnabled;

    static final String KEY_CURRENT_MILLIS = "SavedStateOfCurrentMillis";
    static final String KEY_QUESTIONNUMBER = "SavedStateOfQuestionNumber";
    static final String KEY_HINT_COUNTER = "SavedStateOfHintCounter";
    static final String KEY_HALF_COUNTER = "SavedStateOfHalfCounter";
    static final String KEY_IS_TIMER_ON = "SavedStateOfIsTimerOn";
    static final String KEY_OPTA_IS_YELLOW = "SavedStateOfOptAIsYellow";
    static final String KEY_OPTB_IS_YELLOW = "SavedStateOfOptBIsYellow";
    static final String KEY_OPTC_IS_YELLOW = "SavedStateOfOptCIsYellow";
    static final String KEY_OPTD_IS_YELLOW = "SavedStateOfOptDIsYellow";
    static final String KEY_IS_HALF_ACTIF = "SavedStateOfIsHalfLifeLineActif";
    static final String KEY_OPT_TO_ERASE_1 = "SavedStateOfOptToErase1";
    static final String KEY_OPT_TO_ERASE_2 = "SavedStateOfOptToErase2";
    static final String KEY_CORRECT_OPTION_IS_SHOWN = "SavedStateOfCorrectOptionIsShown";
    static final String KEY_WRONG_OPTION_IS_SHOWN = "SavedStateOfWrongOptionIsShown";
    static final String KEY_IS_NEXT_ENABLED = "SavedStateOfIsNextEnabled";
    static final String KEY_IS_HINT_ENABLED = "SavedStateOfIsHintEnabled";
    static final String KEY_IS_HALF_ENABLED = "SavedStateOfIsHalfEnabled";
    static final String KEY_IS_HINT_VISIBLE = "SavedStateOfIsHintVisible";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hide action bar
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
        //Initialiwe buttons
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
        optionA.setOnClickListener(this);
        optionB.setOnClickListener(this);
        optionC.setOnClickListener(this);
        optionD.setOnClickListener(this);
        //get questions from welcome activity
        questions = WelcomeActivity.getQuestions();
        correctAnswers = WelcomeActivity.getCorrectAnswers();
        wrongAnswers = WelcomeActivity.getWrongAnswers();
        String category = WelcomeActivity.getCategory();
        //Set the first question
        question.setText(questions[questionNumber][0]);
        hint.setText(questions[questionNumber][1]);
        optionA.setText(questions[questionNumber][2]);
        optionB.setText(questions[questionNumber][3]);
        optionC.setText(questions[questionNumber][4]);
        optionD.setText(questions[questionNumber][5]);
        //Initialiwe variables
        if(savedInstanceState != null){
            currentMillis = savedInstanceState.getLong(KEY_CURRENT_MILLIS);
            isTimerOn = savedInstanceState.getBoolean(KEY_IS_TIMER_ON);
            halfLifelineCounter = savedInstanceState.getInt(KEY_HALF_COUNTER);
            hintCounter = savedInstanceState.getInt(KEY_HINT_COUNTER);
        } else {
            hintCounter = 0;
            halfLifelineCounter = 0;
            currentMillis = 60000;
            isTimerOn = true;
        }
        if(isTimerOn) setTimer(currentMillis);
        else time.setText((String.valueOf(currentMillis / 1000)));
        //Set the background theme according to the category chosen
        RelativeLayout root = findViewById(R.id.root);
        if (category.equals(getString(R.string.literature))) {
            root.setBackgroundColor(getResources().getColor(R.color.literature));
            half.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.rippleliterature));
            showHint.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.rippleliterature));
        } else if (category.equals(getString(R.string.cinema))) {
            root.setBackgroundColor(getResources().getColor(R.color.cinema));
            half.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.ripplecinema));
            showHint.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.ripplecinema));
        } else if (category.equals(getString(R.string.science))) {
            root.setBackgroundColor(getResources().getColor(R.color.science));
            half.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.ripplescience));
            showHint.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.ripplescience));
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
            case R.id.optionA:case R.id.optionB:case R.id.optionC:case R.id.optionD: {
                turnYellow();
                break;
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_CURRENT_MILLIS, currentMillis);
        outState.putInt(KEY_HINT_COUNTER, hintCounter);
        outState.putInt(KEY_HALF_COUNTER, halfLifelineCounter);
        outState.putInt(KEY_QUESTIONNUMBER, questionNumber);
        outState.putBoolean(KEY_IS_TIMER_ON, isTimerOn);
        outState.putBoolean(KEY_OPTA_IS_YELLOW, OptAIsYellow);
        outState.putBoolean(KEY_OPTB_IS_YELLOW, OptBIsYellow);
        outState.putBoolean(KEY_OPTC_IS_YELLOW, OptCIsYellow);
        outState.putBoolean(KEY_OPTD_IS_YELLOW, OptDIsYellow);
        outState.putBoolean(KEY_IS_HALF_ACTIF, isHalfLifeLineActif);
        outState.putInt(KEY_OPT_TO_ERASE_1, option_to_erase_1);
        outState.putInt(KEY_OPT_TO_ERASE_2, option_to_erase_2);
        outState.putBoolean(KEY_CORRECT_OPTION_IS_SHOWN, correctOptionIsShown);
        outState.putBoolean(KEY_WRONG_OPTION_IS_SHOWN, wrongOptionIsShown);
        outState.putBoolean(KEY_IS_NEXT_ENABLED, isNextEnabled);
        outState.putBoolean(KEY_IS_HINT_ENABLED, isHintEnabled);
        outState.putBoolean(KEY_IS_HALF_ENABLED, isHalfEnabled);
        outState.putBoolean(KEY_IS_HINT_VISIBLE, isHintVisible);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        questionNumber = savedInstanceState.getInt(KEY_QUESTIONNUMBER);
        question.setText(questions[questionNumber][0]);
        hint.setText(questions[questionNumber][1]);
        optionA.setText(questions[questionNumber][2]);
        optionB.setText(questions[questionNumber][3]);
        optionC.setText(questions[questionNumber][4]);
        optionD.setText(questions[questionNumber][5]);
        halfLifelineCounter = savedInstanceState.getInt(KEY_HALF_COUNTER);
        hintCounter = savedInstanceState.getInt(KEY_HINT_COUNTER);
        currentMillis = savedInstanceState.getLong(KEY_CURRENT_MILLIS);
        isTimerOn = savedInstanceState.getBoolean(KEY_IS_TIMER_ON);
        if(isTimerOn) setTimer(currentMillis);
        isNextEnabled = savedInstanceState.getBoolean(KEY_IS_NEXT_ENABLED);
        if (isNextEnabled) nextButton.setEnabled(true);
        isHintEnabled = savedInstanceState.getBoolean(KEY_IS_HINT_ENABLED);
        if(!isHintEnabled) hint.setEnabled(false);
        isHalfEnabled = savedInstanceState.getBoolean(KEY_IS_HALF_ENABLED);
        if(!isHalfEnabled) half.setEnabled(false);
        isHintVisible = savedInstanceState.getBoolean(KEY_IS_HINT_VISIBLE);
        if(isHintVisible) hint.setVisibility(View.VISIBLE);
        OptAIsYellow = savedInstanceState.getBoolean(KEY_OPTA_IS_YELLOW);
        if (OptAIsYellow)
            optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
        OptBIsYellow = savedInstanceState.getBoolean(KEY_OPTB_IS_YELLOW);
        if (OptBIsYellow)
            optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
        OptCIsYellow = savedInstanceState.getBoolean(KEY_OPTC_IS_YELLOW);
        if (OptCIsYellow)
            optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
        OptDIsYellow = savedInstanceState.getBoolean(KEY_OPTD_IS_YELLOW);
        if (OptDIsYellow)
            optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
        correctOptionIsShown = savedInstanceState.getBoolean(KEY_CORRECT_OPTION_IS_SHOWN);
        if (correctOptionIsShown) {
            correctOption = findViewById(correctAnswers[questionNumber]);
            correctOption.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turngreen));
        }
        wrongOptionIsShown = savedInstanceState.getBoolean(KEY_WRONG_OPTION_IS_SHOWN);
        if (wrongOptionIsShown) {
            wrongOption = findViewById(options.getCheckedRadioButtonId());
            wrongOption.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnred));
        }
        isHalfLifeLineActif = savedInstanceState.getBoolean(KEY_IS_HALF_ACTIF);
        option_to_erase_1 = savedInstanceState.getInt(KEY_OPT_TO_ERASE_1);
        option_to_erase_2 = savedInstanceState.getInt(KEY_OPT_TO_ERASE_2);
        if (isHalfLifeLineActif) {
            butToErase1 =  findViewById(option_to_erase_1);
            butToErase2 = findViewById(option_to_erase_2);
            butToErase1.setVisibility(View.INVISIBLE);
            butToErase2.setVisibility(View.INVISIBLE);
        }
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

    public void turnYellow() {
        //options turn yellow on click. If another is clicked, others return back to their initial color.
        int id = options.getCheckedRadioButtonId();
        switch(id){
            case R.id.optionA: {
                optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
                OptAIsYellow = true;
                optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
                OptBIsYellow = false;
                optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
                OptCIsYellow = false;
                optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
                OptDIsYellow = false;
                break;

            }
            case R.id.optionB: {
                optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
                OptBIsYellow = true;
                optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
                OptAIsYellow = false;
                optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
                OptCIsYellow = false;
                optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
                OptDIsYellow = false;
                break;
            }
            case R.id.optionC: {
                optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
                OptCIsYellow = true;
                optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
                OptAIsYellow = false;
                optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
                OptBIsYellow = false;
                optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
                OptDIsYellow = false;
                break;
            }
            case R.id.optionD: {
                optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnyellow));
                OptDIsYellow = true;
                optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
                OptAIsYellow = false;
                optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
                OptBIsYellow = false;
                optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
                OptCIsYellow = false;
                break;
            }
        }


    }

    public void checkTheAnswer() {
        //warn if nothing is chosen
        if (!(optionA.isChecked()) && !(optionB.isChecked()) && !(optionC.isChecked()) && !(optionD.isChecked())) {
            Toast.makeText(this, R.string.chosenothingwarning, Toast.LENGTH_SHORT).show();
            return;
        }
        //show correct option in green
        correctOption = findViewById(correctAnswers[questionNumber]);
        correctOption.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turngreen));
        correctOptionIsShown = true;
        //disable lifelines temporarily until the next question
        showHint.setEnabled(false);
        isHintEnabled = false;
        half.setEnabled(false);
        isHalfEnabled = false;
        //cancel timer
        timer.cancel();
        isTimerOn = false;
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
            wrongOption.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.turnred));
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
        optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
        optionA.setVisibility(View.VISIBLE);
        optionB.setText(questions[questionNumber][3]);
        optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
        optionB.setVisibility(View.VISIBLE);
        optionC.setText(questions[questionNumber][4]);
        optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
        optionC.setVisibility(View.VISIBLE);
        optionD.setText(questions[questionNumber][5]);
        optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.optionsbackground));
        optionD.setVisibility(View.VISIBLE);
        hint.setVisibility(View.INVISIBLE);
        isHintVisible = false;
        time.setTextColor(Color.WHITE);
        options.clearCheck();
        nextButton.setEnabled(false);
        isNextEnabled = false;
        showHint.setEnabled(true);
        isHintEnabled = true;
        half.setEnabled(true);
        isHalfEnabled = true;
        setTimer(60000);
        isTimerOn = true;
        correctOptionIsShown = false;
        wrongOptionIsShown = false;
        OptAIsYellow = false;
        OptBIsYellow = false;
        OptCIsYellow = false;
        OptDIsYellow = false;
        isHalfLifeLineActif = false;
    }

    public void onDestroy(){
        super.onDestroy();
        if(timer != null) timer.cancel();
    }
}
