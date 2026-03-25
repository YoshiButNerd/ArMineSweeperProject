package com.arielfriedman.arminesweeperproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arielfriedman.arminesweeperproject.baseActivity.BaseActivity;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.MusicManager;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.SfxManager;

public class LossActivity extends BaseActivity implements View.OnClickListener {

    Button btnHome;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentLayout(R.layout.activity_loss);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        MusicManager.getInstance().stopMusic();
    }

    public void initViews() {
        btnHome = findViewById(R.id.returnHomeBtn);
        btnHome.setSoundEffectsEnabled(false);
        btnHome.setOnClickListener(this);
        addPressAnimation(btnHome);
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
        SharedPreferences prefs = getSharedPreferences("LastScreenBeforeGame", MODE_PRIVATE);
        String lobbyScreen = prefs.getString("lobby_screen", "MainActivity");
        if (lobbyScreen.equals("AdminMainActivity")) {
            intent = new Intent(LossActivity.this, AdminMainActivity.class);
        }
        else if (lobbyScreen.equals("MainActivity")){
            intent = new Intent(LossActivity.this, MainActivity.class);
        }
        else {
            intent = new Intent(LossActivity.this, LoginActivity.class);
        }
        startActivity(intent);
    }
}