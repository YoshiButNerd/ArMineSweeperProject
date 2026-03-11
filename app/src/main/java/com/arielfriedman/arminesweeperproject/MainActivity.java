package com.arielfriedman.arminesweeperproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    Intent intent;

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
        btnGoInfo = findViewById(R.id.goInfoBtn);
        btnGoGame = findViewById(R.id.goGameBtn);
        btnGoLogin = findViewById(R.id.goLoginBtn);
        btnGoInfo.setOnClickListener(this);
        btnGoGame.setOnClickListener(this);
        btnGoLogin.setOnClickListener(this);
        btnGoInfo.setSoundEffectsEnabled(false);
        btnGoGame.setSoundEffectsEnabled(false);
        btnGoLogin.setSoundEffectsEnabled(false);
    }

    @Override
    public void onClick(View v) {
        SfxManager.play(this, R.raw.sfx_clickbtn);
        if (v == btnGoInfo){
            intent = new Intent(MainActivity.this, InfoActivity.class);
            intent.putExtra("PREVIOUS_ACTIVITY", "MainActivity");
        }
        else if (v == btnGoGame){
            startNewRun();
            intent = new Intent(MainActivity.this, GameActivity.class);
            Log.d("MainActivity", "Set intent and runstate successfully");
        }
        else if (v == btnGoLogin){
            intent = new Intent(MainActivity.this, LoginActivity.class);
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