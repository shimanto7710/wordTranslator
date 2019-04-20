package com.example.shimantoahmed.learnvocabulary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shimantoahmed.learnvocabulary.Activity.CropImageActivity;
import com.example.shimantoahmed.learnvocabulary.Activity.MainActivity;
import com.example.shimantoahmed.learnvocabulary.Activity.WordTranslation;
import com.example.shimantoahmed.learnvocabulary.Fragment.BlankFragment;
import com.example.shimantoahmed.learnvocabulary.Fragment.BlankFragment2;
import com.example.shimantoahmed.learnvocabulary.Fragment.FragDictionary;
import com.example.shimantoahmed.learnvocabulary.Fragment.FragOne;
import com.example.shimantoahmed.learnvocabulary.Fragment.FragThree;
import com.example.shimantoahmed.learnvocabulary.Fragment.FragTwo;
import com.example.shimantoahmed.learnvocabulary.db.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FlashScreen extends AppCompatActivity {

//    private ProgressBar progressbar;
//    private int progressCount;
//    private Handler progressHandler = new Handler();
//    private TextView progressText ;
//    Button btn;
    Boolean finishFlag=false;
    DatabaseHelper myDbHelper;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnNext;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }


        new EasyProcess(this).execute();



        finishFlag=false;


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
//        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

//        btnSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                launchHomeScreen();
//            }
//        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    final ProgressDialog progressDialog = ProgressDialog.show(FlashScreen.this, "", "Please wait...");
                    new Thread() {
                        public void run() {
                            try{
                                while (!finishFlag){
                                    Log.d("666666","flag :"+finishFlag);
                                }
                                progressDialog.dismiss();
                                launchHomeScreen();
                                //your code here.....
                            }
                            catch (Exception e) {
                                Log.e("tag", e.getMessage());
                            }
                            // dismiss the progress dialog
                            Log.d("666666","flag :"+finishFlag);

                        }
                    }.start();


                }
            }
        });

    }



    public void initializeDatabase() {

        try {
            myDbHelper.createDataBase();
//            Toast.makeText(MainActivitya.this, "Create Success", Toast.LENGTH_SHORT).show();
            Log.d("xxx ", "createDataBase(): success");
        } catch (IOException ioe) {
            Log.d("xxx ", "createDataBase(): failed");
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
            Log.d("xxx ", "openDataBase(): success");
//            Toast.makeText(MainActivitya.this, "open Success", Toast.LENGTH_SHORT).show();
        } catch (SQLException sqle) {
            Log.d("xxx ", "openDataBase(): faild");
            throw sqle;
        }



    }


    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(getApplicationContext(), CropImageActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
//                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
//                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


    public class EasyProcess extends AsyncTask<String, Boolean, Void> {

        Context context;

        public EasyProcess(Context context) {
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(String... params) {

            myDbHelper = new DatabaseHelper(getApplicationContext());
            initializeDatabase();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d("dipbanik","complete");
            Toast.makeText(getApplicationContext(),"Database Copied",Toast.LENGTH_SHORT).show();
            finishFlag=true;
//            btnNext.setVisibility();
        }

    }

}
