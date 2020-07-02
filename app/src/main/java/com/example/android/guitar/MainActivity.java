package com.example.android.guitar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.android.guitar.exercise.CatalogActivity;
import com.example.android.guitar.firebaseblogapp.BlogActivity;
import com.example.android.guitar.learning.LearningActivity;
import com.example.android.guitar.maps.MapActivity;
import com.example.android.guitar.sharedpref.SharedPrefActivity;
import com.example.android.guitar.web.WebActivity;

public class MainActivity extends AppCompatActivity  {

    private LinearLayout mTitleLayout;
    private ScrollView mScrollView;
    private TextView mTextView;
    private TextView mTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mTextView = (TextView) findViewById(R.id.maintitle);
        mTextView2 = (TextView) findViewById(R.id.subtitle);

        mTitleLayout = (LinearLayout) findViewById(R.id.titleLayout);

        mTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FadeInRight)
                        .duration(500)
                        .playOn(mTextView);
                YoYo.with(Techniques.FadeInLeft)
                        .duration(500)
                        .playOn(mTextView2);
            }
        });

//        mScrollView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_transition_left_to_right));
        mTextView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_transition_left_to_right));
        mTextView2.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_transition_right_to_left));


    }

    public void learnActivity(View view) {
        Intent intent = new Intent(MainActivity.this, LearningActivity.class);
        startActivity(intent);
    }

    public void webActivity(View view) {
        Intent intent = new Intent(MainActivity.this, WebActivity.class);
        startActivity(intent);
    }

    public void storageActivity(View view) {
        Intent intent = new Intent(MainActivity.this, CatalogActivity.class);
        startActivity(intent);
    }

    public void blogActivity(View view) {
        Intent intent = new Intent(MainActivity.this, BlogActivity.class);
        startActivity(intent);
    }
    public void mapsActivity(View view) {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
    }
    public void songsActivity(View view) {
        Intent intent = new Intent(MainActivity.this, SharedPrefActivity.class);
        startActivity(intent);
    }
}

