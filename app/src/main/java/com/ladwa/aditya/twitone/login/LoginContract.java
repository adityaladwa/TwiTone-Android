package com.ladwa.aditya.twitone.login;

/**
 * Created by Aditya on 24-Jun-16.
 */
public interface LoginContract {
    interface View {
        void onError();

        void onSuccess();

    }

    interface Presenter {
        void login();
    }
}
