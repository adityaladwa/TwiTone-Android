package com.ladwa.aditya.twitone.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.data.local.TwitterContract;
import com.ladwa.aditya.twitone.util.Utility;

import timber.log.Timber;

/**
 * A Remote Factory class for Collection Widget
 * Created by Aditya on 25-Jul-16.
 */
public class WidgetCollectionRemoteFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext = null;

    public WidgetCollectionRemoteFactory(Context context, Intent intent) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {
        mCursor = mContext.getContentResolver().query(TwitterContract.Tweet.CONTENT_URI, null, null, null, TwitterContract.Tweet.COLUMN_ID + " DESC ");
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);
        try {
            Timber.d(String.valueOf(position));
            if (position < getCount()) {
                mCursor.move(position + 1);
                views.setTextViewText(R.id.textview_user_name,
                        mCursor.getString(mCursor.getColumnIndex(TwitterContract.Tweet.COLUMN_USER_NAME)));
                views.setTextViewText(R.id.textview_screen_name,
                        mCursor.getString(mCursor.getColumnIndex(TwitterContract.Tweet.COLUMN_USER_SCREEN_NAME)));
                views.setTextViewText(R.id.textview_time,
                        Utility.parseDate(mCursor.getString(mCursor.getColumnIndex(TwitterContract.Tweet.COLUMN_DATE_CREATED)))
                );
                views.setTextViewText(R.id.textview_tweet,
                        mCursor.getString(mCursor.getColumnIndex(TwitterContract.Tweet.COLUMN_TWEET)));


            }
        } catch (CursorIndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
            Timber.d("Exception");
        }

        Bundle extras = new Bundle();
        extras.putInt(WidgetCollectionProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        fillInIntent.setAction(WidgetCollectionProvider.CLICK_ACTION);
        // Make it possible to distinguish the individual on-click
        // action of a given item
        views.setOnClickFillInIntent(R.id.frame_widget, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
