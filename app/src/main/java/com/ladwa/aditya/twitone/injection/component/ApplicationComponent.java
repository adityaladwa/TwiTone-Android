package com.ladwa.aditya.twitone.injection.component;

import android.app.Application;
import android.content.Context;

import com.ladwa.aditya.twitone.injection.ApplicationContext;
import com.ladwa.aditya.twitone.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

}
