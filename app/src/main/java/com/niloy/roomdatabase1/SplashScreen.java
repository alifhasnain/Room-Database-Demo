package com.niloy.roomdatabase1;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";

    AnimationDrawable loadAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView image = findViewById(R.id.imageView);
        image.setBackgroundResource(R.drawable.loading_anim);

        loadAnimation = (AnimationDrawable)image.getBackground();
        loadAnimation.start();

        TextView splashText = findViewById(R.id.splashText);
        TextView splashText2 = findViewById(R.id.splashText2);

        /*splashText.animate().alpha(1f).setDuration(2000);
        splashText2.animate().alpha(1f).setDuration(2000);*/

        //This is for Splash Screen Of App
        Thread splashScreenThread = new Thread()    {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(SplashScreen.this , MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        splashScreenThread.start();
    }
}
