package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;
import java.util.concurrent.TimeUnit;
import android.media.MediaPlayer;

public class PomodoroActivity extends ChooseTimerActivity {
    private static final int MULTIPLIER = 60000;
    public static final long MILLI = 1000; // 1000 milliseconds in one second

    public long WORK;
    public long LONG_BREAK;
    public long SHORT_BREAK;

    private CountDownTimer countdown;
    private Button startButton;
    private Button pauseButton;
    private Button stopButton;

    private TextView mins;
    private TextView secs;
    private TextView workState;

    private long startTime;
    private int count;
    private long timeLeft;
    boolean isPaused;

    private MediaPlayer mediaPlayer;
    private String display;

    // drawer layout for navigation
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle abdt;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);

        // sets toolbar and nav
        setToolbar();
        myToolbar.setSubtitle(R.string.pomodoro);

        startButton = (Button) findViewById(R.id.start_countdown);
        pauseButton = (Button) findViewById(R.id.pause_countdown);
        stopButton = (Button) findViewById(R.id.stop_alarm);

        mins = (TextView)findViewById(R.id.minutes);
        secs = (TextView)findViewById(R.id.seconds);
        workState = (TextView)findViewById(R.id.work_state);

        count = 0;
        isPaused = false;

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);

        WORK = getIntent().getLongExtra("work_extra", 1500000); // 25 minutes default
        LONG_BREAK = getIntent().getLongExtra("long_extra", 1800000); // 30 minutes default
        SHORT_BREAK = getIntent().getLongExtra("short_extra", 300000); // 5 minutes default

        display = Long.toString(WORK/MULTIPLIER);
        mins.setText(display);
        secs.setText("00");
    }

    /**
     *  Set toolbar and navigation
     */
    public void setToolbar(){
        // sets toolbar
        myToolbar = findViewById(R.id.my_toolbar);
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
                    startActivity(new Intent(PomodoroActivity.this, PomodoroActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_change_pomodoro) {
                    startActivity(new Intent(PomodoroActivity.this, ChooseTimerActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_add_task) {
                    startActivity(new Intent(PomodoroActivity.this, AddActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_to_do) {
                    startActivity(new Intent(PomodoroActivity.this, ToDoActivity.class));
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
            case R.id.action_add_task:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(PomodoroActivity.this, AddActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startPomodoro(View view) {
        if(!isPaused) {
            // choose the start time of the timer
            if(count >= 0) {
                if(count % 2 == 1) {
                    startTime = SHORT_BREAK;
                }
                else {
                    workState.setText("Work Time");
                    startTime = WORK;
                }
            }
            else {
                startTime = LONG_BREAK;
            }

            count++;
            // repeat work and short breaks until 4 cycles are up
            if(count == 9) {
                count = -1;
            }
        }

        countdown = new CountDownTimer(startTime, MILLI) {
            @Override
            public void onTick(long millisUntilFinished) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
                isPaused = false;
                startButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                timeLeft = millisUntilFinished;
                mins.setText((TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) + "");
                secs.setText((TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + "");
            }

            @Override
            public void onFinish() {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
                isPaused = false;
                pauseButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                if(startTime == WORK) {
                    workState.setText("CONGRATULATIONS YOU MADE IT");
                }
                else {
                    workState.setText("Break Over");
                }
            }
        }.start();
    }

    public void pausePomodoro(View view) {
        // convert timeLeft in milliseconds to minutes and seconds
        mins.setText((TimeUnit.MILLISECONDS.toMinutes(timeLeft)) + "");
        secs.setText((TimeUnit.MILLISECONDS.toSeconds(timeLeft) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft))) + "");

        // pause timer
        countdown.cancel();
        pauseButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
        startTime = timeLeft;
        isPaused = true;
    }

    public void stopAlarm(View view) {
        stopButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
        mediaPlayer.stop();
        if(startTime == WORK) {
            if(count == -1) {
                workState.setText("Long Break Time");
                display = Long.toString(LONG_BREAK/MULTIPLIER);
                mins.setText(display);
                secs.setText("00");
            }
            else {
                workState.setText("Short Break Time");
                display = Long.toString(SHORT_BREAK/MULTIPLIER);
                mins.setText(display);
                secs.setText("00");
            }
        }
        else {
            workState.setText("Work Time");
            display = Long.toString(WORK/MULTIPLIER);
            mins.setText(display);
            secs.setText("00");
        }
    }

    @Override
    public void onBackPressed() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        else {
            if(countdown != null) {
                countdown.cancel();
            }
        }
        super.onBackPressed();
    }
}