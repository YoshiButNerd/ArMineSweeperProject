package com.arielfriedman.arminesweeperproject;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arielfriedman.arminesweeperproject.adapters.UserAdapter;
import com.arielfriedman.arminesweeperproject.model.User;
import com.arielfriedman.arminesweeperproject.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UsersListActivity";
    private UserAdapter userAdapter;
    private TextView tvUserCount;
    private DatabaseService databaseService;
    Button btnGoBack;
    Intent intent;
    ArrayList<User>users=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Initviews();
    }

    public void Initviews() {
        databaseService=DatabaseService.getInstance();
        RecyclerView usersList = findViewById(R.id.rv_users_list);
        tvUserCount = findViewById(R.id.tv_user_count);
        usersList.setLayoutManager(new LinearLayoutManager(this));
        userAdapter=new UserAdapter(users);
        usersList.setAdapter(userAdapter);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseService.getUserList(new DatabaseService.DatabaseCallback<List<User>>() {


            @Override
            public void onCompleted(List<User> users) {
                userAdapter.setUserList(users);
                tvUserCount.setText("Total users: " + users.size());
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to get users list", e);
            }
        });
    }

    @Override
    public void onClick(View view) {
        intent = new Intent(UsersListActivity.this, AdminMainActivity.class);
        startActivity(intent);
    }
}