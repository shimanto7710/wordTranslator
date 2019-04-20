package com.example.shimantoahmed.learnvocabulary.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.shimantoahmed.learnvocabulary.R;

public class ShakilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shakil);

        TextView textView=(TextView)findViewById(R.id.ttt);
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        textView.setText(result);
    }
}
