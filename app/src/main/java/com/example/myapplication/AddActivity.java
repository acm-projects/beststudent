package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    // tag for debugging
    private static final String TAG = "AddActivity";

    // button views to get due date and time
    private static Button dateEdit;
    private static Button timeEdit;

    // user's due date
    private static Calendar dueDate;
    private String strDate;
    private String time = "";

    // drawer layout for navigation
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle abdt;
    private Toolbar myToolbar;

    // Firebase variables
    private DatabaseReference mTasksDatabaseRef;
    private DatabaseReference mClassesDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;

    // dynamic class spinner
    private Spinner classSpinner;
    private List<String> classStrs;

    /**
     * Method for when add task page loads
     * @param savedInstanceState saves dynamic user data in case activity is put in the background
     *                           or orientation changes or something
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Add activity started.");

        super.onCreate(savedInstanceState);
        // sets layout page
        setContentView(R.layout.activity_add_task);

        // sets tool bar
        setToolbar();

        // instantiates buttons by id
        dateEdit = findViewById(R.id.button_date);
        timeEdit = findViewById(R.id.button_time);

        // instantiates due date as current time for now
        dueDate = Calendar.getInstance();

        // get the formatted time
        SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm a", Locale.US);
        String time = sdfTime.format(dueDate.getTime());
        timeEdit.setText(time);

        // Set date on button to current date
        String date = (dueDate.get(Calendar.MONTH) + 1) + "/" + dueDate.get(Calendar.DATE) + "/"
                + dueDate.get(Calendar.YEAR);
        dateEdit.setText(date);

        // call to add class items to class spinner
        addItemsToClassSpinner();
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
                    startActivity(new Intent(AddActivity.this, PomodoroActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_change_pomodoro) {
                    startActivity(new Intent(AddActivity.this, ChooseTimerActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_add_task) {
                    startActivity(new Intent(AddActivity.this, AddActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_to_do) {
                    startActivity(new Intent(AddActivity.this, ToDoActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_settings) {
                    startActivity(new Intent(AddActivity.this, SettingsActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_calendar) {
                    startActivity(new Intent(AddActivity.this, CalendarActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.sign_out) {
                    startActivity(new Intent(AddActivity.this, LogoutActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_classes) {
                    startActivity(new Intent(AddActivity.this, ClassesActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_notes) {
                    startActivity(new Intent(AddActivity.this, NotesActivity.class));
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

    // dynamically add class items
    public void addItemsToClassSpinner() {
        classSpinner = findViewById(R.id.class_spinner);

        // initialize database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mClassesDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("classes");
        mClassesDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // prevent multiple instances of same data
                classStrs = new ArrayList<>();
                // get all the data in database
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    SchoolClass tempClass = data.getValue(SchoolClass.class);
                    classStrs.add(tempClass.getClassName());
                }
                classStrs.add("No Class");
                // specify an adapter
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(AddActivity.this,
                        R.layout.support_simple_spinner_dropdown_item, classStrs);
                dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                classSpinner.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Adds a task after user is done filling out form
     * @param view
     */
    public void addTask(View view) {
        // gets name of task
        final EditText nameField = (EditText) findViewById(R.id.text_edit_name);
        String name = nameField.getText().toString();

        // gets class of task
        String className = "No Class";
        if(!classSpinner.getSelectedItem().toString().isEmpty()) {
            className = classSpinner.getSelectedItem().toString();
        }

        // get notes
        final EditText notesField = (EditText) findViewById(R.id.text_edit_notes);
        String notes = notesField.getText().toString();

        // gets priority level from spinner, defaults to 1
        final Spinner prioritySpinner = (Spinner) findViewById(R.id.spinner_priority);
        int priority = 1;
        if (!prioritySpinner.getSelectedItem().toString().isEmpty())
                priority = Integer.parseInt(prioritySpinner.getSelectedItem().toString());

        // gets hour duration
        final EditText hourField = (EditText) findViewById(R.id.text_edit_duration_hour);
        String durHour = hourField.getText().toString();
        int hours = 0;
        if (!durHour.isEmpty())
            hours = Integer.parseInt(durHour);

        // gets minute duration
        final EditText minuteField = (EditText) findViewById(R.id.text_edit_duration_minute);
        String durMinute = minuteField.getText().toString();
        int minutes = 0;
        if (!durMinute.isEmpty())
            minutes = Integer.parseInt(durMinute);

        // sets the duration of task
        Duration d = Duration.ofMinutes(minutes);
        d = d.plusHours(hours);
        int s = (int) d.getSeconds();
        if ((s / 86400) != 0)
            time += (s / 86400) + "d ";
        if ((s % 86400) / 3600 != 0)
            time += ((s % 86400) / 3600) + "h ";
        if (((s % 86400) % 3600) / 60 != 0)
            time += (((s % 86400) % 3600) / 60) + "m ";
        // defaults to 0 minutes if empty
        if (time.isEmpty())
            time += "0m ";

        // get the due date in string format
        SimpleDateFormat sdformat = new SimpleDateFormat("EEE MMMM dd, yyyy h:mm a", Locale.US);
        strDate = sdformat.format(dueDate.getTime());


        // initialize database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTasksDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("tasks");
        // push task to database
        String key = mTasksDatabaseRef.push().getKey();
        // creates new task with all of user's info
        Task t = new Task(name, strDate, className, notes, time, priority, key);
        mTasksDatabaseRef.child(key).setValue(t);

        // empty text fields
        nameField.setText("");
        notesField.setText("");
        hourField.setText("");
        minuteField.setText("");

        Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show();

        // debug check
        Log.d(TAG, "task: made.");

        // exits activity when done
        finish();
    }

    /**
     * shows time picker when button is clicked
     * @param v
     */
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * shows date picker when button is clicked
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * creates a time picker, defaults to current time, changes button text to selected time,
     * updates due time to selected time
     */
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, dueDate.get(Calendar.HOUR_OF_DAY), dueDate.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(getActivity()));
        }

        // updates button text and due time
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            dueDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dueDate.set(Calendar.MINUTE, minute);
            // get the formatted time
            SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm a", Locale.US);
            String time = sdfTime.format(dueDate.getTime());
            timeEdit.setText(time);
        }
    }

    /**
     * creates a date picker, defaults to current date, changes button text to selected date,
     * updates due date to selected date
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH),
                    dueDate.get(Calendar.DATE));
        }

        // changes due date and button text to selected values
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            dueDate.set(Calendar.YEAR, year);
            dueDate.set(Calendar.MONTH, month);
            dueDate.set(Calendar.DATE, day);
            String date = (month + 1) + "/" + day + "/" + year;
            dateEdit.setText(date);
        }
    }
}
