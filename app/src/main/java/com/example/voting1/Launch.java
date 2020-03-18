package com.example.voting1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Launch extends AppCompatActivity {
    Animation topdown, left;
    ImageView launchimg;
    TextView launchtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        topdown = AnimationUtils.loadAnimation(this,R.anim.topdown);
        left = AnimationUtils.loadAnimation(this,R.anim.txtanim);
        launchimg = findViewById(R.id.launch_logo);
        launchtxt = findViewById(R.id.launch_text);

        launchimg.setAnimation(topdown);
        launchtxt.setAnimation(left);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Pair[] pairs= new Pair[2];
                pairs[0] = new Pair<View,String>(launchimg,"logo");
                pairs[1] = new Pair<View,String>(launchtxt,"txt");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Launch.this,pairs);
                Intent next = new Intent(Launch.this, MainActivity.class);
                startActivity(next,options.toBundle());
                finish();


            }
        }, 5000);
    }
}
