package com.example.android.quiz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Oya on 12-12-17.
 */

public class WelcomeActivity extends AppCompatActivity {

    public static String questions[][]= new String [5][6];
    public static int correctAnswers[]= new int [5];
    public static int wrongAnswers[][] = new int [5][3];
    public static String category;
    public static final int[] literatureCorrectAnswers = {
            R.id.optionD, R.id.optionB, R.id.optionC, R.id.optionB, R.id.optionA,
    };
    public static final int[][] literatureWrongAnswers = {
            {R.id.optionA, R.id.optionB, R.id.optionC},
            {R.id.optionA, R.id.optionC, R.id.optionD},
            {R.id.optionA, R.id.optionB, R.id.optionD},
            {R.id.optionA, R.id.optionC, R.id.optionD},
            {R.id.optionB, R.id.optionC, R.id.optionD},
    };
    public static final int[] cinemaCorrectAnswers = {
            R.id.optionA, R.id.optionD, R.id.optionB, R.id.optionA, R.id.optionC
    };
    public static final int[][] cinemaWrongAnswers = {
            {R.id.optionB, R.id.optionC, R.id.optionD},
            {R.id.optionA, R.id.optionB, R.id.optionC},
            {R.id.optionA, R.id.optionC, R.id.optionD},
            {R.id.optionB, R.id.optionC, R.id.optionD},
            {R.id.optionA, R.id.optionB, R.id.optionD}
    };
    public static final int[] scienceCorrectAnswers = {
            R.id.optionA, R.id.optionA, R.id.optionC, R.id.optionD, R.id.optionB
    };
    public static final int[][] scienceWrongAnswers = {
            {R.id.optionB, R.id.optionC, R.id.optionD},
            {R.id.optionB, R.id.optionC, R.id.optionD},
            {R.id.optionA, R.id.optionB, R.id.optionD},
            {R.id.optionA, R.id.optionB, R.id.optionC},
            {R.id.optionA, R.id.optionC, R.id.optionD},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        Button openFeedback = (Button) findViewById(R.id.openFeedback);
        openFeedback.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the family category is clicked on.
            @Override
            public void onClick(View view) {
                Intent feedbackIntent = new Intent(WelcomeActivity.this, FeedbackActivity.class);
                // Start the new activity
                startActivity(feedbackIntent);
            }
        });
    }

    public void startQuiz(View view){
        RadioButton literature = findViewById(R.id.literatureButton);
        RadioButton cinema = findViewById(R.id.cinemaButton);
        RadioButton science = findViewById(R.id.scienceButton);
        if (!(literature.isChecked()) && !(cinema.isChecked()) && !(science.isChecked())) {
            Toast.makeText(this, R.string.chosenothingwarning, Toast.LENGTH_SHORT).show();
            return;
        }
        Resources resources = getResources();

        if(literature.isChecked()){
            TypedArray typedArray = resources.obtainTypedArray(R.array.literature_questions);
            int length = typedArray.length();
            for (int i = 0; i < length; ++i) {
                int id = typedArray.getResourceId(i, 0);
                questions[i] = resources.getStringArray(id);
            }
            typedArray.recycle();
            correctAnswers = literatureCorrectAnswers;
            wrongAnswers = literatureWrongAnswers;
            category = getString(R.string.literature);
        } else if (cinema.isChecked()) {
            TypedArray typedArray = resources.obtainTypedArray(R.array.cinema_questions);
            int length = typedArray.length();
            for (int i = 0; i < length; ++i) {
                int id = typedArray.getResourceId(i, 0);
                questions[i] = resources.getStringArray(id);
            }
            typedArray.recycle();
            correctAnswers = cinemaCorrectAnswers;
            wrongAnswers = cinemaWrongAnswers;
            category = getString(R.string.cinema);
        } else if(science.isChecked()) {
            TypedArray typedArray = resources.obtainTypedArray(R.array.science_questions);
            int length = typedArray.length();
            for (int i = 0; i < length; ++i) {
                int id = typedArray.getResourceId(i, 0);
                questions[i] = resources.getStringArray(id);
            }
            typedArray.recycle();
            correctAnswers = scienceCorrectAnswers;
            wrongAnswers = scienceWrongAnswers;
            category = getString(R.string.science);
        }

        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
