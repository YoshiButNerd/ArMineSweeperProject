package com.arielfriedman.arminesweeperproject;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button send;
    String fname, lname, email, phone;
    EditText dataFname;
    EditText dataLname;
    EditText dataEtEmail;
    EditText dataEtPhone;
    Intent sendPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        send = findViewById(R.id.btnReg);
        send.setOnClickListener(this);
        dataFname = findViewById(R.id.etFname);
        dataLname = findViewById(R.id.etLname);
        dataEtEmail = findViewById(R.id.etEmail);
        dataEtPhone = findViewById(R.id.etPhone);
    }

    @Override
    public void onClick(View v) {
        fname = dataFname.getText().toString();
        lname = dataLname.getText().toString();
        email = dataEtEmail.getText().toString();
        phone = dataEtPhone.getText().toString();
        sendPage = new Intent(this, ActShowData.class);
        sendPage.putExtra("fname", fname);
        sendPage.putExtra("lname", lname);
        sendPage.putExtra("email", email);
        sendPage.putExtra("phone", phone);
        startActivity(sendPage);
    }
}
