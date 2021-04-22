package com.example.a41p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {

    TextView lastWorkout;
    ImageButton startButton;
    ImageButton pauseButton;
    ImageButton stopButton;
    TextView enterWorkout;
    TextView enterWorkoutField;
    Chronometer timer;
    long time;
    SharedPreferences sharedPreferences;
    String lastWorkoutType;
    long lastWorkoutTime;
    String currentWorkout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("savedData", MODE_PRIVATE);

        lastWorkout = findViewById(R.id.lastWorkout);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        enterWorkout = findViewById(R.id.enterWorkout);
        enterWorkoutField = findViewById(R.id.enterWorkoutField);
        timer = findViewById(R.id.timer);

        lastWorkoutTime = sharedPreferences.getLong("time" , 0);
        lastWorkoutType = sharedPreferences.getString("WorkoutType" , "");

         //converts milliseconds to seconds
         long lastWorkoutTimeSec = lastWorkoutTime/1000;
         //finds minutes
         int minutes = (int)lastWorkoutTimeSec / 60;
         //uses remainder to see how many seconds
         int seconds = (int)lastWorkoutTimeSec % 60;
         //formats the time into a presentable string
         String timeString = String.format("%02d:%02d",minutes,seconds);


        enterWorkout.setText("Enter your Workout Type: ");
        lastWorkout.setText("You spent " + timeString + " on " + lastWorkoutType + " last time");
        currentWorkout = enterWorkoutField.getText().toString();


        if(savedInstanceState != null)
        {
            timer.setBase(savedInstanceState.getLong("time"));
            timer.start();
        }
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.setBase(time + SystemClock.elapsedRealtime());
                timer.start();
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = timer.getBase() - SystemClock.elapsedRealtime();
                timer.stop();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = SystemClock.elapsedRealtime() - timer.getBase();

                currentWorkout = enterWorkoutField.getText().toString();
                sharedPreferences.edit().putString("WorkoutType", currentWorkout).apply();
                sharedPreferences.edit().putLong("time", time).apply();

                timer.setBase(SystemClock.elapsedRealtime());
                timer.stop();
                time = 0;
            }
        });

    }
    @Override
    public void onSaveInstanceState (Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Long TimerTime = timer.getBase();
        savedInstanceState.putLong("time", TimerTime);
    }
}