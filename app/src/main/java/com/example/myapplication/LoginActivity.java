package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // user input variables
    private EditText userEmail;
    private EditText userPassword;

    // Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseRef;

    // Google sign in
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private static boolean firstSignIn;

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
        findViewById(R.id.google_sign_in).setOnClickListener(this);

        // configure Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseRef = mFirebaseDatabase.getReference().child("users");
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

    // check if google sign in worked
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }

    // link Firebase with Google sign in
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if(firstSignIn) {
                                // create user info
                                User newUser = new User(acct.getDisplayName(), acct.getEmail());
                                //push user to database
                                mUsersDatabaseRef.child(user.getUid()).setValue(newUser);
                                firstSignIn = false;
                            }

                            Toast.makeText(LoginActivity.this, "Signed In!", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    // google sign in
    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View view) {
        int identifier = view.getId();
        if(identifier == R.id.create_account) {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        }
        else if(identifier == R.id.login) {
            signIn(userEmail.getText().toString(), userPassword.getText().toString());
        }
        else if(identifier == R.id.google_sign_in) {
            signInGoogle();
        }
    }
}

