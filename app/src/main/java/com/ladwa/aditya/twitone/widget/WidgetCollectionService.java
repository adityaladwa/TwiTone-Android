package com.ladwa.aditya.twitone.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * A Setvice for Widget Collection
 * Created by Aditya on 25-Jul-16.
 */
public class WidgetCollectionService extends RemoteViewsService {
    public static final String TAG = WidgetCollectionService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetCollectionRemoteFactory(getApplicationContext(), intent);
    }
}
