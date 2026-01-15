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

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener{

    Button aBtnGoInfo;
    Button aBtnGoGame;
    Button aBtnGoUserInfo;

    Button btnGoUsersList;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Initviews();
    }

        public void Initviews() {
            aBtnGoInfo = findViewById(R.id.aGoInfoBtn);
            aBtnGoGame = findViewById(R.id.aGoGameBtn);
            aBtnGoUserInfo = findViewById(R.id.usersInfoBtn);
            btnGoUsersList = findViewById(R.id.goInfoUsersBtn);
            aBtnGoInfo.setOnClickListener(this);
            aBtnGoGame.setOnClickListener(this);
            btnGoUsersList.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v == aBtnGoInfo){
                intent = new Intent(AdminMainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
            else if (v == aBtnGoGame){
                intent = new Intent(AdminMainActivity.this, GameActivity.class);
                startActivity(intent);
            }
            else if (v == aBtnGoUserInfo){
                intent = new Intent(AdminMainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
            else if (v == btnGoUsersList){
                intent = new Intent(AdminMainActivity.this, UsersListActivity.class);
                startActivity(intent);
            }
        }
    }