package com.arielfriedman.arminesweeperproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arielfriedman.arminesweeperproject.adapters.ScoreAdapter;
import com.arielfriedman.arminesweeperproject.adapters.UserAdapter;
import com.arielfriedman.arminesweeperproject.baseActivity.BaseActivity;
import com.arielfriedman.arminesweeperproject.model.User;
import com.arielfriedman.arminesweeperproject.services.DatabaseService;
import com.arielfriedman.arminesweeperproject.specialClasses.BtnHandler;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.SfxManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreListActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "UsersListScoreActivity";
    private ScoreAdapter scoreAdapter;
    private TextView tvScoreUserCount;
    private DatabaseService databaseService;
    Button btnGoBack;
    Intent intent;
    String previousActivity;
    ArrayList<User> usersScore = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentLayout(R.layout.activity_score_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Initviews();
    }

    public void Initviews() {
        databaseService = DatabaseService.getInstance();
        RecyclerView usersScoreList = findViewById(R.id.rv_users_score_list);
        tvScoreUserCount = findViewById(R.id.tv_score_user_count);
        usersScoreList.setLayoutManager(new LinearLayoutManager(this));
        scoreAdapter = new ScoreAdapter(usersScore);
        usersScoreList.setAdapter(scoreAdapter);
        btnGoBack = findViewById(R.id.btnGoBackScore);
        BtnHandler.handleBtn(btnGoBack);
        btnGoBack.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseService.getUserList(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                // Sort users by score in descending order (highest score first)
                Collections.sort(users, (u1, u2) -> Integer.compare(u2.getScore(), u1.getScore()));
                
                scoreAdapter.setScoreList(users);
                tvScoreUserCount.setText("Total users: " + users.size());
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to get users list", e);
            }
        });
    }

    @Override
    public void onClick(View view) {
        SfxManager.play(this, R.raw.sfx_clickbtn);
        SharedPreferences prefs = getSharedPreferences("LastScreenBeforeGame", MODE_PRIVATE);
        previousActivity = prefs.getString("lobby_screen", "MainActivity");
        if (previousActivity.equals("AdminMainActivity"))
            intent = new Intent(ScoreListActivity.this, AdminMainActivity.class);
        else if (previousActivity.equals("MainActivity"))
            intent = new Intent(ScoreListActivity.this, MainActivity.class);
        else {
            intent = new Intent(ScoreListActivity.this, LoginActivity.class);
        }
        startActivity(intent);
    }
}
