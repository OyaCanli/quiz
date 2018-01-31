package com.example.android.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class FeedbackActivity extends AppCompatActivity {

    EditText edittext;
    CheckBox geo, pol, his, mus, spo;
    String message, other;
    Menu menu;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        this.menu = menu;
        menu.removeItem(R.id.feedback);
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
            Intent intent = new Intent(FeedbackActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendFeedback(View view) {
        if(geo.isChecked())  message += "\n" + getString(R.string.geography);
        if(pol.isChecked())  message += "\n" + getString(R.string.politics);
        if(his.isChecked())  message += "\n" + getString(R.string.history);
        if(mus.isChecked())  message += "\n" + getString(R.string.music);
        if(spo.isChecked())  message += "\n" + getString(R.string.sports);
        message += "\n" + getString(R.string.youadded);
        other = edittext.getText().toString();
        if(!(other.equals(" "))) message += "\n" + other;
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