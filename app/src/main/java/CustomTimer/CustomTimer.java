package com.itnstudios.drafthero;

import android.app.Activity;

/**
 * Created by moshe on 5/12/2016.
 */
public class CustomTimer extends Thread {

    public interface TimerListener{
        public void onNewLap(int lapNumber);
        public void onTick(long timeRemainingMillis);
    }

    private TimerListener listener;
    private Activity activity;

    private long timeRemainingMillis;
    private long lapLengthMillis;
    private int  lapNumber;
    private long previousTickMillis;
    private long tickLength;
    private long timePaused;
    private long stopTimeInFuture;

    private boolean clockRunning;
    private boolean clockAlreadyStarted;

    public CustomTimer(Activity activity) {
        super();
        this.activity = activity;
        lapLengthMillis = 0;
        lapNumber = 0;
        timeRemainingMillis = 0;
        previousTickMillis = 0;
        tickLength = 1000;
    }

    public void setListener(TimerListener listener){
        this.listener = listener;
    }

    public void setLapLengthMillis(long lapLengthMillis){
        this.lapLengthMillis = lapLengthMillis;
    }

    @Override
    public void run() {
        super.run();
        while(true) {
            while (clockAlreadyStarted) {
                if (clockRunning) {
                    long currentTime = System.currentTimeMillis();
                    timeRemainingMillis = stopTimeInFuture - currentTime;

                    if (currentTime - previousTickMillis >= tickLength) {
                        previousTickMillis = currentTime;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listener.onTick(timeRemainingMillis);
                            }
                        });
                        //Log.d("Tick happening", "" + timeRemainingMillis);
                    }
                    if (timeRemainingMillis < 0) {
                        lapNumber++;
                        stopTimeInFuture = (currentTime + lapLengthMillis);
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                listener.onNewLap(lapNumber);
                            }
                        });
                        //Log.d("New lap happening", ""+lapNumber);
                    }
                }
            }
            try {
                this.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setTickLength(long tickLength) {
        this.tickLength = tickLength;
    }

    public boolean isClockRunning(){
        return clockRunning;
    }

    public void stopClock() {
        clockAlreadyStarted = false;
        clockRunning = false;
    }
    public void pauseClock(){
        clockRunning = false;
        timePaused = System.currentTimeMillis();
    }

    public void startClock(){
        if(!clockAlreadyStarted) {
            previousTickMillis = System.currentTimeMillis();;
            lapNumber = 0;
            stopTimeInFuture = previousTickMillis + lapLengthMillis;
            clockRunning = true;
            clockAlreadyStarted = true;
            if(State.NEW == this.getState())
                super.start();
        }else{
            resumeClock();
        }
    }

    // Offset the time lap is completed by the amount of time clock was paused
    public  void resumeClock(){
        clockRunning = true;
        stopTimeInFuture += System.currentTimeMillis() - timePaused;
    }


}
