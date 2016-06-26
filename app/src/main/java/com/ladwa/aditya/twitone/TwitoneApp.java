package com.ladwa.aditya.twitone;

import android.support.multidex.MultiDexApplication;

import com.ladwa.aditya.twitone.data.DaggerTwitterComponent;
import com.ladwa.aditya.twitone.data.TwitterComponent;
import com.ladwa.aditya.twitone.data.remote.TwitterModule;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by Aditya on 25-Jun-16.
 */
public class TwitoneApp extends MultiDexApplication {
    private TwitterComponent mTwitterComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        mTwitterComponent = DaggerTwitterComponent.builder()
                .appModule(new AppModule(this))
                .twitterModule(new TwitterModule())
                .build();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }

    public TwitterComponent getTwitterComponent() {
        return mTwitterComponent;
    }
}
