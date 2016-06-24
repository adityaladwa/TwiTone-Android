package com.ladwa.aditya.twitone;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Provides;

/**
 * Created by Aditya on 24-Jun-16.
 */
public class AppModule {
    Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }
}
