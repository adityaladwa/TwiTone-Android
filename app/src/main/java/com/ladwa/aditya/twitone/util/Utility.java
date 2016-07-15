package com.ladwa.aditya.twitone.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A Class with utility functions
 * Created by Aditya on 13-Jul-16.
 */
public class Utility {

    public static final long TWO_DAY_IN_SECOND = 172800;
    public static final long DUMMY_TIME = 5;

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static long getTimeDifference(String dbDate) {
        Date d1 = null, d2 = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String now = getDateTime();

        try {
            d2 = dateFormat.parse(now);
            d1 = dateFormat.parse(dbDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert d2 != null;
        assert d1 != null;
        return (d2.getTime() - d1.getTime()) / 1000;
    }

    public static boolean checkProfileDataValidity(String date) {
        long second = getTimeDifference(date);
        return second < TWO_DAY_IN_SECOND;
    }

}
