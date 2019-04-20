package com.example.shimantoahmed.learnvocabulary.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.shimantoahmed.learnvocabulary.R;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.shimantoahmed.learnvocabulary.adapter.MyAdapter;
import com.example.shimantoahmed.learnvocabulary.db.DatabaseHelper;
import com.example.shimantoahmed.learnvocabulary.model.Word;
import com.example.shimantoahmed.learnvocabulary.others.OcrManager;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;
import java.util.List;

public class WordTranslation extends AppCompatActivity {

    TextView textView;
    OcrManager manager;
    public ScrollView scrollView;
    public String result;
    public int initialScroll;
    public int initialScrollRecy;
    //    Button tryAgain;
    public ImageView expandRecyBtn;
    public ImageView expandTextBtn;
    public List<Word> wordList;
    public boolean[] boolenText;
    public RecyclerView recyclerView;
    public boolean[] boolenRecy;

    public List<Word> finalWordList;
    public MyAdapter adptr;
    public CardView expandRecyView, expandTextView;
    public int staticInitialHeight;
    public boolean iniFlag;
    public boolean[] flag;
    public DatabaseHelper myDatabaseHelper;
    public List<Word> textList = new ArrayList<>();
    public LinearLayout textLayout;

    public LinearLayoutManager linearLayoutManager;
    LinearLayout linearLayout;
    public TextView engWordText, typeTxt, engPronTxt, bangWordText, bangPronText, bangSynText, defiText, engSynTxt, examText, antonymsTxt;
    public LinearLayout lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_translation);

        expandRecyBtn = (ImageView) findViewById(R.id.expandRecyBtn);
        expandTextBtn = (ImageView) findViewById(R.id.expandTextBtn);

        expandRecyView = (CardView) findViewById(R.id.expandRecyView);
        expandTextView = (CardView) findViewById(R.id.expandTextView);
        myDatabaseHelper = new DatabaseHelper(this);

        engWordText = (TextView) findViewById(R.id.word_text);
        typeTxt = (TextView) findViewById(R.id.type);
        engPronTxt = (TextView) findViewById(R.id.eng_pron_txt);
        bangWordText = (TextView) findViewById(R.id.bang_word_text);
        bangPronText = (TextView) findViewById(R.id.bng_pron_txt);
        bangSynText = (TextView) findViewById(R.id.bang_syn_txt);
        defiText = (TextView) findViewById(R.id.defi_txt);
        engSynTxt = (TextView) findViewById(R.id.eng_syn_txt);
        examText = (TextView) findViewById(R.id.exanple_txt);
        antonymsTxt = (TextView) findViewById(R.id.eng_antonyms_txt);
        lLayout = (LinearLayout) findViewById(R.id.detail_view_linear);
        textLayout = (LinearLayout) findViewById(R.id.textLayout);
        int start, end;

//        SharedPreferences prefs = getSharedPreferences("myPref", MODE_PRIVATE);
//        result = prefs.getString("result", "No name defined");//"No name defined" is the default value.


        Intent intent = getIntent();
        result = intent.getStringExtra("result");

//        textView = (TextView) findViewById(R.id.txt);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        iniFlag = true;
        wordList = new ArrayList<>();
        finalWordList = new ArrayList<>();

        String ultimateResult = makeItSimpe(result);
        textView.setText(result);
        result = ultimateResult;
//
//        String[] s = new String[]{};
//        s = ultimateResult.split(" ");
//        makeLinks(textView, s);

//        setTextViews();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

//        adptr = new MyAdapter(finalWordList, this);
        recyclerView.setAdapter(adptr);

        flag = new boolean[]{true};
//        engWordText.setText(finalWordList.get(5).getEngWord());
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                if (flag[0]) {
                    expandRecyView();
                    lLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
//                    Log.d("rrrrr", String.valueOf(finalWordList.get(position)));
                    engWordText.setText(finalWordList.get(position).getEngWord());
                    typeTxt.setText(finalWordList.get(position).getType().toUpperCase());
                    engPronTxt.setText(finalWordList.get(position).getEngPron());
                    bangWordText.setText(finalWordList.get(position).getBangWord());
                    bangPronText.setText(finalWordList.get(position).getBangPron());
                    defiText.setText(finalWordList.get(position).getDefinition());
                    engSynTxt.setText(finalWordList.get(position).getEngSyn());
                    bangSynText.setText(finalWordList.get(position).getBngSyn());
                    examText.setText(finalWordList.get(position).getExample());
                    antonymsTxt.setText(finalWordList.get(position).getAntonyms());
                    flag[0] = false;
                } else {
                    lLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
//                    Log.d("rrrrr", String.valueOf(finalWordList.get(position)));
                    engWordText.setText(finalWordList.get(position).getEngWord());
                    typeTxt.setText(finalWordList.get(position).getType().toUpperCase());
                    engPronTxt.setText(finalWordList.get(position).getEngPron());
                    bangWordText.setText(finalWordList.get(position).getBangWord());
                    bangPronText.setText(finalWordList.get(position).getBangPron());
                    defiText.setText(finalWordList.get(position).getDefinition());
                    engSynTxt.setText(finalWordList.get(position).getEngSyn());
                    bangSynText.setText(finalWordList.get(position).getBngSyn());
                    examText.setText(finalWordList.get(position).getExample());
                    antonymsTxt.setText(finalWordList.get(position).getAntonyms());
                    flag[0] = false;
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        initialScroll = 0;

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView
                int scrollX = scrollView.getScrollX(); // For HorizontalScrollView

//                Log.d("ggggg", String.valueOf(scrollY));
                if (scrollY == 0) {
                    finalWordList.clear();
                    for (int i = 0; i < textList.size(); i++) {
                        finalWordList.add(textList.get(i));
                    }
                    adptr.notifyDataSetChanged();
                    initialScroll = scrollY;
                }
                if ((Math.abs(initialScroll - scrollY)) > 50) {
                    finalWordList.clear();
                    String rrrr = getNumberOfWordsDisplayed();
                    String a[] = rrrr.split(" ");

                    for (int i = 0; i < a.length; i++) {
                        for (int j = 0; j < textList.size(); j++) {

                            String temp = a[i];
                            if (temp != null) {

                                char[] chars = temp.toCharArray();
                                Boolean bbb = false;

                                if (contain(temp, "ing")) {
                                    bbb = handleGerand(temp, j);
                                } else if (chars.length > 0 && chars[temp.length() - 1] == 's') {
                                    bbb = handleED(temp, j, 1);
                                } else if (chars.length > 0) {
                                    if (chars[temp.length() - 1] == 'd') {
                                        bbb = handleED(temp, j, 1);
                                        if (!bbb) {
                                            bbb = handleED(temp, j, 2);
                                        }
                                    }
                                }
//                                temp = temp.trim();
                                if (temp.toLowerCase().equals(textList.get(j).getEngWord().toLowerCase()) || bbb) {
                                    finalWordList.add(textList.get(j));
                                    break;
                                }
                            }
                        }
                    }
                    adptr.notifyDataSetChanged();
                    initialScroll = scrollY;
                }
            }
        });


        final boolean[] fontFlag = {true};
        textView.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                if (fontFlag[0]) {

                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                    fontFlag[0] = false;

                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
                } else {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                    fontFlag[0] = true;
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                }
            }
        });

        boolenText = new boolean[]{true};
        expandTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandTextView();
            }
        });

        boolenRecy = new boolean[]{true};
        expandRecyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandRecyView();
                if (flag[0]) {
                    flag[0] = false;
                } else {
                    flag[0] = true;
                    lLayout.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });


        adptr.notifyDataSetChanged();

        new EasyProcess(this, 0, 5, textView.getText().toString()).execute();


    }

    private Boolean handleED(String s, int pos, int size) {
        s = s.toLowerCase();
        String aaa = "";
        boolean f = false;
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length - size; i++) {
            aaa += chars[i];
        }
        if (size == 2) {
            aaa += 'e';
        }
        if (aaa.equals(textList.get(pos).getEngWord().toLowerCase())) {
            f = true;
        }
        return f;

    }


    public void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                if (interpolatedTime == 1) {
                    v.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    v.requestLayout();
                } else {
                    v.getLayoutParams().height = targetHeight + (int) (targetHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };


        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void toggle(final View v1, final View v2) {
        v1.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v2.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight1 = v1.getMeasuredHeight();
        final int targetHeight2 = v2.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v1.getLayoutParams().height = 1;
        v2.getLayoutParams().height = 1;
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.VISIBLE);
        Animation a1 = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v1.getLayoutParams().height = targetHeight1 + (int) (targetHeight1 * interpolatedTime);
                v1.requestLayout();

                if (interpolatedTime == 1) {
                    v2.setVisibility(View.GONE);
                } else {
                    v2.getLayoutParams().height = targetHeight2 - (int) (targetHeight2 * interpolatedTime);
                    v2.requestLayout();
                }

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };


        a1.setDuration((int) (targetHeight1 / v1.getContext().getResources().getDisplayMetrics().density));
        v1.startAnimation(a1);

    }


    private String getNumberOfWordsDisplayed() {
        int start = textView.getLayout().getLineStart(getFirstLineIndex());
        int end = textView.getLayout().getLineEnd(getLastLineIndex());
        return makeItSimpe(textView.getText().toString().substring(start, end));
    }

    /**
     * Gets the first line that is visible on the screen.
     *
     * @return
     */
    public int getFirstLineIndex() {
        int scrollY = scrollView.getScrollY();
        Layout layout = textView.getLayout();
        if (layout != null) {
            return layout.getLineForVertical(scrollY);
        }
//        Log.d(TAG, "Layout is null: ");
        return -1;
    }

    /**
     * Gets the last visible line number on the screen.
     *
     * @return last line that is visible on the screen.
     */
    public int getLastLineIndex() {
        int height = scrollView.getHeight();
        int scrollY = scrollView.getScrollY();
        Layout layout = textView.getLayout();
        if (layout != null) {
            return layout.getLineForVertical(scrollY + height);
        }
        return -1;
    }

    public abstract static class DoubleClickListener implements View.OnClickListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v);
                lastClickTime = 0;
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onSingleClick(View v);

        public abstract void onDoubleClick(View v);
    }


//    public class Process extends AsyncTask<String, Boolean, Void> {
//
//        private Bitmap bitmap;
//        ProgressDialog progressDialog;
//        Context context;
//
//
//        public Process(Bitmap bitmap, Context context) {
//         /*  progressDialog=new ProgressDialog(getApplicationContext());
//           progressDialog.setMessage("Please wait");
//            progressDialog.show();*/
//            this.bitmap = bitmap;
//            this.context = context;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            progressDialog = ProgressDialog.show(context, "", "Please wait...");
//
//
//        }
//
//        @Override
//        protected Void doInBackground(String... params) {
//            try {
//
//                result = manager.startRecognize(bitmap);
//            } catch (Exception e) {
//                Log.e("eeeee", "advance: " + e.getMessage());
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
////            if (!result.equals(null)) {
//            textView.setText(result);
//            Log.d("lllll", "result: " + result);
//            String a[] = getNumberOfWordsDisplayed();
//            for (int i = 0; i < a.length; i++) {
//                for (int j = 0; j < textList.size(); j++) {
//                    if (a[i].equals(textList.get(j).getEngWord())) {
//                        finalWordList.add(textList.get(j));
//                    }
//                }
//            }
//            adptr.notifyDataSetChanged();
////            }
//        }
//
//    }


    //
//
    public class EasyProcess extends AsyncTask<String, Boolean, Void> {

        ProgressDialog progressDialog;
        Context context;
        int start, end;
        SpannableString spannableString;
        String original;

        public EasyProcess(Context context, int start, int end, String original) {
            this.context = context;
            this.start = start;
            this.end = end;
            this.original = original;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog = ProgressDialog.show(context, "", "Please wait...");

        }

        @Override
        protected Void doInBackground(String... params) {
            String r[] = result.split(" ");
//            Log.d("123456",result);
            if (start < r.length) {
                String[] links = new String[]{};
                links = getDataByMainThread(start, end);


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        spannableString = new SpannableString(textView.getText());

                    }
                });
//                links = result.split(" ");
                for (int i = 0; i < links.length; i++) {
//                    Log.d("ttttt", "data: " + links[i]);
//                    Word word = myDatabaseHelper.getWord(links[i]);
//                    if (word != null) {

                    final int finalI = i;
                    final String[] finalLinks = links;
                    final ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Log.d("llllll","normal clicked :");
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    int position=5;
                                    if (flag[0]) {
                                        expandRecyView();
                                        lLayout.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.INVISIBLE);
//                    Log.d("rrrrr", String.valueOf(finalWordList.get(position)));
                                        engWordText.setText(finalWordList.get(position).getEngWord());
                                        typeTxt.setText(finalWordList.get(position).getType().toUpperCase());
                                        engPronTxt.setText(finalWordList.get(position).getEngPron());
                                        bangWordText.setText(finalWordList.get(position).getBangWord());
                                        bangPronText.setText(finalWordList.get(position).getBangPron());
                                        defiText.setText(finalWordList.get(position).getDefinition());
                                        engSynTxt.setText(finalWordList.get(position).getEngSyn());
                                        bangSynText.setText(finalWordList.get(position).getBngSyn());
                                        examText.setText(finalWordList.get(position).getExample());
                                        antonymsTxt.setText(finalWordList.get(position).getAntonyms());
                                        flag[0] = false;
                                    } else {
                                        lLayout.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.INVISIBLE);
//                    Log.d("rrrrr", String.valueOf(finalWordList.get(position)));
                                        engWordText.setText(finalWordList.get(position).getEngWord());
                                        typeTxt.setText(finalWordList.get(position).getType().toUpperCase());
                                        engPronTxt.setText(finalWordList.get(position).getEngPron());
                                        bangWordText.setText(finalWordList.get(position).getBangWord());
                                        bangPronText.setText(finalWordList.get(position).getBangPron());
                                        defiText.setText(finalWordList.get(position).getDefinition());
                                        engSynTxt.setText(finalWordList.get(position).getEngSyn());
                                        bangSynText.setText(finalWordList.get(position).getBngSyn());
                                        examText.setText(finalWordList.get(position).getExample());
                                        antonymsTxt.setText(finalWordList.get(position).getAntonyms());
                                        flag[0] = false;
                                    }

                                    Toast.makeText(getApplicationContext(), finalLinks[finalI], Toast.LENGTH_SHORT).show();
                                    int pos = 0;
                                    boolean bbb=false;
                                    String temp=finalLinks[finalI].toLowerCase();
                                    for (int i = 0; i < finalWordList.size(); i++) {

                                        char[] chars=temp.toCharArray();
                                        if (contain(temp, "ing")) {
                                            bbb = handleGerand(temp,i);
                                        } else if (chars.length > 0 && chars[temp.length() - 1] == 's') {
                                            bbb = handleED(temp, i, 1);
                                        } else if (chars.length > 0) {
                                            if (chars[temp.length() - 1] == 'd') {
                                                bbb = handleED(temp, i, 1);
                                                if (!bbb) {
                                                    bbb = handleED(temp, i, 2);
                                                }
                                            }
                                        }
                                        if (temp.equals(finalWordList.get(i).toString().toLowerCase()) || bbb) {
                                            Log.d("llllll","clicked :"+pos);
                                            pos = i;
                                            break;
                                        }
                                        Log.d("llllll","data :"+temp);
                                        Log.d("llllll","2nd clicked :"+pos);
                                    }
                                    recyclerView.scrollToPosition(pos);

                                }
                            });

                        }
                    };
                    final String link = links[i];

                    final int startIndexOfLink = textView.getText().toString().indexOf(link);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            spannableString.setSpan(clickableSpan, startIndexOfLink,
                                    startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        }
                    });

                }
            }
//            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adptr.notifyDataSetChanged();
            start += 5;
            end += 5;
            String r[] = result.split(" ");

            textView.setHighlightColor(
                    Color.TRANSPARENT); // prevent TextView change background when highlight
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(spannableString, TextView.BufferType.SPANNABLE);

            if (start <= r.length) {
                new EasyProcess(context, start, end, original).execute();
            }
        }

    }

    public Word ing_trim(String s) {
        String[] split = s.split("ing");
        Word word = myDatabaseHelper.getWord(split[0]);
        if (word != null) {

        } else {
            split[0] += "e";
            word = myDatabaseHelper.getWord(split[0]);
        }
//        Log.d("jjjjj", "size: " + split.length + " split :" + split[0]);
        return word;
    }

    public boolean contain(String word, String containSyntex) {

        boolean isFound = word.indexOf(containSyntex) != -1 ? true : false;
        return isFound;
    }


    public void subMain() {

        Word w = myDatabaseHelper.getWord("make");
//        Log.d("kkkkk", String.valueOf(w));
        String r[] = result.split(" ");
        Word word;

        for (int j = 0; j < r.length; j++) {
//            r[j] = filter(r[j]);
            if (contain(r[j], "ing")) {
                word = ing_trim(r[j]);
            }
            word = myDatabaseHelper.getWord(r[j]);
            if (word != null) {
                textList.add(word);
                finalWordList.add(word);
            }
            adptr.notifyDataSetChanged();
//            for (int i = 0; i < wordList.size(); i++) {
//
////                if (r[j].equals(wordList.get(i).getEngWord())) {
////                    textList.add(wordList.get(i));
////                    finalWordList.add(wordList.get(i));
////                    adptr.notifyDataSetChanged();
////                    break;
////                }
//
//            }
        }

////        byte[] byteArray = getIntent().getByteArrayExtra("image");
////        final Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//
//        final Bitmap bmp = BitmapFactory.decodeResource(getResources(),
//                R.raw.im);
//
//        for (int i = 0; i < textList.size(); i++) {
////            Log.d("iiiii",textList.get(i).getEngWord()+"  "+textList.get(i).getBangWord());
//            finalWordList.add(textList.get(i));
//        }
//        adptr.notifyDataSetChanged();

    }

    public void expandTextView() {
        if (boolenText[0]) {
            if (iniFlag) {
                staticInitialHeight = expandTextView.getMeasuredHeight();
                iniFlag = false;
                initialScrollRecy = expandRecyView.getMeasuredHeight();
            }
            final int ini = staticInitialHeight;

            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {

//                            Log.d("99999", "loop: " + String.valueOf(expandTextView.getMeasuredHeight()));
                    if (interpolatedTime == 1) {
                        expandTextView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                        expandTextView.requestLayout();
                    } else {
                        expandTextView.getLayoutParams().height = ini + (int) (ini * interpolatedTime);
                        expandTextView.requestLayout();
                    }
//-----------------------------------------------------------------  Collapsing ------------
                    if (interpolatedTime == 1) {
                        expandRecyView.setVisibility(View.GONE);
                    } else {
                        expandRecyView.getLayoutParams().height = ini - (int) (ini * interpolatedTime);
                        expandRecyView.requestLayout();
                    }

                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            a.setDuration((int) (staticInitialHeight / expandTextView.getContext().getResources().getDisplayMetrics().density));
            expandTextView.startAnimation(a);


//                    collapse(recyclerView);
            boolenText[0] = false;
        } else {

            if (iniFlag) {
                staticInitialHeight = expandTextView.getMeasuredHeight();
//                        Log.d("88888", "actual height: " + String.valueOf(expandTextView.getMeasuredHeight()));
                iniFlag = false;
                initialScrollRecy = expandRecyView.getMeasuredHeight();
            }
            final int ini = staticInitialHeight;

            expandRecyView.setVisibility(View.VISIBLE);

            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {


                    if (expandRecyView.getMeasuredHeight() <= ini) {
                        expandRecyView.getLayoutParams().height = ini + (int) (ini * interpolatedTime);
                        expandRecyView.requestLayout();
                    }

//-----------------------------------------------------------------  Collapsing ------------

                    if (expandTextView.getMeasuredHeight() >= ini) {
                        expandTextView.getLayoutParams().height = ini - (int) (ini * interpolatedTime);
                        expandTextView.requestLayout();
                    }

                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };


            a.setDuration((int) (staticInitialHeight / expandTextView.getContext().getResources().getDisplayMetrics().density));
            expandTextView.startAnimation(a);
            boolenText[0] = true;
        }
    }

    public void expandRecyView() {
        if (boolenRecy[0]) {

            if (iniFlag) {
                staticInitialHeight = expandTextView.getMeasuredHeight();
//                        Log.d("88888", "actual height: " + String.valueOf(expandTextView.getMeasuredHeight()));
                iniFlag = false;
                initialScrollRecy = expandRecyView.getMeasuredHeight();

            }
            final int ini = staticInitialHeight;

            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {

//                            Log.d("99999", "loop: " + String.valueOf(expandTextView.getMeasuredHeight()));
                    if (interpolatedTime == 1) {
                        expandRecyView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                        expandRecyView.requestLayout();
                    } else {
                        expandRecyView.getLayoutParams().height = ini + (int) (ini * interpolatedTime);
                        expandRecyView.requestLayout();
                    }
//-----------------------------------------------------------------  Collapsing ------------
                    if (interpolatedTime == 1) {
                        expandTextView.setVisibility(View.GONE);
                    } else {
                        expandTextView.getLayoutParams().height = ini - (int) (ini * interpolatedTime);
                        expandTextView.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            a.setDuration((int) (staticInitialHeight / expandTextView.getContext().getResources().getDisplayMetrics().density));
            expandRecyView.startAnimation(a);
            boolenRecy[0] = false;
        } else {

            if (iniFlag) {
                staticInitialHeight = expandTextView.getMeasuredHeight();
                iniFlag = false;
                initialScrollRecy = expandRecyView.getMeasuredHeight();

            }
            final int ini = initialScrollRecy;
            final int ini2 = staticInitialHeight;

            expandTextView.setVisibility(View.VISIBLE);

            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {


                    if (expandTextView.getMeasuredHeight() <= ini2) {
                        expandTextView.getLayoutParams().height = ini2 + (int) (ini2 * interpolatedTime);
                        expandTextView.requestLayout();
                    }

//-----------------------------------------------------------------  Collapsing ------------

                    if (expandRecyView.getMeasuredHeight() >= ini) {
                        expandRecyView.getLayoutParams().height = ini - (int) (ini * interpolatedTime);
                        expandRecyView.requestLayout();
                    }

                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            a.setDuration((int) (initialScrollRecy / expandTextView.getContext().getResources().getDisplayMetrics().density));
            expandRecyView.startAnimation(a);
            boolenText[0] = true;

            boolenRecy[0] = true;
        }
    }


    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
//                    Log.d("main","OnLongPress");
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    @Override
    public void onBackPressed() {

        expandRecyView();
        if (flag[0]) {
            super.onBackPressed();
        } else {
            flag[0] = true;
            lLayout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }


    private List<String> getTrimedBySyntex(String w) {
        List<String> stringList = new ArrayList<>();
        String[] r = new String[]{};
        if (contain(w, ",")) {
            r = w.split(",");
        } else if (contain(w, "-")) {
            r = w.split("-");
        } else if (contain(w, ".")) {
            r = w.split(".");
        } else if (contain(w, "/")) {
            r = w.split("/");
        } else if (contain(w, " ")) {
            r = w.split(" ");
        } else {
            r = w.split("\\r?\\n");
        }

        if (r.length == 0) {
            stringList.add(w.toLowerCase());
        } else {
            for (int i = 0; i < r.length; i++) {
//                Log.d("jjjjj", "loop :: " + r[i]);
                if (!r[i].equals(" ")) {
                    stringList.add(r[i].toLowerCase());
                }
            }
        }
        return stringList;
    }

    public String[] getDataByMainThread(int start, int end) {

        List<String> spanList = new ArrayList<>();
        String r[] = result.split(" ");
        Word word;

        if (r.length >= end) {
            for (int j = start; j < end; j++) {
//                Log.d("456789", String.valueOf(start));
                String temp = r[j];
//                if (contain(r[j], "ing")) {
//                    r[j] = ing_trim(r[j]);
//                }
                if (!dontAccept(r[j])) {
                    if (contain(r[j], "ing")) {
                        word = ing_trim(r[j]);
                    } else {
                        word = myDatabaseHelper.getWord(r[j]);
                        if (word != null) {
                        } else {
                            word = reCheckDatabae(r[j]);
                        }
                    }
                    if (word != null) {
//                        Log.d("fffff", "data: " + r[j]);
                        if (!word.getBangWord().equals("কুঠার") && !word.getEngWord().equals(null)) {
                            textList.add(word);
                            finalWordList.add(word);
                            spanList.add(temp);
                        }
                    }
                }
            }
        } else {
            for (int j = start; j < r.length; j++) {

                String temp = r[j];
                if (contain(temp, "ing")) {
                    word = ing_trim(temp);
                } else {
                    word = myDatabaseHelper.getWord(temp);
                    if (word != null) {
                    } else {
                        word = reCheckDatabae(r[j]);
                    }
                }

                if (!dontAccept(r[j])) {
                    if (word != null && !dontAccept(r[j])) {
                        if (!word.getBangWord().equals("কুঠার") && !word.getEngWord().equals(null)) {
                            textList.add(word);
                            finalWordList.add(word);
                            spanList.add(r[j]);
                        }
                    }
                }
            }
        }
        String[] aa = new String[]{};
        aa = spanList.toArray(new String[spanList.size()]);
        return aa;
    }

    private Word reCheckDatabae(String s) {
        char[] chars = s.toCharArray();
        Word word = null;
        if (chars[chars.length - 1] == 's') {

            String aaa = "";
            for (int l = 0; l < chars.length - 1; l++) {
                aaa += chars[l];
            }
            word = myDatabaseHelper.getWord(aaa);
        } else if (chars[chars.length - 1] == 'd') {
            String aaa = "";
            for (int l = 0; l < chars.length - 1; l++) {
                aaa += chars[l];
            }
            word = myDatabaseHelper.getWord(aaa);
            if (word != null) {

            } else {
                aaa = "";
                for (int l = 0; l < chars.length - 2; l++) {
                    aaa += chars[l];
                }
                word = myDatabaseHelper.getWord(aaa);
            }
        } else if (chars[chars.length - 1] == 'd' && chars[chars.length - 2] == 'e') {
            String aaa = "";
            for (int l = 0; l < chars.length - 2; l++) {
                aaa += chars[l];
            }
            word = myDatabaseHelper.getWord(aaa);
        }

        return word;
    }

    public void setTextViews() {
        String r[] = result.split(" ");
        TextView[] textViews = new TextView[]{};
        ViewGroup.LayoutParams lparms = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < r.length; i++) {
//            textViews[i]=new TextView(this);
//            textViews[i].setText(r[0]);
//            textViews[i].setTextColor(Color.RED);
//            textViews[i].setLayoutParams(lparms);
//            lLayout.addView(textViews[i]);
//
            TextView t = new TextView(this);
            t.setText(r[i]);
            t.setLayoutParams(lparms);
            textLayout.addView(t);
        }
    }

    public void makeLinks(TextView textView, final String[] links) {
        SpannableString spannableString = new SpannableString(textView.getText());
        for (int i = 0; i < links.length; i++) {
            Word word = myDatabaseHelper.getWord(links[i]);
            if (word != null) {

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
        }
        textView.setHighlightColor(
                Color.TRANSPARENT); // prevent TextView change background when highlight
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }


    public String makeItSimpe(String s) {

        boolean flag = true;
        char[] chars = new char[]{};
        String re = "";
        chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] >= 97 && chars[i] <= 121) || (chars[i] >= 65 && chars[i] <= 90)) {
                re += chars[i];
                flag = true;
            } else if (chars[i] == 10) {
                if (flag) {
                    re += " ";
                    flag = false;
                }
            } else if (chars[i] == 32) {
                if (flag) {
                    re += " ";
                    flag = false;
                }
            } else {
                if (flag) {
                    re += " ";
                    flag = false;
                }
            }
        }
        return re;
    }


    public class MakeUnderLineProcess extends AsyncTask<String, Boolean, Void> {

        ProgressDialog progressDialog;
        Context context;
        String ulResult;
        SpannableString spannableString;

        public MakeUnderLineProcess(Context context, String ulResult) {
            this.context = context;
            this.ulResult = ulResult;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(context, "", "Please wait...");

        }

        @Override
        protected Void doInBackground(String... params) {

            String[] links = new String[]{};
            links = ulResult.split(" ");
            spannableString = new SpannableString(textView.getText());
            for (int i = 0; i < links.length; i++) {
                Word word = myDatabaseHelper.getWord(links[i]);
                if (word != null) {

                    final int finalI = i;
                    final String[] finalLinks = links;
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Toast.makeText(getApplicationContext(), finalLinks[finalI], Toast.LENGTH_SHORT).show();

                        }
                    };
                    String link = links[i];

                    int startIndexOfLink = textView.getText().toString().indexOf(link);
                    spannableString.setSpan(clickableSpan, startIndexOfLink,
                            startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            textView.setHighlightColor(
                    Color.TRANSPARENT); // prevent TextView change background when highlight
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(spannableString, TextView.BufferType.SPANNABLE);

        }

    }

    private void setText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SpannableString spannableString;
                spannableString = new SpannableString(textView.getText());

                String[] links = new String[]{};
                links = result.split(" ");

//                links=result.split(" ");

                for (int i = 0; i < links.length; i++) {
                    Word word = myDatabaseHelper.getWord(links[i]);
                    if (word != null) {

                        final int finalI = i;
                        final String[] finalLinks = links;
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                Toast.makeText(getApplicationContext(), finalLinks[finalI], Toast.LENGTH_SHORT).show();
                            }
                        };
                        String link = links[i];

                        int startIndexOfLink = textView.getText().toString().indexOf(link);
                        spannableString.setSpan(clickableSpan, startIndexOfLink,
                                startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        textView.setHighlightColor(
                                Color.TRANSPARENT); // prevent TextView change background when highlight
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
                    }
                }
            }
        });
    }

    public boolean dontAccept(String word) {
        word = word.toLowerCase();
        boolean flag = false;
        String[] ignore = new String[]{"the", "am", "is", "are", "was", "ware", "he", "his", "her", "had", "has", "i", "an", "a", "by", "and", "may", "at", "with", "not"};
        if (word.length() > 2) {
            for (int i = 0; i < ignore.length; i++) {
                if (word.equals(ignore[i])) {
                    flag = true;
                    break;
                }
            }
        } else {
            flag = true;
        }

        return flag;
    }

    public boolean handleGerand(String s, int pos) {

        s = s.toLowerCase();
        Boolean f = false;
        char[] chars = s.toCharArray();
        String aaa = "";
        for (int i = 0; i < chars.length - 3; i++) {
            aaa += chars[i];
        }

        if (aaa.equals(textList.get(pos).getEngWord().toLowerCase())) {
            f = true;
        }

        if (!f) {
            aaa += "e";
            if (aaa.equals(textList.get(pos).getEngWord().toLowerCase())) {
                f = true;
            }
        }

        return f;
    }


}
