package com.tripewise.utilites;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Util {
    private static final String datePattern = "d MMM yyyy";
    private static final String timePattern = "HH:MM";
    private static final String timeStampPattern = datePattern + " " + timePattern;

    public static long dateToMilli(String dateString) {
        long time = 0;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(timeStampPattern);

            Date date = dateFormat.parse(dateString);

            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String milliToDate(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(timeStampPattern);

        return dateFormat.format(new Date(time));
    }

    public static boolean validateFormat(String input) {
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        try {
            format.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String createGif() {
        return "file:///android_asset/gif/"+ randomGifName();
    }

    private static String randomGifName() {
        Random random = new Random();
        int n = random.nextInt(20);

        if (n != 0) {
            String gifName = "trip_" + n + ".gif";

            return gifName;
        }

        return randomGifName();
    }
}
