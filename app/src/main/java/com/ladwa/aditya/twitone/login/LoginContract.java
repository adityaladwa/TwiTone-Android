package com.ladwa.aditya.twitone.login;

import com.ladwa.aditya.twitone.BasePresenter;
import com.ladwa.aditya.twitone.BaseView;

/**
 * Created by Aditya on 24-Jun-16.
 */
public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void onError();

        void onSuccess();

    }

    interface Presenter extends BasePresenter {
        void login();
    }
}
