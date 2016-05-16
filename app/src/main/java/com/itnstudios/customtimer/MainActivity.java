package com.itnstudios.customtimer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import CustomTimer.CustomTimer;

public class MainActivity extends AppCompatActivity {

    CustomTimer timer;
    TextView tvClock;
    Button startButton, pauseButton, resumeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.btn_start);
        pauseButton = (Button) findViewById(R.id.btn_pause);
        resumeButton = (Button) findViewById(R.id.btn_resume);

        tvClock = (TextView) findViewById(R.id.tv_clock);
        updateClock(5000);

        timer = new CustomTimer(this);
        timer.setLapLengthMillis(5000);
        timer.setTickLength(500);
        timer.setListener(new CustomTimer.TimerListener() {
            @Override
            public void newLap(int lapNumber) {
                Toast.makeText(getApplicationContext(), "Lap Number: " + lapNumber, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTick(long timeRemainingMillis) {
                updateClock(timeRemainingMillis);
                Log.d("Tick Happening", timeRemainingMillis+"");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateClock(long timeInMillis) {
        int min = (int) (timeInMillis / 60000);
        int sec = (int) ((timeInMillis % 60000) / 1000);
        tvClock.setText(String.format("%02d:%02d", min, sec));
    }

    public void startClock(View v){
        timer.startClock();
        v.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    public void pauseClock(View v){
        timer.pauseClock();
        v.setVisibility(View.GONE);
        resumeButton.setVisibility(View.VISIBLE);
    }

    public void resumeClock(View v){
        timer.resumeClock();
        v.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    public void stopClock(View v){
        timer.stopClock();
        resumeButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
    }
}
