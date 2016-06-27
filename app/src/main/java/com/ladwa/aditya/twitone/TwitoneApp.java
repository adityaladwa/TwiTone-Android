package com.ladwa.aditya.twitone;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.ladwa.aditya.twitone.data.DaggerTwitterComponent;
import com.ladwa.aditya.twitone.data.TwitterComponent;
import com.ladwa.aditya.twitone.data.remote.TwitterModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

/**
 * Created by Aditya on 25-Jun-16.
 */
public class TwitoneApp extends MultiDexApplication {
    private TwitterComponent mTwitterComponent;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        mTwitterComponent = DaggerTwitterComponent.builder()
                .appModule(new AppModule(this))
                .twitterModule(new TwitterModule())
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static TwitterComponent getTwitterComponent(Context context) {
        TwitoneApp application = (TwitoneApp) context.getApplicationContext();
        return application.mTwitterComponent;
    }

    public static RefWatcher getRefWatcher(Context context) {
        TwitoneApp application = (TwitoneApp) context.getApplicationContext();
        return application.refWatcher;
    }
}
