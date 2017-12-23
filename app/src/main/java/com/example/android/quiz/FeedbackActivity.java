package com.example.android.quiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Oya on 17-12-17.
 */

public class FeedbackActivity extends AppCompatActivity {

    EditText edittext;
    CheckBox geo;
    CheckBox pol;
    CheckBox his;
    CheckBox mus;
    CheckBox spo;
    String message;
    String other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        edittext = findViewById(R.id.other);
        geo = findViewById(R.id.geo);
        pol = findViewById(R.id.pol);
        his = findViewById(R.id.his);
        mus = findViewById(R.id.mus);
        spo = findViewById(R.id.spo);
        message = getString(R.string.feedbackquestion);
        Button newGame = (Button) findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the family category is clicked on.
            @Override
            public void onClick(View view) {
                Intent newGameIntent = new Intent(FeedbackActivity.this, WelcomeActivity.class);
                // Start the new activity
                startActivity(newGameIntent);
            }
        });
        Button quit = (Button) findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the family category is clicked on.
            @Override
            public void onClick(View view) {
                Intent goToHome = new Intent(Intent.ACTION_MAIN);
                goToHome.addCategory(Intent.CATEGORY_HOME);
                startActivity(goToHome);
            }
        });
    }

    public void sendFeedback(View view) {
        if(geo.isChecked())  message += getString(R.string.geography_);
        if(pol.isChecked())  message += getString(R.string.politics_);
        if(his.isChecked())  message += getString(R.string.history_);
        if(mus.isChecked())  message += getString(R.string.music_);
        if(spo.isChecked())  message += getString(R.string.sports_);
        message += getString(R.string.youadded);
        other = edittext.getText().toString();
        if(!(other.equals(" "))) message += other;
        message += getString(R.string.thanks);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: "));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailsubject));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        String[] addresses = {getString(R.string.mailaddress)};
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }



    }

}