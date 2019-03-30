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

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import android.media.MediaPlayer;

/**
 * Activity for Pomodoro Timer
 */
public class PomodoroActivity extends ChooseTimerActivity {
    // Defaults
    private static final int MULTIPLIER = 60000;
    public static final long MILLI = 1000; // 1000 milliseconds in one second

    // time for each type of interval
    public long WORK;
    public long LONG_BREAK;
    public long SHORT_BREAK;

    // buttons and timer
    private CountDownTimer countdown;
    private Button startButton;
    private Button pauseButton;
    private Button stopButton;

    // timer's text views and title
    private TextView mins;
    private TextView secs;
    private TextView workState;

    private long startTime;
    private int count;
    private long timeLeft;
    boolean isPaused;

    // alarm player
    private MediaPlayer mediaPlayer;

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

        // sets buttons
        startButton = (Button) findViewById(R.id.start_countdown);
        pauseButton = (Button) findViewById(R.id.pause_countdown);
        stopButton = (Button) findViewById(R.id.stop_alarm);

        // sets textviews
        mins = (TextView)findViewById(R.id.minutes);
        secs = (TextView)findViewById(R.id.seconds);
        workState = (TextView)findViewById(R.id.work_state);

        count = 0;
        isPaused = false;

        // sets times for each interval
        WORK = getIntent().getLongExtra("work_extra", 1500000); // 25 minutes default
        LONG_BREAK = getIntent().getLongExtra("long_extra", 1800000); // 30 minutes default
        SHORT_BREAK = getIntent().getLongExtra("short_extra", 300000); // 5 minutes default

        // sets timer for the first time
        mins.setText(String.format(Locale.getDefault(),"%02d", WORK / MULTIPLIER));
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
                } else if (menuItem.getItemId() == R.id.action_settings) {
                    startActivity(new Intent(PomodoroActivity.this, SettingsActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_calendar) {
                    startActivity(new Intent(PomodoroActivity.this, CalendarActivity.class));
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

    /**
     * sets pomodoro timer
     * @param view start button
     */
    public void startPomodoro(View view) {
        // if not paused, reset the time to the correct interval
        if(!isPaused) {
            // choose the start time of the timer
            if(count >= 0) {
                if(count % 2 == 1)
                    startTime = SHORT_BREAK;
                else {
                    workState.setText("Work Time");
                    startTime = WORK;
                }
            }
            else
                startTime = LONG_BREAK;

            count++;
            // repeat work and short breaks until 4th break (long break)
            if(count == 7)
                count = -1;
        }

        // sets alarm
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);

        countdown = new CountDownTimer(startTime, MILLI) {
            @Override
            public void onTick(long millisUntilFinished) {
                isPaused = false;
                startButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                timeLeft = millisUntilFinished;
                mins.setText(String.format(Locale.getDefault(), "%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                secs.setText(String.format(Locale.getDefault(), "%02d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
                isPaused = false;
                pauseButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                if(Math.abs(count) % 2 == 1) {
                    workState.setText("CONGRATULATIONS YOU MADE IT");
                }
                else {
                    workState.setText("Break Over");
                }
            }
        }.start();
    }

    /**
     * pauses pomodoro
     * @param view pause button
     */
    public void pausePomodoro(View view) {
        // convert timeLeft in milliseconds to minutes and seconds
        mins.setText(String.format(Locale.getDefault(), "%02d", TimeUnit.MILLISECONDS.toMinutes(timeLeft)));
        secs.setText(String.format(Locale.getDefault(), "%02d", TimeUnit.MILLISECONDS.toSeconds(timeLeft) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft))));

        // pause timer
        countdown.cancel();
        pauseButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
        startTime = timeLeft;
        isPaused = true;
    }

    /**
     * Stops alarm
     * @param view stop button
     */
    public void stopAlarm(View view) {
        stopButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
        // stops alarm
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // sets title for next section
        if(count == -1) {
            workState.setText("Long Break Time");
            mins.setText(String.format(Locale.getDefault(),"%02d", LONG_BREAK / MULTIPLIER));
            secs.setText("00");
        }
        else if (count % 2 == 1){
            workState.setText("Short Break Time");
            mins.setText(String.format(Locale.getDefault(),"%02d", SHORT_BREAK / MULTIPLIER));
            secs.setText("00");
        }
        else {
            workState.setText("Work Time");
            mins.setText(String.format(Locale.getDefault(),"%02d", WORK / MULTIPLIER));
            secs.setText("00");
        }
    }

    /**
     * stops timer and alarm if back button is pressed
     */
    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        } else {
            if(countdown != null) {
                countdown.cancel();
            }
        }
        super.onBackPressed();
    }


}