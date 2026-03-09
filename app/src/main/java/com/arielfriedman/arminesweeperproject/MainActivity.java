package com.arielfriedman.arminesweeperproject;

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

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private SharedPreferences prefs;
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
    }

    public void Initviews() {
        btnGoInfo = findViewById(R.id.goInfoBtn);
        btnGoGame = findViewById(R.id.goGameBtn);
        btnGoLogin = findViewById(R.id.goLoginBtn);
        btnGoInfo.setOnClickListener(this);
        btnGoGame.setOnClickListener(this);
        btnGoLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

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
    }
}