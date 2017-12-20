package com.example.android.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    public static int answers[]= new int [5];
    public static int wrongAnswers[][] = new int [5][3];
    public static String category;

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
            Toast.makeText(this, "You didn't chose any of the categories.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(literature.isChecked()){
            questions = QuestionLists.literatureQuestions;
            answers = QuestionLists.literatureCorrectAnswers;
            wrongAnswers = QuestionLists.literatureWrongAnswers;
            category = "literature";
        } else if (cinema.isChecked()) {
            questions = QuestionLists.cinemaQuestions;
            answers = QuestionLists.cinemaCorrectAnswers;
            wrongAnswers = QuestionLists.cinemaWrongAnswers;
            category = "cinema";
        } else if(science.isChecked()) {
            questions = QuestionLists.scienceQuestions;
            answers = QuestionLists.scienceCorrectAnswers;
            wrongAnswers = QuestionLists.scienceWrongAnswers;
            category = "science";
        }

        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
