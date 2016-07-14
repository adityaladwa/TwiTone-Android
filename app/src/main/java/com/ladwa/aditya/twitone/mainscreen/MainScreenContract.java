package com.ladwa.aditya.twitone.mainscreen;

import com.ladwa.aditya.twitone.BasePresenter;
import com.ladwa.aditya.twitone.BaseView;

/**
 * A contract that holds the abstraction of MainScreen
 * Created by Aditya on 27-Jun-16.
 */
public class MainScreenContract {

    interface View extends BaseView<Presenter> {
        void logout();

        void loadedUser(com.ladwa.aditya.twitone.data.local.models.User user);
    }

    interface Presenter extends BasePresenter {

        void loadUserInfo();

        void loadTimeLine();
    }
}
