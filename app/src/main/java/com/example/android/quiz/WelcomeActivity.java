package com.example.android.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

/**
 * Created by Oya on 12-12-17.
 */



public class WelcomeActivity extends AppCompatActivity {

    public static String questions[][]= new String [5][6];
    public static int answers[]= new int [5];
    public static int wrongAnswers[][] = new int [5][3];
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
    }

    public void startQuiz(View view){
        RadioButton literature = (RadioButton) findViewById(R.id.literatureButton);
        RadioButton cinema = (RadioButton) findViewById(R.id.cinemaButton);
        if(literature.isChecked()){
            questions = QuestionLists.literatureQuestions;
            answers = QuestionLists.literatureCorrectAnswers;
            wrongAnswers = QuestionLists.literatureWrongAnswers;
        }
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
