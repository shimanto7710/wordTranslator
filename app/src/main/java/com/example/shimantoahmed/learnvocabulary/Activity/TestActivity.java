package com.example.shimantoahmed.learnvocabulary.Activity;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shimantoahmed.learnvocabulary.R;
import com.example.shimantoahmed.learnvocabulary.others.DropDownAnim;



import java.lang.reflect.Method;


public class TestActivity extends AppCompatActivity {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView=(TextView)findViewById(R.id.text);


    }


    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.MATCH_PARENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
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


//    class Ani extends Animation {
//        View view;
//        Layout layout;
//
//        public Ani(View view) {
//            this.view=view;
//        }
//
//        @Override
//        protected void applyTransformation(float interpolatedTime, Transformation t) {
//            int newHeight;
//
//            newHeight = (int) (initialHeight * interpolatedTime);
////            ril1.removeAllViews();
//            LinearLayout linearLayout;
//            linearLayout
//            view.setWidth(100);
//            btn.setHeight(300);
////            btn.setText("as");
////            ril1.addView(btn);
//            ril1.getLayoutParams().height = newHeight;
////            ril1.requestLayout();
//        }
//
//        @Override
//        public void initialize(int width, int height, int parentWidth, int parentHeight) {
//            super.initialize(width, height, parentWidth, parentHeight);
//            initialHeight = view.getHeight();
//        }
//
//        @Override
//        public boolean willChangeBounds() {
//            return true;
//        }
//    }

}



