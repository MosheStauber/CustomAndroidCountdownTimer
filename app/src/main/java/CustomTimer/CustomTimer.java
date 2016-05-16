package CustomTimer;

import android.app.Activity;

/**
 * Created by moshe on 5/12/2016.
 */
public class CustomTimer extends Thread {

    public interface TimerListener{
        public void newLap(int lapNumber);
        public void onTick(long timeRemainingMillis);
    }

    private TimerListener listener;
    private Activity activity;

    private long timerStartMillis;
    private long timeRemainingMillis;
    private long lapLengthMillis;
    private int  lapNumber;
    private long previousTickMillis;
    private long tickLength;
    private long timePaused;

    private boolean clockRunning;
    private boolean clockBegan;

    public CustomTimer(Activity activity) {
        super();
        this.activity = activity;
        lapLengthMillis = 0;
        timerStartMillis = 0;
        lapNumber = 0;
        timeRemainingMillis = 0;
        previousTickMillis = 0;
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
            while (clockBegan) {
                if (clockRunning) {
                    long currentTime = System.currentTimeMillis();
                    timeRemainingMillis = (timerStartMillis + lapLengthMillis * (lapNumber + 1)) - currentTime;

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
                        timeRemainingMillis = System.currentTimeMillis();
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                listener.newLap(lapNumber);
                            }
                        });
                        //Log.d("New lap happening", ""+lapNumber);
                    }
                }
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
        clockBegan = false;
        clockRunning = false;
    }
    public void pauseClock(){
        clockRunning = false;
        timePaused = System.currentTimeMillis();
    }

    public void startClock(){
        if(!clockBegan) {
            timerStartMillis = System.currentTimeMillis();
            previousTickMillis = timerStartMillis;
            lapNumber = 0;
            clockRunning = true;
            clockBegan = true;
            if(State.NEW == this.getState())
                super.start();
        }else{
            resumeClock();
        }
    }

    public  void resumeClock(){
        clockRunning = true;
        timerStartMillis += (System.currentTimeMillis() - timePaused);
    }

}
