package com.example.android.quiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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
        Button newGame = findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the family category is clicked on.
            @Override
            public void onClick(View view) {
                Intent newGameIntent = new Intent(FeedbackActivity.this, WelcomeActivity.class);
                // Start the new activity
                startActivity(newGameIntent);
            }
        });
        Button quit = findViewById(R.id.quit);
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
        if(geo.isChecked())  message += "\n" + getString(R.string.geography);
        if(pol.isChecked())  message += "\n" + getString(R.string.politics);
        if(his.isChecked())  message += "\n" + getString(R.string.history);
        if(mus.isChecked())  message += "\n" + getString(R.string.music);
        if(spo.isChecked())  message += "\n" + getString(R.string.sports);
        message += "\n" + getString(R.string.youadded);
        other = edittext.getText().toString();
        if(!(other.equals(" "))) message += other;
        message += "\n" + getString(R.string.thanks);
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