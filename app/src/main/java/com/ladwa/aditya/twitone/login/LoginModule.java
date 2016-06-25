package com.ladwa.aditya.twitone.login;

import com.ladwa.aditya.twitone.util.CustomeScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aditya on 25-Jun-16.
 */
@Module
public class LoginModule {

    private final LoginContract.View mView;

    public LoginModule(LoginContract.View mView) {
        this.mView = mView;
    }

    @Provides
    @CustomeScope
    LoginContract.View providesLoginView() {
        return mView;
    }
}
