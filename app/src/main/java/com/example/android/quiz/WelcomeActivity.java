package com.example.android.quiz;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    private static String questions[][]= new String [5][6];
    private static int correctAnswers[]= new int [5];
    private static int wrongAnswers[][] = new int [5][3];
    private static String category;
    private final int[] literatureCorrectAnswers = {
            R.id.optionD, R.id.optionB, R.id.optionC, R.id.optionB, R.id.optionA,
    };
    private final int[][] literatureWrongAnswers = {
            {R.id.optionA, R.id.optionB, R.id.optionC},
            {R.id.optionA, R.id.optionC, R.id.optionD},
            {R.id.optionA, R.id.optionB, R.id.optionD},
            {R.id.optionA, R.id.optionC, R.id.optionD},
            {R.id.optionB, R.id.optionC, R.id.optionD},
    };
    private final int[] cinemaCorrectAnswers = {
            R.id.optionA, R.id.optionD, R.id.optionB, R.id.optionA, R.id.optionC
    };
    private final int[][] cinemaWrongAnswers = {
            {R.id.optionB, R.id.optionC, R.id.optionD},
            {R.id.optionA, R.id.optionB, R.id.optionC},
            {R.id.optionA, R.id.optionC, R.id.optionD},
            {R.id.optionB, R.id.optionC, R.id.optionD},
            {R.id.optionA, R.id.optionB, R.id.optionD}
    };
    private final int[] scienceCorrectAnswers = {
            R.id.optionA, R.id.optionA, R.id.optionC, R.id.optionD, R.id.optionB
    };
    private final int[][] scienceWrongAnswers = {
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
        Button openFeedback = findViewById(R.id.openFeedback);
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

    protected void startQuiz(View view){
        RadioButton literature = findViewById(R.id.literatureButton);
        RadioButton cinema = findViewById(R.id.cinemaButton);
        RadioButton science = findViewById(R.id.scienceButton);
        //warns if nothing is checked
        if (!(literature.isChecked()) && !(cinema.isChecked()) && !(science.isChecked())) {
            Toast.makeText(this, R.string.chosenothingwarning, Toast.LENGTH_SHORT).show();
            return;
        }
        //assign the questions according to the category chosen
        if(literature.isChecked()){
            extractTypedArray(R.array.literature_questions);
            correctAnswers = literatureCorrectAnswers;
            wrongAnswers = literatureWrongAnswers;
            category = getString(R.string.literature);
        } else if (cinema.isChecked()) {
            extractTypedArray(R.array.cinema_questions);
            correctAnswers = cinemaCorrectAnswers;
            wrongAnswers = cinemaWrongAnswers;
            category = getString(R.string.cinema);
        } else if(science.isChecked()) {
            extractTypedArray(R.array.science_questions);
            correctAnswers = scienceCorrectAnswers;
            wrongAnswers = scienceWrongAnswers;
            category = getString(R.string.science);
        }
        startActivity(new Intent(this, MainActivity.class));
    }

    //Extract the question, hints and options from array.xml
    private void extractTypedArray(int arrayId){
        Resources resources = getResources();
        TypedArray typedArray = resources.obtainTypedArray(arrayId);
        int length = typedArray.length();
        for (int i = 0; i < length; ++i) {
            int id = typedArray.getResourceId(i, 0);
            questions[i] = resources.getStringArray(id);
        }
        typedArray.recycle();
    }

    public static String[][] getQuestions(){
        return questions;
    }

    public static int[] getCorrectAnswers(){
        return correctAnswers;
    }

    public static int[][] getWrongAnswers(){
        return wrongAnswers;
    }

    public static String getCategory(){
        return category;
    }
}
