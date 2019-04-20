package com.example.shimantoahmed.learnvocabulary.Activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shimantoahmed.learnvocabulary.Activity.MainActivity;
import com.example.shimantoahmed.learnvocabulary.R;
import com.example.shimantoahmed.learnvocabulary.model.Word;
import com.example.shimantoahmed.learnvocabulary.others.OcrManager;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;


import com.example.shimantoahmed.learnvocabulary.Activity.WordTranslation;
import com.example.shimantoahmed.learnvocabulary.PopUpActivity.PopUpCapture;
import com.example.shimantoahmed.learnvocabulary.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResultActivity extends AppCompatActivity {
    //    ImageView imgeView;
//    Button btn;
//    int taskValue;
    private final int requestCode = 20;

    TextView txt1, txt2, txt3, txt4, txt5, txt6;

    OcrManager manager;
    private TextRecognizer textRecognizer;
    String result;
    private LinearLayout layout;
    private TextView text1, text2, text3, text4, text5, text6;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        context = getLayoutInflater().getContext();

        txt1 = (TextView) findViewById(R.id.txt1);
//        txt2=(TextView)findViewById(R.id.txt2);
//        txt3=(TextView)findViewById(R.id.txt3);
//        txt4=(TextView)findViewById(R.id.txt4);
//        txt5=(TextView)findViewById(R.id.txt5);
//        txt6=(TextView)findViewById(R.id.txt6);

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scroll);
        layout = (LinearLayout) findViewById(R.id.parentLayout);


        final int[] initialScroll = {0};

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

//        int s, e;
//        s = 0;
//        e = 5;
//
//        SpannableString ss = null;
////        for(int i=0;i<25;i++){
//
//        ss = new SpannableString("Android is a Software stack");
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(View textView) {
//                Toast.makeText(getApplicationContext(), "Normal Link", Toast.LENGTH_SHORT).show();
//            }
//        };
//        ss.setSpan(clickableSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////        }
//
//
//        txt1.setText(ss);
//        txt1.setMovementMethod(LinkMovementMethod.getInstance());

        String a="my name is shimanto ahmed";
        txt1.setText(a);
        String[] s=new String[]{};
        s=a.split(" ");
        makeLinks(txt1,s);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView
                int scrollX = scrollView.getScrollX(); // For HorizontalScrollView


                if ((Math.abs(initialScroll[0] - scrollY)) > 100) {
                    checkViewAndUpdate();

                    initialScroll[0] = scrollY;
                }

            }
        });


//        final Bitmap bmp = BitmapFactory.decodeResource(getResources(),
//                R.raw.im);

//        Intent i = getIntent();
//        taskValue = i.getIntExtra("btn", -1);

//        manager = new OcrManager();
//        manager.intAPI();

//        imgeView = (ImageView) findViewById(R.id.image);
//        btn = (Button) findViewById(R.id.button);
//        imgeView.setImageBitmap(bmp);

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        // taskValue = 1 means load From gallary else for 2 capture image by camera
//        if (taskValue == 1) {
//
//            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//            photoPickerIntent.setType("image/*");
//            startActivityForResult(photoPickerIntent, 10);
//
//        } else {
//            Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(photoCaptureIntent, requestCode);
//        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            imgeView.setImageBitmap(bitmap);
            //--------------------------------------  Camera  -----------------

            try {
                new EasyProcess(bitmap, this).execute();
            } catch (Exception e) {
                Log.e("eeeee", e.getMessage());
            }

        } else if (resultCode == RESULT_OK) {
            try {
                // ------------  Load Image  --------------

                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
//                imgeView.setImageBitmap(bitmap);

                try {
                    new EasyProcess(bitmap, this).execute();
                } catch (Exception e) {
                    Log.e("eeeee", e.getMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("mmmmm", e.getMessage());
//                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }

    }


    public class EasyProcess extends AsyncTask<String, Boolean, Void> {

        private Bitmap bitmap;
        ProgressDialog progressDialog;
        Context context;


        public EasyProcess(Bitmap bitmap, Context context) {
         /*  progressDialog=new ProgressDialog(getApplicationContext());
           progressDialog.setMessage("Please wait");
            progressDialog.show();*/
            this.bitmap = bitmap;
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "", "Please wait...");


        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                SparseArray<TextBlock> item = textRecognizer.detect(frame);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < item.size(); i++) {
                    stringBuilder.append(item.valueAt(i).getValue());
                }
                result = String.valueOf(stringBuilder);

            } catch (Exception e) {
                Log.e("eeeee", "easy: " + e.getMessage());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            Log.d("ttttt", "result :" + result);

        }

    }


    private void checkViewAndUpdate() {

        Rect rect = new Rect();
        layout.getHitRect(rect);
        if (text1.getLocalVisibleRect(rect)) {
            Toast.makeText(context, "visible", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Not visible", Toast.LENGTH_LONG).show();
        }

    }

    private void setTextViews() {

        text1 = new TextView(context);
        text2 = new TextView(context);
        text3 = new TextView(context);
        text4 = new TextView(context);
        text5 = new TextView(context);
        text6 = new TextView(context);

        text1.setText("text1");
        text2.setText("text2");
        text3.setText("text3");
        text4.setText("text4");
        text5.setText("text5");
        text6.setText("text6");

        text1.setTextSize(30);
        text2.setTextSize(30);
        text3.setTextSize(30);
        text4.setTextSize(30);
        text5.setTextSize(30);
        text6.setTextSize(30);

        //layout.removeAllViews();

        layout.addView(text1);
        layout.addView(text2);
        layout.addView(text3);
        layout.addView(text4);
        layout.addView(text5);
        layout.addView(text6);

//        String[] s=new String[]{"aa","bb"};
//        ClickableSpan[]a=new ClickableSpan[]{new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                widget.
//            }
//        },};
//        makeLinks(txt1,s,a);
    }


    public void makeLinks(TextView textView, final String[] links) {
        SpannableString spannableString = new SpannableString(textView.getText());
        for (int i = 0; i < links.length; i++) {
            final int finalI = i;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(getApplicationContext(), links[finalI], Toast.LENGTH_SHORT).show();
                }
            };
            String link = links[i];

            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHighlightColor(
                Color.TRANSPARENT); // prevent TextView change background when highlight
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

}
