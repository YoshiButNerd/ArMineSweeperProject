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
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arielfriedman.arminesweeperproject.baseActivity.BaseActivity;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;
import com.arielfriedman.arminesweeperproject.specialClasses.BtnHandler;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.MusicManager;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.SfxManager;
import com.arielfriedman.arminesweeperproject.specialClasses.NotificationReceiver;

public class AdminMainActivity extends BaseActivity implements View.OnClickListener{

    Button aBtnGoInfo;
    Button aBtnGoGame;
    Button aBtnGoLogin;
    Button aBtnGoUsersList;
    Button aBtnGoTutorial;
    Intent intent;

    public static final String SCREENPREFS = "LastScreenBeforeGame" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentLayout(R.layout.activity_admin_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Initviews();
        setNotifs();
    }
    public void Initviews() {
        sharedpreferences = getSharedPreferences(SCREENPREFS, Context.MODE_PRIVATE);
        aBtnGoInfo = findViewById(R.id.aGoInfoBtn);
        aBtnGoGame = findViewById(R.id.aGoGameBtn);
        aBtnGoUsersList = findViewById(R.id.aGoInfoUsersBtn);
        aBtnGoLogin = findViewById(R.id.aGoLoginBtn);
        aBtnGoTutorial = findViewById(R.id.aGoTutorialBtn);
        BtnHandler.handleBtn(aBtnGoInfo);
        BtnHandler.handleBtn(aBtnGoGame);
        BtnHandler.handleBtn(aBtnGoLogin);
        BtnHandler.handleBtn(aBtnGoUsersList);
        BtnHandler.handleBtn(aBtnGoTutorial);
        aBtnGoInfo.setOnClickListener(this);
        aBtnGoGame.setOnClickListener(this);
        aBtnGoLogin.setOnClickListener(this);
        aBtnGoUsersList.setOnClickListener(this);
        aBtnGoTutorial.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SfxManager.play(this, R.raw.sfx_clickbtn);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("lobby_screen", "AdminMainActivity");
        editor.commit();
        if (v == aBtnGoInfo) {
            intent = new Intent(AdminMainActivity.this, InfoActivity.class);
        }
        else if (v == aBtnGoGame) {
            startNewRun();
            intent = new Intent(AdminMainActivity.this, GameActivity.class);
            Log.d("MainActivity", "Set intent and runstate successfully");
        }
        else if (v == aBtnGoLogin) {
            intent = new Intent(AdminMainActivity.this, LoginActivity.class);
        }
        else if (v == aBtnGoUsersList) {
            intent = new Intent(AdminMainActivity.this, UsersListActivity.class);
        }
        else if (v == aBtnGoTutorial) {
            intent = new Intent(AdminMainActivity.this, TutorialActivity.class);
        }
        startActivity(intent);
    }

    public void setNotifs() {
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

    public void startNewRun() {
        RunState runState = RunState.getInstance();
        runState.setNewRun();
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int musicVol = prefs.getInt("music_volume", 50);
        float volume = musicVol / 100f;
        MusicManager.getInstance().startMusic(this, R.raw.game_music, volume);
    }
}