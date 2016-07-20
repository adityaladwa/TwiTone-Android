package com.ladwa.aditya.twitone.util;

import android.content.Context;
import android.content.res.Configuration;

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

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static final String TWITTER_DATE = "Wed Jul 20 01:39:56 GMT+05:30 2016";

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

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static String parseDate(String date) {
        Date twitterDate = null;
        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            twitterDate = sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getTimeAgo(twitterDate.getTime());
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = new Date().getTime();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1 min ago ";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " mins ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1 hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days";
        }
    }
}
