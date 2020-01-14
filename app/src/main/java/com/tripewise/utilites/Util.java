package com.tripewise.utilites;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static long dateToMilli(String dateString) {
        long time = 0;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");

            Date date = dateFormat.parse(dateString);

            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String milliToDate(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");

        return dateFormat.format(new Date(time));
    }
}
