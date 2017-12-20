package com.example.android.quiz;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int questionNumber;
    TextView question;
    TextView hint;
    TextView time;
    RadioButton optionA;
    RadioButton optionB;
    RadioButton optionC;
    RadioButton optionD;
    RadioGroup options;
    Button nextButton;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionNumber = 0;
        question = (TextView) findViewById(R.id.question);
        hint = (TextView) findViewById(R.id.hint);
        optionA = (RadioButton) findViewById(R.id.optionA);
        optionB = (RadioButton) findViewById(R.id.optionB);
        optionC = (RadioButton) findViewById(R.id.optionC);
        optionD = (RadioButton) findViewById(R.id.optionD);
        options = (RadioGroup) findViewById(R.id.options);
        nextButton = (Button) findViewById(R.id.next);
        time = (TextView) findViewById(R.id.time) ;
        timer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText((String.valueOf(millisUntilFinished/1000)));
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    protected void showHint(View view) {
        TextView hint = (TextView) findViewById(R.id.hint);
        hint.setVisibility(View.VISIBLE);
    }

    protected void halfTheOptions(View view) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i=0; i<3; i++){
            list.add(WelcomeActivity.wrongAnswers[questionNumber][i]);
        }
            Random rand = new Random();
            int index = rand.nextInt(list.size());
            int option_to_erase_1 = list.get(index);
            list.remove(index);
            int option_to_erase_2 = list.get(rand.nextInt(list.size()));
            RadioButton but1 = (RadioButton)findViewById(option_to_erase_1);
            RadioButton but2 = (RadioButton)findViewById(option_to_erase_2);
            but1.setVisibility(View.INVISIBLE);
            but2.setVisibility(View.INVISIBLE);

    }

    protected void turnYellow(View view){
        if(optionA.isChecked()){
            optionA.setBackgroundColor(Color.YELLOW);
            optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
            optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
            optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
        }
        if(optionB.isChecked()){
            optionB.setBackgroundColor(Color.YELLOW);
            optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
            optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
            optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
        }
        if(optionC.isChecked()){
            optionC.setBackgroundColor(Color.YELLOW);
            optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
            optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
            optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
        }
        if(optionD.isChecked()){
            optionD.setBackgroundColor(Color.YELLOW);
            optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
            optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
            optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
        }

    }

    protected void checkTheAnswer(View view){
        RadioButton correctOption = (RadioButton) findViewById(WelcomeActivity.answers[questionNumber]);
        correctOption.setBackgroundColor(Color.GREEN);
        timer.cancel();
        if(options.getCheckedRadioButtonId() == WelcomeActivity.answers[questionNumber]){
            nextButton.setEnabled(true);
            Toast.makeText(this, "Correct! Click next to continue.", Toast.LENGTH_SHORT).show();
        } else {
            RadioButton checked = (RadioButton) findViewById(options.getCheckedRadioButtonId());
            checked.setBackgroundColor(Color.RED);
            Toast.makeText(this, "Wrong answer! Game is over.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void setNextQuestion(View view) {
        questionNumber++;
        question.setText(WelcomeActivity.questions[questionNumber][0]);
        hint.setText(WelcomeActivity.questions[questionNumber][1]);
        optionA.setText(WelcomeActivity.questions[questionNumber][2]);
        optionA.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
        optionA.setVisibility(View.VISIBLE);
        optionB.setText(WelcomeActivity.questions[questionNumber][3]);
        optionB.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
        optionB.setVisibility(View.VISIBLE);
        optionC.setText(WelcomeActivity.questions[questionNumber][4]);
        optionC.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
        optionC.setVisibility(View.VISIBLE);
        optionD.setText(WelcomeActivity.questions[questionNumber][5]);
        optionD.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.literaturebackground));
        optionD.setVisibility(View.VISIBLE);
        hint.setVisibility(View.INVISIBLE);
        options.clearCheck();
        nextButton.setEnabled(false);
        timer.start();
    }
}
