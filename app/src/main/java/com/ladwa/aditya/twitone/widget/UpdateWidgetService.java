package com.ladwa.aditya.twitone.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;
import android.widget.RemoteViews;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.data.local.TwitterContract;
import com.ladwa.aditya.twitone.mainscreen.MainScreen;
import com.ladwa.aditya.twitone.util.Utility;

/**
 * A Setvice that updates widgets
 * Created by Aditya on 25-Jul-16.
 */
public class UpdateWidgetService extends IntentService {
    public static final String TAG = UpdateWidgetService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UpdateWidgetService(String name) {
        super(name);
    }

    public UpdateWidgetService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

        int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int i = 0; i < appWidgetIds.length; i++) {
            int id = appWidgetIds[i];
            Intent inten = new Intent(this.getApplicationContext(), MainScreen.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, inten, 0);

            RemoteViews views = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.item_widget);

            try {
                Cursor mCursor = getContentResolver().query(TwitterContract.Tweet.CONTENT_URI, null, null, null, TwitterContract.Tweet.COLUMN_ID + " DESC");
                Log.d("Widget", String.valueOf(mCursor.getCount()));
                mCursor.moveToFirst();
                views.setTextViewText(R.id.textview_user_name,
                        mCursor.getString(mCursor.getColumnIndex(TwitterContract.Tweet.COLUMN_USER_NAME)));
                views.setTextViewText(R.id.textview_screen_name,
                        mCursor.getString(mCursor.getColumnIndex(TwitterContract.Tweet.COLUMN_USER_SCREEN_NAME)));
                views.setTextViewText(R.id.textview_time,
                        Utility.parseDate(mCursor.getString(mCursor.getColumnIndex(TwitterContract.Tweet.COLUMN_DATE_CREATED)))
                );
                views.setTextViewText(R.id.textview_tweet,
                        mCursor.getString(mCursor.getColumnIndex(TwitterContract.Tweet.COLUMN_TWEET)));


            } catch (CursorIndexOutOfBoundsException | NullPointerException e) {
                e.printStackTrace();
            }

            views.setOnClickPendingIntent(R.id.frame_widget, pendingIntent);
            appWidgetManager.updateAppWidget(id, views);

        }
    }
}
