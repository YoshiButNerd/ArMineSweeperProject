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

import com.arielfriedman.arminesweeperproject.BaseActivity.BaseActivity;

public class ShopActivity extends BaseActivity implements View.OnClickListener {

    Button btnItem1;
    Button btnItem2;
    Button btnItem3;

    Button btnGoNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentLayout(R.layout.activity_shop);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        InitViews();
    }

    public void InitViews() {
        btnItem1 = findViewById(R.id.item1Btn);
        btnItem2 = findViewById(R.id.item2Btn);
        btnItem3 = findViewById(R.id.item3Btn);
        btnGoNext = findViewById(R.id.goNextBtn);
        btnGoNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(ShopActivity.this, GameActivity.class);
        startActivity(intent);
    }
}