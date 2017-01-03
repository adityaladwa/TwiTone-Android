package com.ladwa.aditya.twitone.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ladwa.aditya.twitone.R;


/**
 * An App widget provider
 * Created by Aditya on 25-Jul-16.
 */
public class WidgetCollectionProvider extends AppWidgetProvider {
    public static final String CLICK_ACTION = "CLICK_ACTION";
    public static final String EXTRA_ITEM = "EXTRA_ITEM";


    @Override
    public void onReceive(Context context, Intent intent) {
//        Timber.d("onRecieve");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(CLICK_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            int id = appWidgetIds[i];

            Intent intentWidget = new Intent(context, WidgetCollectionService.class);
            intentWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            intentWidget.setData(Uri.parse(intentWidget.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews views = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.widget_collection);
            views.setRemoteAdapter(R.id.widget_list_view, intentWidget);

            Intent clickIntent = new Intent(context, WidgetCollectionService.class);
            clickIntent.setAction(CLICK_ACTION);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));


            PendingIntent clickPendingIntent = PendingIntent
                    .getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list_view, clickPendingIntent);
            appWidgetManager.updateAppWidget(id, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
