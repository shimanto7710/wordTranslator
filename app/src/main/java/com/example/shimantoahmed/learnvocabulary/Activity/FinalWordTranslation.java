package com.example.shimantoahmed.learnvocabulary.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shimantoahmed.learnvocabulary.R;
import com.example.shimantoahmed.learnvocabulary.adapter.MyAdapter;
import com.example.shimantoahmed.learnvocabulary.db.DatabaseHelper;
import com.example.shimantoahmed.learnvocabulary.model.ActualWordList;
import com.example.shimantoahmed.learnvocabulary.model.Word;
import com.example.shimantoahmed.learnvocabulary.others.OcrManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.widget.GridLayout.HORIZONTAL;
import static java.security.AccessController.getContext;

public class FinalWordTranslation extends AppCompatActivity {


    TextView textView;
    OcrManager manager;
    public ScrollView scrollView;
    public String result;
    public int initialScroll;
    public int initialScrollRecy;
    //    Button tryAgain;
//    public ImageView expandRecyBtn;
    //    public ImageView expandTextBtn;
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
    //    public List<Word> textList = new ArrayList<>();
    public List<ActualWordList> actualWordList;
    public LinearLayout textLayout;

    public LinearLayoutManager linearLayoutManager;
    LinearLayout linearLayout;
    public TextView engWordText, typeTxt, engPronTxt, bangWordText, bangPronText, bangSynText, defiText, engSynTxt, examText, antonymsTxt;
    public LinearLayout lLayout;
    public CardView secondRootLayout;
    ImageButton articleBtn, listBtn;
    ImageView backBtn;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_word_translation);

        backBtn = (ImageButton) findViewById(R.id.back);
        articleBtn = (ImageButton) findViewById(R.id.article);
        listBtn = (ImageButton) findViewById(R.id.list);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        secondRootLayout = (CardView) findViewById(R.id.root_2nd);
        secondRootLayout.setVisibility(View.GONE);
//        expandRecyBtn = (ImageView) findViewById(R.id.expandRecyBtn);
//        expandTextBtn = (ImageView) findViewById(R.id.expandTextBtn);

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
        final int start, end;

//        SpannableString spannableStrin=null;
//        textView.setText(spannableStrin, TextView.BufferType.SPANNABLE);

        Intent intent = getIntent();
        result = intent.getStringExtra("result");

        textView = (TextView) findViewById(R.id.txt);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        iniFlag = true;
        wordList = new ArrayList<>();
        finalWordList = new ArrayList<>();

        String ultimateResult = makeItSimpe(result);
        String b[] = ultimateResult.split("\\r?\\n");
        String ss = "";
        for (int i = 0; i < b.length; i++) {
            ss += b[i] + ".";
        }
        String a[] = result.split("\\r?\\n");
        String aaa = "";
        for (int i = 0; i < a.length; i++) {
            aaa += a[i] + ".";
        }
//        int count=0;
//        for (int i=0;i<aaa.length();i++){
//            if (aaa[i].equals("\"")){
//                count++;
//            }
//        }

//        Log.d("101010","count: "+count);
//        String mm="mm.“.";
//        mm+=aaa;
//        aaa=mm;
//        aaa+="asdjoasdj asdahsdiuahd askdjhakdus";
//        aaa+="mm.“.";
//        Log.d("101010", "result: " + aaa + "//");
        textView.setText(aaa);
        result = ss;


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adptr = new MyAdapter(finalWordList, this);
        recyclerView.setAdapter(adptr);

        DividerItemDecoration divider = new
                DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL);
        divider.setDrawable(
                ContextCompat.getDrawable(getBaseContext(), R.drawable.divider)
        );
        recyclerView.addItemDecoration(divider);

        actualWordList = new ArrayList<>();
        flag = new boolean[]{true};


//        engWordText.setText(finalWordList.get(5).getEngWord());

        adptr.setOnItemClickListerner(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (flag[0]) {
                    expandRecyView();
                    lLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);


                    Word w = finalWordList.get(position);
                    engWordText.setText(w.getEngWord().substring(0, 1).toUpperCase() + w.getEngWord().substring(1));
                    typeTxt.setText(finalWordList.get(position).getType().toUpperCase());
                    engPronTxt.setText(w.getEngPron());
                    bangWordText.setText(finalWordList.get(position).getBangWord());
                    bangPronText.setText(finalWordList.get(position).getBangPron());

                    defiText.setText(w.getDefinition().substring(0, 1).toUpperCase() + w.getDefinition().substring(1));
                    String enSyn = "";
                    String b[] = w.getEngSyn().split(",");
                    for (int i = 0; i < b.length; i++) {
                        b[i] = b[i].substring(0, 1).toUpperCase() + b[i].substring(1);
                        enSyn += b[i] + "," + " ";
                    }
                    engSynTxt.setText(enSyn);
                    String a[] = w.getBngSyn().split(",");
                    String bnSyn = "";
                    for (int i = 0; i < a.length; i++) {
//                        a[i]=a[i].substring(0,1).toUpperCase() + a[0].substring(1);
                        bnSyn += a[i] + "," + " ";
                    }
                    bangSynText.setText(bnSyn);
                    examText.setText(finalWordList.get(position).getExample());
                    antonymsTxt.setText(w.getAntonyms().substring(0, 1).toUpperCase() + w.getAntonyms().substring(1));

                    articleBtn.setVisibility(View.GONE);
                    listBtn.setVisibility(View.GONE);

                    flag[0] = false;
                } else {
                    lLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
//                    Log.d("rrrrr", String.valueOf(finalWordList.get(position)));
//                    engWordText.setText(finalWordList.get(position).getEngWord());
//                    typeTxt.setText(finalWordList.get(position).getType().toUpperCase());
//                    engPronTxt.setText(finalWordList.get(position).getEngPron());
//                    bangWordText.setText(finalWordList.get(position).getBangWord());
//                    bangPronText.setText(finalWordList.get(position).getBangPron());
//                    defiText.setText(finalWordList.get(position).getDefinition());
//                    engSynTxt.setText(finalWordList.get(position).getEngSyn());
//                    bangSynText.setText(finalWordList.get(position).getBngSyn());
//                    examText.setText(finalWordList.get(position).getExample());
//                    antonymsTxt.setText(finalWordList.get(position).getAntonyms());

                    Word w = finalWordList.get(position);
                    engWordText.setText(w.getEngWord().substring(0, 1).toUpperCase() + w.getEngWord().substring(1));
                    typeTxt.setText(finalWordList.get(position).getType().toUpperCase());
                    engPronTxt.setText(w.getEngPron());
                    bangWordText.setText(finalWordList.get(position).getBangWord());
                    bangPronText.setText(finalWordList.get(position).getBangPron());

                    defiText.setText(w.getDefinition().substring(0, 1).toUpperCase() + w.getDefinition().substring(1));
                    String enSyn = "";
                    String b[] = w.getEngSyn().split(",");
                    for (int i = 0; i < b.length; i++) {
                        b[i] = b[i].substring(0, 1).toUpperCase() + b[i].substring(1);
                        enSyn += b[i] + "," + " ";
                    }
                    engSynTxt.setText(enSyn);
                    String a[] = w.getBngSyn().split(",");
                    String bnSyn = "";
                    for (int i = 0; i < a.length; i++) {
//                        a[i]=a[i].substring(0,1).toUpperCase() + a[0].substring(1);
                        bnSyn += a[i] + "," + " ";
                    }
                    bangSynText.setText(bnSyn);
                    examText.setText(finalWordList.get(position).getExample());
                    antonymsTxt.setText(w.getAntonyms().substring(0, 1).toUpperCase() + w.getAntonyms().substring(1));

                    articleBtn.setVisibility(View.GONE);
                    listBtn.setVisibility(View.GONE);

                    flag[0] = false;
                }

            }

        });

//        recyclerView.addOnItemTouchListener(new WordTranslation.RecyclerTouchListener(getApplicationContext(),
//                recyclerView, new WordTranslation.ClickListener() {
//            @Override
//            public void onClick(View view, final int position) {
//
//                if (flag[0]) {
//                    expandRecyView();
//                    lLayout.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.INVISIBLE);
//
//
//                    Word w = finalWordList.get(position);
//                    engWordText.setText(w.getEngWord().substring(0, 1).toUpperCase() + w.getEngWord().substring(1));
//                    typeTxt.setText(finalWordList.get(position).getType().toUpperCase());
//                    engPronTxt.setText(w.getEngPron());
//                    bangWordText.setText(finalWordList.get(position).getBangWord());
//                    bangPronText.setText(finalWordList.get(position).getBangPron());
//
//                    defiText.setText(w.getDefinition().substring(0, 1).toUpperCase() + w.getDefinition().substring(1));
//                    String enSyn = "";
//                    String b[] = w.getEngSyn().split(",");
//                    for (int i = 0; i < b.length; i++) {
//                        b[i] = b[i].substring(0, 1).toUpperCase() + b[i].substring(1);
//                        enSyn += b[i] + "," + " ";
//                    }
//                    engSynTxt.setText(enSyn);
//                    String a[] = w.getBngSyn().split(",");
//                    String bnSyn = "";
//                    for (int i = 0; i < a.length; i++) {
////                        a[i]=a[i].substring(0,1).toUpperCase() + a[0].substring(1);
//                        bnSyn += a[i] + "," + " ";
//                    }
//                    bangSynText.setText(bnSyn);
//                    examText.setText(finalWordList.get(position).getExample());
//                    antonymsTxt.setText(w.getAntonyms().substring(0, 1).toUpperCase() + w.getAntonyms().substring(1));
//
//                    articleBtn.setVisibility(View.GONE);
//                    listBtn.setVisibility(View.GONE);
//
//                    flag[0] = false;
//                } else {
//                    lLayout.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.INVISIBLE);
////                    Log.d("rrrrr", String.valueOf(finalWordList.get(position)));
////                    engWordText.setText(finalWordList.get(position).getEngWord());
////                    typeTxt.setText(finalWordList.get(position).getType().toUpperCase());
////                    engPronTxt.setText(finalWordList.get(position).getEngPron());
////                    bangWordText.setText(finalWordList.get(position).getBangWord());
////                    bangPronText.setText(finalWordList.get(position).getBangPron());
////                    defiText.setText(finalWordList.get(position).getDefinition());
////                    engSynTxt.setText(finalWordList.get(position).getEngSyn());
////                    bangSynText.setText(finalWordList.get(position).getBngSyn());
////                    examText.setText(finalWordList.get(position).getExample());
////                    antonymsTxt.setText(finalWordList.get(position).getAntonyms());
//
//                    Word w = finalWordList.get(position);
//                    engWordText.setText(w.getEngWord().substring(0, 1).toUpperCase() + w.getEngWord().substring(1));
//                    typeTxt.setText(finalWordList.get(position).getType().toUpperCase());
//                    engPronTxt.setText(w.getEngPron());
//                    bangWordText.setText(finalWordList.get(position).getBangWord());
//                    bangPronText.setText(finalWordList.get(position).getBangPron());
//
//                    defiText.setText(w.getDefinition().substring(0, 1).toUpperCase() + w.getDefinition().substring(1));
//                    String enSyn = "";
//                    String b[] = w.getEngSyn().split(",");
//                    for (int i = 0; i < b.length; i++) {
//                        b[i] = b[i].substring(0, 1).toUpperCase() + b[i].substring(1);
//                        enSyn += b[i] + "," + " ";
//                    }
//                    engSynTxt.setText(enSyn);
//                    String a[] = w.getBngSyn().split(",");
//                    String bnSyn = "";
//                    for (int i = 0; i < a.length; i++) {
////                        a[i]=a[i].substring(0,1).toUpperCase() + a[0].substring(1);
//                        bnSyn += a[i] + "," + " ";
//                    }
//                    bangSynText.setText(bnSyn);
//                    examText.setText(finalWordList.get(position).getExample());
//                    antonymsTxt.setText(w.getAntonyms().substring(0, 1).toUpperCase() + w.getAntonyms().substring(1));
//
//                    articleBtn.setVisibility(View.GONE);
//                    listBtn.setVisibility(View.GONE);
//
//                    flag[0] = false;
//                }
//
//            }

//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));


        initialScroll = 0;

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView
                int scrollX = scrollView.getScrollX(); // For HorizontalScrollView

                if (scrollY == 0) {
                    finalWordList.clear();
                    for (int i = 0; i < actualWordList.size(); i++) {
                        if (!actualWordList.get(i).getWord().getBangWord().equals("")) {
                            finalWordList.add(actualWordList.get(i).getWord());
                            Log.d("ssssss", actualWordList.get(i).getWord().getEngWord());
                        }
                    }
//                    Set<Word> uniqueGas = new HashSet<Word>(finalWordList);

//                    finalWordList.clear();
//                    finalWordList.addAll(uniqueGas);

                    adptr.notifyDataSetChanged();
                    initialScroll = scrollY;
//                    for (int i=0;i<finalWordList.size();i++){
//                        Log.d("ssssss",actualWordList.get(i).getWord().getEngWord());
//                    }
                }
                if ((Math.abs(initialScroll - scrollY)) > 20) {
                    finalWordList.clear();
                    String rrrr = getNumberOfWordsDisplayed();
                    String a[] = rrrr.split(" ");

                    for (int i = 0; i < a.length; i++) {
                        for (int j = 0; j < actualWordList.size(); j++) {

                            String temp = a[i];
                            if (temp != null) {

                                if (temp.toLowerCase().equals(actualWordList.get(j).getActualWord())) {
                                    finalWordList.add(actualWordList.get(j).getWord());
                                    break;
                                }
                            }
                        }
                    }

//                    Set<Word> uniqueGas = new HashSet<Word>(finalWordList);
//                    finalWordList.clear();
//                    finalWordList.addAll(uniqueGas);

                    adptr.notifyDataSetChanged();
                    initialScroll = scrollY;
                }
            }
        });


        final boolean[] fontFlag = {true};
        textView.setOnClickListener(new WordTranslation.DoubleClickListener() {
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
//        expandTextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                expandTextView();
//                secondRootLayout.setVisibility(View.GONE);
//            }
//        });

        boolenRecy = new boolean[]{true};
//        expandRecyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                expandRecyView();
//                if (flag[0]) {
//                    flag[0] = false;
//                } else {
//                    flag[0] = true;
//                    lLayout.setVisibility(View.INVISIBLE);
//                    recyclerView.setVisibility(View.VISIBLE);
//                    secondRootLayout.setVisibility(View.GONE);
//                }
//            }
//        });


        adptr.notifyDataSetChanged();

        new EasyProcess(this, 0, 5, textView.getText().toString()).execute();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expandRecyView();
                if (flag[0]) {
                    articleBtn.setVisibility(View.VISIBLE);
                    listBtn.setVisibility(View.VISIBLE);

                    Intent newIntent = new Intent(getApplicationContext(), CropImageActivity.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);

                } else {
                    flag[0] = true;
                    lLayout.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);

                    articleBtn.setVisibility(View.VISIBLE);
                    listBtn.setVisibility(View.VISIBLE);
                }


            }
        });
        final int[] ff = {0};
        final int[] fff = {0};
        final int[] st = {0};
        articleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("qqqqqq", String.valueOf(flag[0]));
//                if (fff[0] == 1) {
//                    fff[0] = 0;

                // expand recycler view
//                if (st==0){
//                    expandRecyView();
//                    if (flag[0]) {
//                        flag[0] = false;
//                    } else {
//                        flag[0] = true;
//                        lLayout.setVisibility(View.INVISIBLE);
//                        recyclerView.setVisibility(View.VISIBLE);
//                        secondRootLayout.setVisibility(View.GONE);
//
//                    }
//                }
                if (st[0] ==0){
                    expandTextView();
                    secondRootLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    st[0] =-1;
                }
                else if(st[0] ==-1){
                    expandTextView();
                    secondRootLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    st[0] =0;
//                    expandRecyView();
//                    if (flag[0]) {
//                        flag[0] = false;
//                    } else {
//                        flag[0] = true;
//                        lLayout.setVisibility(View.INVISIBLE);
//                        recyclerView.setVisibility(View.VISIBLE);
//                        secondRootLayout.setVisibility(View.GONE);
//                    }
//                    st=1;
                }else if (st[0] ==1){
                    expandRecyView();
                    if (flag[0]) {
                        flag[0] = false;
                    } else {
                        flag[0] = true;
                        lLayout.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        secondRootLayout.setVisibility(View.GONE);
                    }
                    st[0] =0;
                }

//                } else if (ff[0] == 0) {
//
//                    expandTextView();
//                    secondRootLayout.setVisibility(View.GONE);
//                    ff[0] = 1;
//                } else {
//                    expandTextView();
//                    secondRootLayout.setVisibility(View.GONE);
//                    ff[0] = 0;
//                }
            }
        });

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ff[0] == 1) {
//                    expandTextView();
//                    secondRootLayout.setVisibility(View.GONE);
//                    ff[0] = 0;
//                } else {
//                    fff[0] = 1;

                // expand recycler view
                if (st[0] ==0){
                    expandRecyView();
                    if (flag[0]) {
                        flag[0] = false;
                    } else {
                        flag[0] = true;
                        lLayout.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        secondRootLayout.setVisibility(View.GONE);
                    }
                    st[0] =1;
                }
                else if (st[0] ==1){
                    expandRecyView();
                    if (flag[0]) {
                        flag[0] = false;
                    } else {
                        flag[0] = true;
                        lLayout.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        secondRootLayout.setVisibility(View.GONE);
                    }
                    st[0] =0;
                }
                else if (st[0] ==-1){
                    expandTextView();
                    secondRootLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    st[0] =0;
                }
//                }
            }
        });

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

    public abstract class DoubleClickListener implements View.OnClickListener {

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


        }

        @Override
        protected Void doInBackground(String... params) {
            Log.d("202020", "rrr :" + result);
            String r[] = result.split(" ");
//            result+="asdsa\"";
            if (start < r.length) {
                String[] links = new String[]{};
                links = getDataByMainThread(start, end);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        spannableString = new SpannableString(textView.getText());

                    }
                });
                for (int i = 0; i < links.length; i++) {

                    Log.d("888888", "data:  " + links[i]);
                    final int finalI = i;
                    final String[] finalLinks = links;
                    final ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {

                            final TextView txtBang = (TextView) findViewById(R.id.popup_bang_word);
                            final TextView txteng = (TextView) findViewById(R.id.popup_eng_word);

                            runOnUiThread(new Runnable() {
                                int pos;

                                @Override
                                public void run() {
                                    Word w = null;
                                    for (int i = 0; i < actualWordList.size(); i++) {
                                        if (finalLinks[finalI].toLowerCase().equals(actualWordList.get(i).getActualWord().toLowerCase())) {
                                            pos = i;
                                            w = actualWordList.get(i).getWord();

                                            break;
                                        }
                                    }

                                    expandRecyView.setVisibility(View.GONE);
                                    secondRootLayout.setVisibility(View.VISIBLE);

                                    String a[] = w.getBngSyn().split(",");
                                    String bnSyn = "";
                                    for (int i = 0; i < a.length; i++) {
                                        bnSyn += a[i] + "," + " ";
                                    }
                                    txtBang.setText(bnSyn);

                                    String enSyn = "";
                                    String b[] = w.getEngSyn().split(",");
                                    for (int i = 0; i < b.length; i++) {
                                        b[i] = b[i].substring(0, 1).toUpperCase() + b[i].substring(1);
                                        enSyn += b[i] + "," + " ";
                                    }
                                    txteng.setText(enSyn);

                                    ImageView close = (ImageView) findViewById(R.id.close);
                                    close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            expandRecyView.setVisibility(View.VISIBLE);
                                            secondRootLayout.setVisibility(View.GONE);
                                        }
                                    });
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
//            Log.d("303030", String.valueOf(spannableString));
            try {
//                if (spannableString!=null){
//                    textView.setText(spannableString, TextView.BufferType.SPANNABLE);
//                }

//                Log.d("xxxuuu",spannableString.toString());
                if (spannableString!=null){
                    Log.d("404040",spannableString.toString());
                    textView.setText(spannableString, TextView.BufferType.SPANNABLE);
                }

            } catch (Exception e) {
                //Log.d("303030", e.getMessage());
                e.printStackTrace();
                Log.e("xxxuuu", e.getMessage());
            }
            if (finalWordList.size() == 0) {
                Toast.makeText(getApplicationContext(), "No Valid Word Found", Toast.LENGTH_SHORT).show();
            }
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


//    public static interface ClickListener {
//        public void onClick(View view, int position);
//
//        public void onLongClick(View view, int position);
//    }
//
//    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
//
//        private WordTranslation.ClickListener clicklistener;
//        private GestureDetector gestureDetector;
//
//        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final WordTranslation.ClickListener clicklistener) {
//
//            this.clicklistener = clicklistener;
//            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//                @Override
//                public boolean onSingleTapUp(MotionEvent e) {
//                    return true;
//                }
//
//                @Override
//                public void onLongPress(MotionEvent e) {
//                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
//                    if (child != null && clicklistener != null) {
//                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
//                    }
////                    Log.d("main","OnLongPress");
//                }
//            });
//        }
//
//        @Override
//        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//            View child = rv.findChildViewUnder(e.getX(), e.getY());
//            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
//                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
//            }
//
//            return false;
//        }
//
//        @Override
//        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//        }
//
//        @Override
//        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//        }
//    }


    @Override
    public void onBackPressed() {

        expandRecyView();
        if (flag[0]) {
            articleBtn.setVisibility(View.VISIBLE);
            listBtn.setVisibility(View.VISIBLE);
            Intent newIntent = new Intent(getApplicationContext(), CropImageActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            super.onBackPressed();
        } else {
            flag[0] = true;
            lLayout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            articleBtn.setVisibility(View.VISIBLE);
            listBtn.setVisibility(View.VISIBLE);
        }

    }


    public String[] getDataByMainThread(int start, int end) {

        List<String> spanList = new ArrayList<>();
        String r[] = result.split(" ");
        Word word;

        if (r.length >= end) {
            for (int j = start; j < end; j++) {
                String temp = r[j];
//                Log.d("ssssss",temp);

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
                        if (!word.getBangWord().equals("কুঠার") && !word.getEngWord().equals(null)) {
                            actualWordList.add(new ActualWordList(temp, word));
                            finalWordList.add(word);
                            spanList.add(temp);
//                            Log.d("ssssss",word.getEngWord());
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
                            actualWordList.add(new ActualWordList(r[j], word));
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

    public boolean dontAccept(String word) {
        word = word.toLowerCase();
        boolean flag = false;
        String[] ignore = new String[]{"the", "am", "is", "are", "was", "ware", "he", "his", "her", "had", "has", "i", "an", "a", "by", "and", "may", "at", "with", "not", "this"};
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                expandRecyView();
                if (flag[0]) {
                    Intent newIntent = new Intent(getApplicationContext(), CropImageActivity.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                    super.onBackPressed();
                } else {
                    flag[0] = true;
                    lLayout.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                break;
        }
        return true;
    }


    public interface ClickListener {

        void onPositionClicked(int position);

        void onLongClicked(int position);
    }

}
