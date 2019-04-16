package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

public class ChooseTimerActivity extends AppCompatActivity {
    private static final int MAX_MINS = 60;
    private static final int MULTIPLIER = 60000;
    private static final int DEFAULT_WORK = 25;
    private static final int DEFAULT_SHORT = 5;
    private static final int DEFAULT_LONG = 30;

    private NumberPicker workpicker;
    private NumberPicker shortpicker;
    private NumberPicker longpicker;

    public long workMinsInMilli = 1500000; // 25 minutes
    public long longMinsInMilli = 1800000; // 30 minutes
    public long shortMinsInMilli = 300000; // 5 minutes

    // drawer layout for navigation
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle abdt;
    private Toolbar myToolbar;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_timer);

        // sets toolbar and nav
        myToolbar = findViewById(R.id.my_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.navigation);
        setToolbar();

        workpicker = (NumberPicker) findViewById(R.id.work_picker);
        workpicker.setMinValue(0);
        workpicker.setMaxValue(MAX_MINS);
        workpicker.setWrapSelectorWheel(true);
        workpicker.setValue(DEFAULT_WORK);

        shortpicker = (NumberPicker) findViewById(R.id.short_picker);
        shortpicker.setMinValue(0);
        shortpicker.setMaxValue(MAX_MINS);
        shortpicker.setWrapSelectorWheel(true);
        shortpicker.setValue(DEFAULT_SHORT);

        longpicker = (NumberPicker) findViewById(R.id.long_picker);
        longpicker.setMinValue(0);
        longpicker.setMaxValue(MAX_MINS);
        longpicker.setWrapSelectorWheel(true);
        longpicker.setValue(DEFAULT_LONG);

        workpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                workMinsInMilli = newVal * MULTIPLIER;
            }
        });

        shortpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                shortMinsInMilli = newVal * MULTIPLIER;
            }
        });

        longpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                longMinsInMilli = newVal * MULTIPLIER;
            }
        });
    }

    public void goToPomodoro(View view) {
        Intent intent = new Intent(getApplicationContext(), PomodoroActivity.class);
        intent.putExtra("work_extra", workMinsInMilli);
        intent.putExtra("long_extra", longMinsInMilli);
        intent.putExtra("short_extra", shortMinsInMilli);
        startActivity(intent);
    }

    /**
     *  Set toolbar and navigation
     */
    public void setToolbar(){
        // sets toolbar
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        abdt = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(abdt);
        abdt.syncState();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.action_pomodoro){
                    startActivity(new Intent(ChooseTimerActivity.this, PomodoroActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_change_pomodoro) {
                    startActivity(new Intent(ChooseTimerActivity.this, ChooseTimerActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_add_task) {
                    startActivity(new Intent(ChooseTimerActivity.this, AddActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_to_do) {
                    startActivity(new Intent(ChooseTimerActivity.this, ToDoActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_settings) {
                    startActivity(new Intent(ChooseTimerActivity.this, SettingsActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_calendar) {
                    startActivity(new Intent(ChooseTimerActivity.this, CalendarActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.sign_out) {
                    startActivity(new Intent(ChooseTimerActivity.this, LogoutActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_classes) {
                    startActivity(new Intent(ChooseTimerActivity.this, ClassesActivity.class));
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
            case R.id.action_add_task:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(ChooseTimerActivity.this, AddActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
