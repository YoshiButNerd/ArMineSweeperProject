package com.arielfriedman.arminesweeperproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    private ImageView myImageView;
    private TextView myTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myImageView = findViewById(R.id.imageView);
        myTitleView = findViewById(R.id.splashTitle);

        Thread mSplashThread = new Thread(){
            @Override
            public void run(){
                try{
                    synchronized (this){
                        MediaPlayer music = MediaPlayer.create(Splash.this, R.raw.elevmusic);
                        music.start();
                        Animation myFadeInAnimation = AnimationUtils.loadAnimation(Splash.this, R.anim.tween);
                        myImageView.startAnimation(myFadeInAnimation);
                        myTitleView.startAnimation(myFadeInAnimation);
                        wait(3000);
                        music.stop();
                        music.release();
                    }
                }
                catch (InterruptedException ex){
                }
                finish();

                Intent intent = new Intent(Splash.this, LoginActivity.class);
                startActivity(intent);
            }

        };
        mSplashThread.start();
    }
}