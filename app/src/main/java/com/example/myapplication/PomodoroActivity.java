package com.example.myapplication;

import android.os.Bundle;

import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
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