package com.dylan.countdown;

import com.dylan.countdown.ui.Panel;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dylan于东海 on 2017/4/11.
 */
public class Clock {
    int hours, minutes, seconds;
    Timer timer;
    LocalDateTime target;

    public Clock(){
    }

    public Clock(String h, String m, String s){
        try {
            hours = new Integer(h.trim());
            minutes = new Integer(m.trim());
            seconds = new Integer(s.trim());
        } catch (NumberFormatException e){
            System.out.println("NumberFormatException");
        }
    }

    public void start(final Panel panel){
        timer = new Timer();
        LocalDateTime timeNow = LocalDateTime.now();
        target = timeNow.plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);

        timer.schedule(new TimerTask() {
            public void run() {
                if (target.compareTo(LocalDateTime.now()) <= 0){
                    panel.alarm();
                    panel.reset();
                    return;
                }
                LocalDateTime nowTime = LocalDateTime.now();
                LocalDateTime timeItems = target.minusHours(nowTime.getHour()).minusMinutes(nowTime.getMinute()).minusSeconds(nowTime.getSecond());
                hours = timeItems.getHour();
                minutes = timeItems.getMinute();
                seconds = timeItems.getSecond();
                panel.rePaint(hours, minutes, seconds);

            }
        }, 0, 100);
    }

    public void reset(){
        if (timer != null){
            timer.cancel();
        }
        hours = 0;
        minutes = 0;
        seconds = 0;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }
}
