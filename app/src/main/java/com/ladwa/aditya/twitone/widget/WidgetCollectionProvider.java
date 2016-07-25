package com.ladwa.aditya.twitone.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.mainscreen.MainScreen;

/**
 * An App widget provider
 * Created by Aditya on 25-Jul-16.
 */
public class WidgetCollectionProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; i++) {
            int id = appWidgetIds[i];
            Log.d("Collection", "yes");

            Intent mainActivityIntent = new Intent(context, MainScreen.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0);

            RemoteViews views = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.widget_collection);

            Intent intentWidget = new Intent(context, WidgetCollectionService.class);
            intentWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            intentWidget.setData(Uri.parse(intentWidget.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(R.id.widgetListviewCollection, intentWidget);

            views.setOnClickPendingIntent(R.id.frame_widget, pendingIntent);

            appWidgetManager.updateAppWidget(id, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
