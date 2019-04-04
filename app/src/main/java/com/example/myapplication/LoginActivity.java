package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // constants
    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseRef;
    private FirebaseUser user;

    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserName = ANONYMOUS;

        // initialize firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseRef = mFirebaseDatabase.getReference().child("users");

        // Firebase sign in/sign out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                // user is signed in
                if(user != null) {
                    onSignedInInitialize(user.getDisplayName());
                }
                // user is signed out
                else {
                    onSignedOutCleanUp();
                    // choose authentication providers
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build()
                    );

                    // Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN
                    );
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            // successfully signed in
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                user = FirebaseAuth.getInstance().getCurrentUser();
                User newUser = new User(user.getDisplayName(), user.getEmail());
                //push user to database
                mUsersDatabaseRef.child(user.getUid()).setValue(newUser);
                finish();
                startActivity(new Intent(LoginActivity.this, ToDoActivity.class));
            }
            // sign in failed
            else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void onSignedInInitialize(String username) {
        mUserName = username;
    }

    private void onSignedOutCleanUp() {
        AuthUI.getInstance().signOut(this);
        mUserName = ANONYMOUS;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // sign out button is clicked
    public void signOut(View view) {
        onSignedOutCleanUp();
    }

    // start Pomodoro activity
    public void startTimer(View view) {
        startActivity(new Intent(getApplicationContext(), ChooseTimerActivity.class));
    }

    // start to-do list
    public void startToDo(View view) {
        startActivity(new Intent(getApplicationContext(), ToDoActivity.class));
    }
}

