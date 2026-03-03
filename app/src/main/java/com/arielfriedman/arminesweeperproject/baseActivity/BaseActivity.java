package com.arielfriedman.arminesweeperproject.baseActivity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arielfriedman.arminesweeperproject.R;
import com.arielfriedman.arminesweeperproject.specialClasses.SettingsDialog;

public class BaseActivity extends AppCompatActivity {

    ImageButton settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initBaseViews();
    }

    public void initBaseViews() {
        settingsBtn = findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(v -> openDialog());
    }

    protected void setContentLayout(int layoutResId) {
        FrameLayout contentFrame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResId, contentFrame, true);
    }

    public void openDialog() {
       SettingsDialog dialog = new SettingsDialog();
       dialog.show(getSupportFragmentManager(), "settings");
    }
}