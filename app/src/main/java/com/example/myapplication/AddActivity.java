package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends ToDoActivity {
    // tag for debugging
    private static final String TAG = "AddActivity";
    // current date
    private static final Calendar c = Calendar.getInstance();
    // button views to get due date and time
    private static Button dateEdit;
    private static Button timeEdit;

    // values of current date
    private static int hour;
    private static int minute;
    private static int year;
    private static int month;
    private static int day;

    // user's due date
    private static Calendar dueDate;
    private String strDate;

    /**
     * Method for when add task page loads
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // sets layout page
        setContentView(R.layout.activity_add);

        // sets toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setSubtitle(R.string.add_task_event);

        // instantiates buttons by id
        dateEdit = findViewById(R.id.button_date);
        timeEdit = findViewById(R.id.button_time);

        // instantiates values of current date
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Set time on button to current time
        String ampm = "";
        if (hour / 12 == 1) {
            ampm = "PM";
            hour = hour % 12;
        }
        else
            ampm = "AM";
        String time = String.format(Locale.US, "%d:%02d %s", hour, minute, ampm);
        timeEdit.setText(time);

        // Set date on button to current date
        String date = (month + 1) + "/" + day + "/" + year;
        dateEdit.setText(date);

        // instantiates due date as current time for now
        dueDate = Calendar.getInstance();

        // call database read listener
        attachDatabaseReadListener();
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
        final EditText classNameField = (EditText) findViewById(R.id.text_edit_class_name);
        String className = classNameField.getText().toString();

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
        d.plusHours(hours);

        // get the due date in string format
        SimpleDateFormat sdformat = new SimpleDateFormat("EEE MMMM dd, yyyy h:mm a", Locale.US);
        strDate = sdformat.format(dueDate.getTime());

        // creates new task with all of user's info
        Task t = new Task(name, strDate, className, notes, d, priority);

        // push task to database
        mTasksDatabaseRef.push().setValue(t);
        // empty text fields
        nameField.setText("");
        classNameField.setText("");
        notesField.setText("");
        hourField.setText("");
        minuteField.setText("");

        Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show();
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
            return new TimePickerDialog(getActivity(), this, hour, minute,
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
            return new DatePickerDialog(getActivity(), this, year, month, day);
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

    protected void attachDatabaseReadListener() {
        if(mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        }
    }

}
