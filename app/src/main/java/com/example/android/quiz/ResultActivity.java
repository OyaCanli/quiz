package com.example.android.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Oya on 02-02-18.
 */

public class ResultActivity extends AppCompatActivity{

    final static String SCORE = "score";
    Menu menu;
    ImageButton mailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        final ProgressBar mProgress = findViewById(R.id.circularProgressbar);
        TextView tv = findViewById(R.id.tv);
        final int score = getIntent().getExtras().getInt(SCORE);
        tv.setText(getString(R.string._score, score));
        mProgress.setProgress(score);
        mailButton = findViewById(R.id.mailButton);
        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message= getString(R.string.mail_message, score);
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto: "));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailsubject));
                intent.putExtra(Intent.EXTRA_TEXT, message);
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.rules) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.info))
                    .setTitle(getString(R.string.rules))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (id == R.id.categories) {
            Intent intent = new Intent(ResultActivity.this, WelcomeActivity.class);
            startActivity(intent);
        } else if(id == R.id.bestRecords){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(MainActivity.bestRecords())
                    .setTitle(getString(R.string.best_records))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (id == R.id.feedback) {
            Intent intent = new Intent(ResultActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
