package com.niloy.roomdatabase1;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

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

        //This is for Splash Screen Of App
        SplashAnimation splashAnimation = new SplashAnimation();
        splashAnimation.start();
    }

    class SplashAnimation extends Thread implements Runnable   {

        @Override
        public void run() {
            try {
                //In here thread will sleep for 3 second
                sleep(2000);
                Intent intent = new Intent(SplashScreen.this , MainActivity.class);
                startActivity(intent);
                //We finished the activity so we won't be back here when back key is pressed
                finish();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
