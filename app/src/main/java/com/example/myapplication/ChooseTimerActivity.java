package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_timer);

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
}
