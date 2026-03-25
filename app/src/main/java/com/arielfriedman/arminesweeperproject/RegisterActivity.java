package com.arielfriedman.arminesweeperproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arielfriedman.arminesweeperproject.baseActivity.BaseActivity;
import com.arielfriedman.arminesweeperproject.model.User;
import com.arielfriedman.arminesweeperproject.services.DatabaseService;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.SfxManager;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/// Activity for registering the user
/// This activity is used to register the user
/// It contains fields for the user to enter their information
/// It also contains a button to register the user
/// When the user is registered, they are redirected to the main activity
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private EditText etEmail, etPassword, etFName, etLName, etPhone;
    private Button btnRegister, btnLogin;
    private DatabaseService databaseService;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        /// set the layout for the activity
        setContentLayout(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Initviews();
    }

    public void Initviews() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        databaseService = DatabaseService.getInstance();
        /// get the views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFName = findViewById(R.id.etFname);
        etLName = findViewById(R.id.etLname);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        /// set the click listener
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setSoundEffectsEnabled(false);
        btnLogin.setSoundEffectsEnabled(false);
        addPressAnimation(btnLogin);
        addPressAnimation(btnRegister);
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
        if (v.getId() == btnRegister.getId()) {
            Log.d(TAG, "onClick: Register button clicked");

            /// get the input from the user
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String fName = etFName.getText().toString();
            String lName = etLName.getText().toString();
            String phone = etPhone.getText().toString();
            boolean isAdmin = false;

            /// Validate input
            Log.d(TAG, "onClick: Validating input...");

            boolean isValid = validator(fName, lName, phone, email, password);

            Log.d(TAG, "onClick: Registering user...");
            /// Register user
            if (isValid) {
                registerUser(fName, lName, phone, email, password, isAdmin);
            }
            else {
                Toast.makeText(RegisterActivity.this, "Not all fields are filled", Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == btnLogin.getId()) {
            /// Navigate back to Login Activity
            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        }
    }


    /// Register the user
    private void registerUser(String fname, String lname, String phone, String email, String password, boolean isAdmin) {
        Log.d(TAG, "registerUser: Registering user...");


        /// create a new user object
        User user = new User("4545", fname, lname, phone, email, password, false);


        /// proceed to create the user
        createUserInDatabase(user);

    }


    private void createUserInDatabase(User user) {
        databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<String>() {
            @Override
            public void onCompleted(String uid) {
                Log.d("TAG", "createUserInDatabase: User created successfully");
                /// save the user to shared preferences
                user.setId(uid);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("email", user.getEmail());
                editor.putString("password", user.getPassword());

                editor.commit();
                Log.d("TAG", "createUserInDatabase: Redirecting to LoginActivity");
                /// Redirect to LoginActivity and clear back stack to prevent user from going back to register screen
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                /// clear the back stack (clear history) and start the LoginActivity
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(loginIntent);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "createUserInDatabase: Failed to create user", e);

                /// show error message to user
                String message = "Registration failed. Please try again";

                if (e instanceof FirebaseAuthWeakPasswordException) {
                    message = "Password must be at least 6 characters";
                }
                else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    message = "Invalid email format";
                }
                else if (e instanceof FirebaseAuthUserCollisionException) {
                    message = "This email is already registered";
                }
                else if (e instanceof FirebaseNetworkException) {
                    message = "Network error, check your internet connection";
                }
                else if (e instanceof FirebaseAuthException) {
                    FirebaseAuthException authException = (FirebaseAuthException) e;
                    message = "Authentication error: " + authException.getErrorCode();
                }
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
    private boolean validator(String fname, String lname, String phone, String email, String password) {
        boolean valid = true;
        String[] fields = {fname, lname, phone, email, password};
        for (String field : fields) {
            if (field.trim().isEmpty()) {
                valid = false;
                break;
            }
        }
        return valid;
    }
}
