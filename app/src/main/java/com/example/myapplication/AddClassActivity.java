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

public class AddClassActivity extends AppCompatActivity {

    // user input variables
    private EditText classNameField;
    private EditText classTimeField;
    private EditText roomField;
    private EditText professorField;
    private EditText officeHoursField;
    private EditText officeField;

    // Firebase variables
    private DatabaseReference mClassesDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;

    // drawer layout for navigation
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle abdt;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        // sets tool bar
        setToolbar();

        classNameField = findViewById(R.id.class_name_input);
        classTimeField = findViewById(R.id.class_time_input);
        roomField = findViewById(R.id.room_number_input);
        professorField = findViewById(R.id.professor_input);
        officeHoursField = findViewById(R.id.office_hours_input);
        officeField = findViewById(R.id.office_room_input);

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
                    startActivity(new Intent(AddClassActivity.this, PomodoroActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_change_pomodoro) {
                    startActivity(new Intent(AddClassActivity.this, ChooseTimerActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_add_task) {
                    startActivity(new Intent(AddClassActivity.this, AddActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_to_do) {
                    startActivity(new Intent(AddClassActivity.this, ToDoActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_settings) {
                    startActivity(new Intent(AddClassActivity.this, SettingsActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_calendar) {
                    startActivity(new Intent(AddClassActivity.this, CalendarActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.sign_out) {
                    startActivity(new Intent(AddClassActivity.this, LogoutActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_classes) {
                    startActivity(new Intent(AddClassActivity.this, ClassesActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_notes) {
                    startActivity(new Intent(AddClassActivity.this, NotesActivity.class));
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
    public void addClass(View view) {
        // check if user input is valid
        if(!validateForm()) {
            return;
        }

        // get user input
        String className = classNameField.getText().toString();
        String classTime = classTimeField.getText().toString();
        String room = roomField.getText().toString();
        String prof = professorField.getText().toString();
        String officeHours = officeHoursField.getText().toString();
        String office = officeField.getText().toString();

        // create a class
        SchoolClass newClass = new SchoolClass(className, room, classTime, prof, office, officeHours);

        // push new class to database
        mClassesDatabaseRef.child(className).setValue(newClass);

        // empty text fields
        classNameField.setText("");
        classTimeField.setText("");
        roomField.setText("");
        professorField.setText("");
        officeHoursField.setText("");
        officeField.setText("");

        // notify the user the class was successfully added
        Toast.makeText(this, "Class Added!", Toast.LENGTH_SHORT).show();

        // finished adding the class
        finish();
    }

    /**
     * Validate that user input is in correct format
     * @return if the user correctly input fields
     */
    private boolean validateForm() {
        boolean valid = true;

        String className = classNameField.getText().toString();
        if(TextUtils.isEmpty(className)) {
            classNameField.setError("Class Name Required");
            valid = false;
        }
        else {
            classNameField.setError(null);
        }

        String classTime = classTimeField.getText().toString();
        if(TextUtils.isEmpty(classTime)) {
            classTimeField.setError("Class Time Required");
            valid = false;
        }
        else {
            classTimeField.setError(null);
        }

        return valid;
    }
}
