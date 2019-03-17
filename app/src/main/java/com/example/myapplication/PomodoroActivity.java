package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;
import java.util.concurrent.TimeUnit;
import android.media.MediaPlayer;

public class PomodoroActivity extends AppCompatActivity {

    public static final long MILLI = 1000; // 1000 milliseconds in one second
    public static final long WORK = 15000; // 25 minutes
    public static final long LONG_BREAK = 18000; // 30 minutes
    public static final long SHORT_BREAK = 3000; // 5 minutes

    CountDownTimer countdown;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);

        startButton = (Button) findViewById(R.id.start_countdown);
        pauseButton = (Button) findViewById(R.id.pause_countdown);
        stopButton = (Button) findViewById(R.id.stop_alarm);

        mins = (TextView)findViewById(R.id.minutes);
        secs = (TextView)findViewById(R.id.seconds);
        workState = (TextView)findViewById(R.id.work_state);

        count = 0;
        isPaused = false;

        mins.setText("25");
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
                workState.setText("CONGRATULATIONS YOU MADE IT");
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
                mins.setText("30");
                secs.setText("00");
            }
            else {
                workState.setText("Short Break Time");
                mins.setText("5");
                secs.setText("00");
            }
        }
        else {
            workState.setText("Work Time");
            mins.setText("25");
            secs.setText("00");
        }
    }

    @Override
    public void onBackPressed() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        else {
            countdown.cancel();
        }
        //countdown.cancel();
        super.onBackPressed();
    }
}
