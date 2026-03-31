package com.arielfriedman.arminesweeperproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arielfriedman.arminesweeperproject.baseActivity.BaseActivity;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.MusicManager;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.SfxManager;
import com.arielfriedman.arminesweeperproject.specialClasses.NotificationReceiver;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    Button btnGoInfo;
    Button btnGoGame;
    Button btnGoLogin;
    Button btnGoTutorial;
    Intent intent;

    public static final String SCREENPREFS = "LastScreenBeforeGame" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentLayout(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Initviews();
        requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        int interval = 60 * 60 * 24 * 1000;

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + interval,
                interval,
                pendingIntent
        );
    }

    public void Initviews() {
        sharedpreferences = getSharedPreferences(SCREENPREFS, Context.MODE_PRIVATE);
        btnGoInfo = findViewById(R.id.goInfoBtn);
        btnGoGame = findViewById(R.id.goGameBtn);
        btnGoLogin = findViewById(R.id.goLoginBtn);
        btnGoTutorial = findViewById(R.id.goTutorialBtn);
        btnGoInfo.setOnClickListener(this);
        btnGoGame.setOnClickListener(this);
        btnGoLogin.setOnClickListener(this);
        btnGoTutorial.setOnClickListener(this);
        btnGoInfo.setSoundEffectsEnabled(false);
        btnGoGame.setSoundEffectsEnabled(false);
        btnGoLogin.setSoundEffectsEnabled(false);
        btnGoTutorial.setSoundEffectsEnabled(false);
        addPressAnimation(btnGoGame);
        addPressAnimation(btnGoInfo);
        addPressAnimation(btnGoLogin);
        addPressAnimation(btnGoTutorial);
    }

    private void addPressAnimation(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start();
                } else if (event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        SfxManager.play(this, R.raw.sfx_clickbtn);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("lobby_screen", "MainActivity");
        editor.commit();
        if (v == btnGoInfo) {
            intent = new Intent(MainActivity.this, InfoActivity.class);
        }
        else if (v == btnGoGame) {
            startNewRun();
            intent = new Intent(MainActivity.this, GameActivity.class);
            Log.d("MainActivity", "Set intent and runstate successfully");
        }
        else if (v == btnGoLogin) {
            intent = new Intent(MainActivity.this, LoginActivity.class);
        }
        else if (v == btnGoTutorial) {
            intent = new Intent(MainActivity.this, TutorialActivity.class);
        }
        startActivity(intent);
    }

    public void startNewRun() {
        RunState runstate = RunState.getInstance();
        runstate.setNewRun();
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int musicVol = prefs.getInt("music_volume", 50);
        float volume = musicVol / 100f;
        MusicManager.getInstance().startMusic(this, R.raw.game_music, volume);
    }
}