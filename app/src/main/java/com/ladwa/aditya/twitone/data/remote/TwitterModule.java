package com.ladwa.aditya.twitone.data.remote;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import javax.inject.Singleton;

import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by Aditya on 24-Jun-16.
 */
public class TwitterModule {

    public static final HttpUrl TWITTER_API_URL = HttpUrl.parse("https://api.github.com/");


    @Provides
    @Singleton
    HttpUrl provideBaseUrl() {
        return TWITTER_API_URL;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }


    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(HttpUrl baseUrl, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(LoganSquareConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

}
