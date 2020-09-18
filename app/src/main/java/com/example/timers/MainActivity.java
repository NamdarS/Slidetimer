package com.example.timers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import me.philio.pinentry.PinEntryView;

public class MainActivity extends AppCompatActivity {

    TextView time;
    TextView colons;
    PinEntryView userInput;
    int timeInput;
    int timeSelected;
    boolean goShowing = true;
    boolean timeShowing = true;
    CountDownTimer timer;
    SeekBar seekBar;
    SeekBar init;
    long defaultTime = 0;
    Button button;
    Button button2;
    MediaPlayer finishSound;

    public void setTime(View view) {
        String input = userInput.getText().toString();

        if (input.matches("") || input.matches("000000")) {
            if (timeSelected == 0) {
                timeInput = 900;
            } else {
                timeInput = timeSelected;
            }
        } else {
            timeInput = Integer.parseInt(reverseFormat(input));
        }

        button.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
        button2.setVisibility(View.INVISIBLE);
        userInput.setVisibility(View.INVISIBLE);
        colons.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        init.setVisibility(View.INVISIBLE);
        setupSeekBar();
        startTimerButton(button);
        setupSeekBar();

    }

    public void setupSeekBar() {
        seekBar.setMax(timeInput);
        seekBar.setProgress(timeInput);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                timeSelected = i;
                if (timeShowing) {
                    time.setText(timeFormatted((long) i * 1000));
                    timeShowing = false;
                } else {
                    userInput.setText(timeFormatted((long) i * 1000).replaceAll(":",""));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void initSeekbar() {
        init = findViewById(R.id.initSeekbar);
        init.setMax(900);

        init.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                userInput.setText(timeFormatted((long) i * 1000).replaceAll(":",""));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void startTimerButton(View view) {
        finishSound = MediaPlayer.create(this, R.raw.bass);
        if (goShowing) {
            button.setText("STOP");
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff5148")));
            if (String.valueOf(timeSelected).matches("0")) {

                timeSelected = timeInput;
            }
            int timeLimit = timeSelected * 1000;

            timer = new CountDownTimer(timeLimit, 1000) {

                public void onTick(long millisUntilDone) {
                    time.setText(timeFormatted(millisUntilDone));
                    seekBar.setProgress((int) millisUntilDone/ 1000);
                }
                public void onFinish() {
                    stopFinish();
                    finishSound.start();
                }
            }.start();

            goShowing = false;
            seekBar.setEnabled(false);

        } else {
            stopFinish();
            time.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.VISIBLE);
            userInput.setVisibility(View.VISIBLE);
            colons.setVisibility(View.VISIBLE);
            button.setVisibility(View.INVISIBLE);
        }

    }

    public void stopFinish() {
        button.setText("GO");
        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1DB954")));
        time.setText(timeFormatted((long) defaultTime * 1000));
        seekBar.setProgress(timeSelected);
        timer.cancel();
        goShowing = true;
        seekBar.setEnabled(true);
    }

    public String timeFormatted(long millis) {

        return String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public String reverseFormat(String string) {
        String time = string.replaceAll(":","");
        int hours = Integer.parseInt(time.substring(0,2));
        int minutes = Integer.parseInt(time.substring(2,4));
        int seconds = Integer.parseInt(time.substring(4,6));
        int totalSeconds = (hours * 3600) + (minutes * 60) + seconds;
        return String.valueOf(totalSeconds);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.startButton);
        button2 = findViewById(R.id.setButton);
        userInput = findViewById(R.id.timeEntry);
        time = findViewById(R.id.textViewTime);
        colons = findViewById(R.id.textViewColons);
        seekBar = findViewById(R.id.seekBar);
        userInput.setText("000000");
        initSeekbar();

    }
}