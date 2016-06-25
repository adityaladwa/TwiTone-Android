package com.ladwa.aditya.twitone.data;

import com.ladwa.aditya.twitone.AppModule;
import com.ladwa.aditya.twitone.data.remote.TwitterModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Aditya on 25-Jun-16.
 */
@Singleton
@Component(modules = {AppModule.class, TwitterModule.class})
public interface TwitterComponent {

    //Expose methods from TwitterModulde
}
