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
import com.arielfriedman.arminesweeperproject.specialClasses.BtnHandler;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.MusicManager;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.SfxManager;

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
            databaseService = DatabaseService.getInstance();
            etEmail = findViewById(R.id.etLoginEmail);
            etPassword = findViewById(R.id.etLoginPassword);
            btnLogin = findViewById(R.id.btnLoginLog);
            btnGoRegister = findViewById(R.id.btnGoRegisterReg);
            String email2 = sharedpreferences.getString("email", "");
            String pass2 = sharedpreferences.getString("password", "");
            etEmail.setText(email2);
            etPassword.setText(pass2);
            btnLogin.setOnClickListener(this);
            btnGoRegister.setOnClickListener(this);
            BtnHandler.handleBtn(btnLogin);
            BtnHandler.handleBtn(btnGoRegister);
        }

        @Override
        public void onClick(View v) {
            SfxManager.play(this, R.raw.sfx_clickbtn);
            if (v.getId() == btnLogin.getId()) {
                Log.d(TAG, "onClick: Login button clicked");

                // get the email and password entered by the user
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    etPassword.setError("נדרש למלא את כל השדות");
                    etPassword.requestFocus();
                }

                else {
                    // log the email and password
                    Log.d(TAG, "onClick: Email: " + email);
                    Log.d(TAG, "onClick: Password: " + password);

                    loginUser(email, password);
                }
            }
            else if (v.getId() == btnGoRegister.getId()) {
                // Navigate to Register Activity
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        }

        private void loginUser(String email, String password) {
            databaseService.LoginUser(email, password, new DatabaseService.DatabaseCallback<String>() {
                // Callback method called when the operation is completed
                @Override
                public void onCompleted(String  uid) {
                    Log.d(TAG, "onCompleted: User logged in: " + uid.toString());
                    // save the user data to shared preferences
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("email", email);
                    editor.putString("password", password);

                    editor.commit();



                    if (email.equals(ADMINEMAIL) && password.equals(ADMINPASS)) {


                        Intent registerIntent = new Intent(LoginActivity.this, AdminMainActivity.class);
                        startActivity(registerIntent);
                    }
                    else {
                        // Redirect to main activity and clear back stack to prevent user from going back to login screen
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        // (clear history) and start the MainActivity
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                    }
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e(TAG, "onFailed: Failed to retrieve user data", e);
                    // Show error message to user
                    etPassword.setError("אימייל או סיסמה שגויים");
                    etPassword.requestFocus();
                }
            });
        }
    }