package com.example.shimantoahmed.learnvocabulary.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shimantoahmed.learnvocabulary.R;
import com.example.shimantoahmed.learnvocabulary.db.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class TextActivity extends AppCompatActivity {

    DatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        myDatabaseHelper = new DatabaseHelper(getApplicationContext());
        Button btn = (Button) findViewById(R.id.txtDoneBtn);
        final EditText et = (EditText) findViewById(R.id.et);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String question = et.getText().toString().trim();
                if (!question.isEmpty()) {

                    final int THUMBSIZE = 64;

                    Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.no_thumb);

                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    Log.d("456789", String.valueOf(byteArray));
                    bitmap.recycle();
                    Log.d("456789", String.valueOf(mydate));

                    boolean isDuplicate = myDatabaseHelper.checkDuplicateItem(et.getText().toString());

                    if (!isDuplicate) {
                        myDatabaseHelper.insertImg(byteArray, mydate, et.getText().toString());
                    }
                    Intent i = new Intent(getApplicationContext(), FinalWordTranslation.class);
                    i.putExtra("result", et.getText().toString());
//                i.putExtra("image",byteArray);
                    startActivity(i);
                }
                else  Toast.makeText(getApplicationContext(),"No Word Found",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
