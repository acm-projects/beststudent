package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
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
        String ampm;
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

    }

    /**
     * Adds a task after user is done filling out form
     * @param button add task button
     */
    public void addTask(View button) {
        // gets name of task
        final EditText nameField = findViewById(R.id.text_edit_name);
        String name = nameField.getText().toString();

        // gets class of task
        final EditText classNameField = findViewById(R.id.text_edit_class_name);
        String className = classNameField.getText().toString();

        // get notes
        final EditText notesField = findViewById(R.id.text_edit_notes);
        String notes = notesField.getText().toString();

        // gets priority level from spinner, defaults to 1
        final Spinner prioritySpinner = findViewById(R.id.spinner_priority);
        int priority = 1;
        if (!prioritySpinner.getSelectedItem().toString().isEmpty())
                priority = Integer.parseInt(prioritySpinner.getSelectedItem().toString());

        // gets hour duration
        final EditText hourField = findViewById(R.id.text_edit_duration_hour);
        String durHour = hourField.getText().toString();
        int hours = 0;
        if (!durHour.isEmpty())
            hours = Integer.parseInt(durHour);

        // gets minute duration
        final EditText minuteField = findViewById(R.id.text_edit_duration_minute);
        String durMinute = minuteField.getText().toString();
        int minutes = 0;
        if (!durMinute.isEmpty())
            minutes = Integer.parseInt(durMinute);

        // sets the duration of task
        Duration d = Duration.ofMinutes(minutes);
        d.plusHours(hours);

        // creates new task with all of user's info
        Task t = new Task(name, dueDate, className, notes, d, priority);

        // debug check
        Log.d(TAG, "task: made.");

        // exits activity when done
        finish();

    }

    /**
     * shows time picker when button is clicked
     * @param v button view
     */
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * shows date picker when button is clicked
     * @param v button view
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
            String ampm;
            if (hourOfDay / 12 == 1) {
                ampm = "PM";
                hourOfDay = hourOfDay % 12;
            }
            else
                ampm = "AM";
            String time = String.format(Locale.US, "%d:%02d %s", hourOfDay, minute, ampm);
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

}
