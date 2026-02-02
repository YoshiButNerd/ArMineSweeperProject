package com.arielfriedman.arminesweeperproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGoInfo;
    Button btnGoGame;
    Button btnGoLogin;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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
            intent.putExtra("PREVIOUS_ACTIVITY", InfoActivity.class.getSimpleName());
        }
        else if (v == btnGoGame){
            intent = new Intent(MainActivity.this, GameActivity.class);
        }
        else if (v == btnGoLogin){
            intent = new Intent(MainActivity.this, LoginActivity.class);
        }
        startActivity(intent);
    }
}