package com.example.playerok.utils.converters;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

public class TimeConverter {

    @SuppressLint("DefaultLocale")
    public static String convertToMMSS(String duration) {
        Long millis = Long.parseLong(duration);
        if (TimeUnit.MILLISECONDS.toHours(millis) == 0) {
            return String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)); }
        else {
            return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        }
    }
}
