package com.arielfriedman.arminesweeperproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arielfriedman.arminesweeperproject.baseActivity.BaseActivity;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;
import com.arielfriedman.arminesweeperproject.services.DatabaseService;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.MusicManager;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.SfxManager;

/// Activity for logging in the user
    /// This activity is used to log in the user
    /// It contains fields for the user to enter their email and password
    /// It also contains a button to log in the user
    /// When the user is logged in, they are redirected to the main activity
    public class LoginActivity extends BaseActivity implements View.OnClickListener {

        private static final String TAG = "LoginActivity";
        private DatabaseService databaseService;
        private EditText etEmail, etPassword;
        private Button btnLogin, btnGoRegister;

        public static final String MyPREFERENCES = "MyPrefs" ;

        static final String ADMINEMAIL = "gigaadmin@gmail.com";
        static final String ADMINPASS = "adminpass123";
        SharedPreferences sharedpreferences;

        public static final String SCREENPREFS = "LastScreenBeforeGame" ;
        SharedPreferences sharedgameprefs;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            /// set the layout for the activity
            setContentLayout(R.layout.activity_login);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            Initviews();
        }

        public void Initviews() {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            sharedgameprefs = getSharedPreferences(SCREENPREFS, Context.MODE_PRIVATE);
            /// get the views
            databaseService = DatabaseService.getInstance();
            etEmail = findViewById(R.id.etLoginEmail);
            etPassword = findViewById(R.id.etLoginPassword);
            btnLogin = findViewById(R.id.btnLoginLog);
            btnGoRegister = findViewById(R.id.btnGoRegisterReg);
            String email2 = sharedpreferences.getString("email", "");
            String pass2 = sharedpreferences.getString("password", "");
            etEmail.setText(email2);
            etPassword.setText(pass2);

            /// set the click listener
            btnLogin.setOnClickListener(this);
            btnGoRegister.setOnClickListener(this);
            btnLogin.setSoundEffectsEnabled(false);
            btnGoRegister.setSoundEffectsEnabled(false);
            addPressAnimation(btnGoRegister);
            addPressAnimation(btnLogin);
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
            if (v.getId() == btnLogin.getId()) {
                Log.d(TAG, "onClick: Login button clicked");

                /// get the email and password entered by the user
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("email", email);
                editor.putString("password", password);

                editor.commit();

                if (email.equals(ADMINEMAIL) && password.equals(ADMINPASS)) {
                    Log.d(TAG, "user is admin");
                    Intent registerIntent = new Intent(LoginActivity.this, AdminMainActivity.class);
                    startActivity(registerIntent);
                }
                else {
                    /// log the email and password
                    Log.d(TAG, "onClick: Email: " + email);
                    Log.d(TAG, "onClick: Password: " + password);

                    Log.d(TAG, "onClick: Validating input...");
                    /// Validate input
                    Log.d(TAG, "onClick: Logging in user...");

                    /// Login user
                    loginUser(email, password);
                }
            } else if (v.getId() == btnGoRegister.getId()) {
                /// Navigate to Register Activity
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        }
        private void loginUser(String email, String password) {
            databaseService.LoginUser(email, password, new DatabaseService.DatabaseCallback<String>() {
                /// Callback method called when the operation is completed
                @Override
                public void onCompleted(String  uid) {
                    Log.d(TAG, "onCompleted: User logged in: " + uid.toString());
                    /// save the user data to shared preferences
                    // SharedPreferencesUtil.saveUser(LoginActivity.this, user);
                    /// Redirect to main activity and clear back stack to prevent user from going back to login screen
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    /// Clear the back stack (clear history) and start the MainActivity
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e(TAG, "onFailed: Failed to retrieve user data", e);
                    /// Show error message to user
                    etPassword.setError("Invalid email or password");
                    etPassword.requestFocus();
                    /// Sign out the user if failed to retrieve user data
                    /// This is to prevent the user from being logged in again
                    //SharedPreferencesUtil.signOutUser(LoginActivity.this);
                }
            });
        }

        public void startNewRun() {
            RunState runstate = RunState.getInstance();
            runstate.setNewRun();
            SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
            int musicVol = prefs.getInt("music_volume", 50);
            float volume = musicVol / 100f;
            MusicManager.getInstance().startMusic(this, R.raw.game_music, volume);
            //save screen to come back to after a loss
            SharedPreferences.Editor editor = sharedgameprefs.edit();
            editor.putString("lobby_screen", "LoginActivity");
            editor.commit();
        }
    }