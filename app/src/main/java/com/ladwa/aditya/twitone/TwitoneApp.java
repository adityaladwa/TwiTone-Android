package com.ladwa.aditya.twitone;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.ladwa.aditya.twitone.data.DaggerTwitterComponent;
import com.ladwa.aditya.twitone.data.TwitterComponent;
import com.ladwa.aditya.twitone.data.remote.TwitterModule;
import com.ladwa.aditya.twitone.util.ConnectionReceiver;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

/**
 * Created by Aditya on 25-Jun-16.
 */
public class TwitoneApp extends MultiDexApplication {
    private static TwitterComponent mTwitterComponent;
    private RefWatcher refWatcher;
    private static TwitoneApp smTwitoneApp;

    @Override
    public void onCreate() {
        super.onCreate();
        smTwitoneApp = this;
        refWatcher = LeakCanary.install(this);
        mTwitterComponent = DaggerTwitterComponent.builder()
                .appModule(new AppModule(this))
                .twitterModule(new TwitterModule())
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static synchronized TwitoneApp getInstance() {
        return smTwitoneApp;
    }


    public static void setConnectionListener(ConnectionReceiver.ConnectionReceiverListener listener) {
        ConnectionReceiver.connectionReceiverListener = listener;
    }

    public static TwitterComponent getTwitterComponent(Context context) {
        TwitoneApp application = (TwitoneApp) context.getApplicationContext();
        return mTwitterComponent;
    }

    public static RefWatcher getRefWatcher(Context context) {
        TwitoneApp application = (TwitoneApp) context.getApplicationContext();
        return application.refWatcher;
    }
}
