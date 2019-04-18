package com.example.myapplication;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNoteActivity extends AppCompatActivity {

    // user input variables
    private EditText noteTitleField;
    private EditText notesField;

    // Firebase variables
    private DatabaseReference mClassesDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;

    // drawer layout for navicgation
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle abdt;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // sets tool bar
        setToolbar();

        noteTitleField = findViewById(R.id.note_name_input);
        notesField = findViewById(R.id.notes_input);

        // initialize database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mClassesDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("classes");
    }

    /**
     *  Set toolbar and navigation
     */
    public void setToolbar(){
        // sets toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        abdt = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(abdt);
        abdt.syncState();
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.action_pomodoro){
                    startActivity(new Intent(AddNoteActivity.this, PomodoroActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_change_pomodoro) {
                    startActivity(new Intent(AddNoteActivity.this, ChooseTimerActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_add_task) {
                    startActivity(new Intent(AddNoteActivity.this, AddActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_to_do) {
                    startActivity(new Intent(AddNoteActivity.this, ToDoActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_settings) {
                    startActivity(new Intent(AddNoteActivity.this, SettingsActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_calendar) {
                    startActivity(new Intent(AddNoteActivity.this, CalendarActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.sign_out) {
                    startActivity(new Intent(AddNoteActivity.this, LogoutActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_classes) {
                    startActivity(new Intent(AddNoteActivity.this, ClassesActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_notes) {
                    startActivity(new Intent(AddNoteActivity.this, NotesActivity.class));
                    return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // when button to add class is pushed
    public void addNote(View view) {
        // check if user input is valid
        if(!validateForm()) {
            return;
        }

        // get user input
        String noteTitle = noteTitleField.getText().toString();
        String notes = notesField.getText().toString();

        // create a class
        Note newClass = new Note(noteTitle, notes);
//
//        // push new class to database
//        mClassesDatabaseRef.child(noteTitle).setValue(newClass);

        // empty text fields
        noteTitleField.setText("");
        notesField.setText("");

        // notify the user the note was successfully added
        Toast.makeText(this, "Note Added!", Toast.LENGTH_SHORT).show();

        // finished adding the class
        finish();
    }

    /**
     * Validate that user input is in correct format
     * @return if the user correctly input fields
     */
    private boolean validateForm() {
        boolean valid = true;

        String notes = notesField.getText().toString();
        if(TextUtils.isEmpty(notes)) {
            notesField.setError("Notes Required");
            valid = false;
        }
        else {
            notesField.setError(null);
        }
        return valid;
    }
}
