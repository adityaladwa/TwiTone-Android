package com.ladwa.aditya.twitone.injection.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.injection.ApplicationContext;
import com.ladwa.aditya.twitone.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import twitter4j.AsyncTwitter;
import twitter4j.Twitter;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    TwitterRepository repository();

    Twitter twitter();

    AsyncTwitter async();

    SharedPreferences sharedPreferences();

}
