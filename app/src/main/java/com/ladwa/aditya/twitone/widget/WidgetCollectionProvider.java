package com.ladwa.aditya.twitone.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ladwa.aditya.twitone.R;

import timber.log.Timber;

/**
 * An App widget provider
 * Created by Aditya on 25-Jul-16.
 */
public class WidgetCollectionProvider extends AppWidgetProvider {
    public static final String CLICK_ACTION = "com.ladwa.aditya.twitone.CLICK_ACTION";
    public static final String EXTRA_ITEM = "com.ladwa.aditya.twitone.EXTRA_ITEM";


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Timber.d("onRecieve");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(CLICK_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i = 0; i < appWidgetIds.length; i++) {
            int id = appWidgetIds[i];
            Log.d("Collection", "yes");


            Intent intentWidget = new Intent(context, WidgetCollectionService.class);
            intentWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            intentWidget.setData(Uri.parse(intentWidget.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews views = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.widget_collection);
            views.setRemoteAdapter(R.id.widgetListviewCollection, intentWidget);

            Intent clickIntent = new Intent(context, WidgetCollectionService.class);
            clickIntent.setAction(CLICK_ACTION);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            intentWidget.setData(Uri.parse(intentWidget.toUri(Intent.URI_INTENT_SCHEME)));


            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widgetListviewCollection, clickPendingIntent);
            appWidgetManager.updateAppWidget(id, views);
        }
    }
}
