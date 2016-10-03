package com.strozh.emailclient.fragments.Inbox;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import com.strozh.emailclient.R;

public class SingleMail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_mail);
        Intent intent = getIntent();
        String from = intent.getStringExtra("EXTRA_FROM").toUpperCase();
        String subject = intent.getStringExtra("EXTRA_SUBJECT").toUpperCase();
        String content = intent.getStringExtra("EXTRA_CONTENT");

        setTitle(subject);

        Log.d("EmailClient", "onCreate: EXTRA_FROM = " + from);
        Log.d("EmailClient", "onCreate: EXTRA_SUBJECT = " + subject);
        Log.d("EmailClient", "onCreate: EXTRA_BODY = " + content);

        TextView textViewFrom = (TextView) findViewById(R.id.tv_from);
        if (textViewFrom != null) {

            textViewFrom.setText(from);
        }

        TextView textViewSubject = (TextView) findViewById(R.id.tv_subject);
        if (textViewSubject != null) {

            textViewSubject.setText(subject);
        }

        TextView textViewBody = (TextView) findViewById(R.id.tv_content);
        if (textViewBody != null) {

            textViewBody.setText(Html.fromHtml(content));
        }
    }
}