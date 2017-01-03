package com.ladwa.aditya.twitone.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.ui.mainscreen.MainScreen;
import com.ladwa.aditya.twitone.ui.settings.SettingsRepository;

import java.util.List;

/**
 * A Notification Utility class
 * Created by Aditya on 23-Jul-16.
 */
public class NotificationUtil {
    private final static String TAG = NotificationUtil.class.getSimpleName();

    public static void showNotification(Context context, int size, String title, List<Tweet> tweetList) {

        if (SettingsRepository.getInstance().isNotificationEnabled(context))
            return;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notify)
                        .setContentTitle(size + context.getString(R.string.new_tweets))
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setContentText(title);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(size + context.getString(R.string.new_tweets));
        int loopTime;
        int loop = tweetList.size();
        if (loop < 5) {
            loopTime = loop;
        } else {
            loopTime = 5;
        }


        for (int i = 0; i < loopTime; i++) {
            inboxStyle.addLine("@" + tweetList.get(i).getScreenName() + " : " + tweetList.get(i).getTweet());
        }

        mBuilder.setStyle(inboxStyle);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        new Intent(context, MainScreen.class),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat.from(context).notify(100, mBuilder.build());

    }
}
