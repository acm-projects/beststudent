package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // user input variables
    private EditText userEmail;
    private EditText userPassword;

    // Firebase variables
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // find text fields of existing user
        userEmail = findViewById(R.id.editTextEmailAddress);
        userPassword = findViewById(R.id.editTextPass);

        // find Buttons
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.create_account).setOnClickListener(this);

        // initialize Firebase
        mAuth = FirebaseAuth.getInstance();
    }

    // check user when login activity starts
    @Override
    public void onStart() {
        super.onStart();
        // check if user is signed in and update UI if they are
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /**
     *  Sign into account with email and password
     *  @param email the email address linked to account
     *  @param password the password linked to account
     */
    private void signIn(String email, String password) {

        // check if user input is valid
        if(!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Signed In!", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Email or password was incorrect.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     *  Sign out from account
     */
    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    /**
     * Validate that user input is in correct format
     * @return if the user correctly input fields
     */
    private boolean validateForm() {
        boolean valid = true;

        String existingEmail = userEmail.getText().toString();
        if(TextUtils.isEmpty(existingEmail)) {
            userEmail.setError("Email Required");
            valid = false;
        }
        else {
            userEmail.setError(null);
        }

        String existingPassword = userPassword.getText().toString();
        if(TextUtils.isEmpty(existingPassword)) {
            userPassword.setError("Password Required");
            valid = false;
        }
        else {
            userPassword.setError(null);
        }

        return valid;
    }

    /**
     * Update UI depending on the user
     * @param user the Firebase user creating account or signing in
     */
    private void updateUI(FirebaseUser user) {
        if(user != null) {
            startActivity(new Intent(LoginActivity.this, ToDoActivity.class));
        }
        else {
            return;
        }
    }

    @Override
    public void onClick(View view) {
        int identifier = view.getId();
        if(identifier == R.id.create_account) {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        }
        else if(identifier == R.id.login) {
            if(!TextUtils.isEmpty(userEmail.getText().toString()) && !TextUtils.isEmpty(userPassword.getText().toString())) {
                signIn(userEmail.getText().toString(), userPassword.getText().toString());
            }
        }
    }
}

