package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {
    // Firebase variables
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        startActivity(new Intent(LogoutActivity.this, LoginActivity.class));
        finish();
    }
}
