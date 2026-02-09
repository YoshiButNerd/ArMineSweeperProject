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

public class AdminMainActivity extends BaseActivity implements View.OnClickListener{

    Button aBtnGoInfo;
    Button aBtnGoGame;
    Button aBtnGoLogin;
    Button aBtnGoUsersList;

    Intent intent;

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
    }

        public void Initviews() {
            aBtnGoInfo = findViewById(R.id.aGoInfoBtn);
            aBtnGoGame = findViewById(R.id.aGoGameBtn);
            aBtnGoUsersList = findViewById(R.id.goInfoUsersBtn);
            aBtnGoLogin = findViewById(R.id.goLoginBtn);
            aBtnGoInfo.setOnClickListener(this);
            aBtnGoGame.setOnClickListener(this);
            aBtnGoLogin.setOnClickListener(this);
            aBtnGoUsersList.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == aBtnGoInfo){
                intent = new Intent(AdminMainActivity.this, InfoActivity.class);
                intent.putExtra("PREVIOUS_ACTIVITY", "AdminMainActivity");
            }
            else if (v == aBtnGoGame){
                intent = new Intent(AdminMainActivity.this, GameActivity.class);
            }
            else if (v == aBtnGoLogin){
                intent = new Intent(AdminMainActivity.this, LoginActivity.class);
            }
            else if (v == aBtnGoUsersList){
                intent = new Intent(AdminMainActivity.this, UsersListActivity.class);
            }
            startActivity(intent);
        }
    }