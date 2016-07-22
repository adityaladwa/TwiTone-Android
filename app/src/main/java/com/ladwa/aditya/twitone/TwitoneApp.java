package com.ladwa.aditya.twitone;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.multidex.MultiDexApplication;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.stetho.Stetho;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.ladwa.aditya.twitone.data.DaggerTwitterComponent;
import com.ladwa.aditya.twitone.data.TwitterComponent;
import com.ladwa.aditya.twitone.data.remote.TwitterModule;
import com.ladwa.aditya.twitone.util.ConnectionReceiver;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

/**
 * An Application subclass for Twitone App
 * Created by Aditya on 25-Jun-16.
 */
public class TwitoneApp extends MultiDexApplication {
    private static TwitterComponent mTwitterComponent;
    private static RefWatcher refWatcher;
    private static TwitoneApp smTwitoneApp;
    private static Tracker mTracker;

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

        //Stetho
        Stetho.initializeWithDefaults(this);

        //Drawer Image loader
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                super.set(imageView, uri, placeholder);
                Glide.with(imageView.getContext()).load(uri)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                super.cancel(imageView);
                Glide.clear(imageView);
            }
        });

    }

    public static synchronized TwitoneApp getInstance() {
        return smTwitoneApp;
    }


    public static void setConnectionListener(ConnectionReceiver.ConnectionReceiverListener listener) {
        ConnectionReceiver.connectionReceiverListener = listener;
    }

    public static TwitterComponent getTwitterComponent() {
        return mTwitterComponent;
    }

    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }

    public static synchronized Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(smTwitoneApp);
            mTracker = analytics.newTracker(R.xml.track_app);
            mTracker.enableAutoActivityTracking(true);
        }
        return mTracker;
    }
}
