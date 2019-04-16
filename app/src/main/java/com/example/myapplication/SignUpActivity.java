package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    // progress display
    private ProgressBar loading;

    // user input variables
    private EditText emailField;
    private EditText passwordField;
    private EditText usernameField;

    // Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // get text input from new user
        emailField = findViewById(R.id.email_set_up);
        passwordField = findViewById(R.id.password_set_up);
        usernameField = findViewById(R.id.username_set_up);

        // get progress bar
        loading = findViewById(R.id.progress);

        // initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseRef = mFirebaseDatabase.getReference().child("users");
    }

    /**
     *  Create account with email and password
     *  @param email the email address linked to account
     *  @param password the password linked to account
     */
    private void createAccount(final String email, final String userName, String password) {

        // check if user input is valid
        if(!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // create user info
                            User newUser = new User(userName, email);
                            //push user to database
                            mUsersDatabaseRef.child(user.getUid()).setValue(newUser);
                            // account created
                            loading.setVisibility(View.GONE);
                            Toast.makeText(SignUpActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "Email already taken.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            loading.setVisibility(View.GONE);
                            emailField.setVisibility(View.VISIBLE);
                            usernameField.setVisibility(View.VISIBLE);
                            passwordField.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    /**
     * Validate that user input is in correct format
     * @return if the user correctly input fields
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if(TextUtils.isEmpty(email)) {
            emailField.setError("Email Required");
            valid = false;
        }
        else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if(TextUtils.isEmpty(password)) {
            passwordField.setError("Password Required");
            valid = false;
        }
        else {
            passwordField.setError(null);
        }

        String username = usernameField.getText().toString();
        if(TextUtils.isEmpty(username)) {
            usernameField.setError("Username Required");
            valid = false;
        }
        else {
            usernameField.setError(null);
        }

        return valid;
    }

    /**
     * Update UI depending on the user
     * @param user the Firebase user creating account or signing in
     */
    private void updateUI(FirebaseUser user) {
        if(user != null) {
            startActivity(new Intent(SignUpActivity.this, ToDoActivity.class));
        }
        else {
            return;
        }
    }

    /**
     * Successful new user sign up when the sign up button on sign up page is clicked
     * @param view the button was clicked
     */
    public void callCreateAccount(View view) {
        loading.setVisibility(View.VISIBLE);
        emailField.setVisibility(View.GONE);
        usernameField.setVisibility(View.GONE);
        passwordField.setVisibility(View.GONE);
        // create account
        createAccount(emailField.getText().toString(), usernameField.getText().toString(), passwordField.getText().toString());
    }
}
