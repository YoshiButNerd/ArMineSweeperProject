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

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGoBack;
    Intent intent;
    String previousActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Initviews();
    }

    public void Initviews() {
        btnGoBack = findViewById(R.id.goBackBtn);
        btnGoBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        previousActivity = getIntent().getStringExtra("PREVIOUS_ACTIVITY");
        if (previousActivity.equals("AdminMainActivity"))
            intent = new Intent(InfoActivity.this, AdminMainActivity.class);
        else if (previousActivity.equals("MainActivity"))
            intent = new Intent(InfoActivity.this, MainActivity.class);
        startActivity(intent);
    }
}