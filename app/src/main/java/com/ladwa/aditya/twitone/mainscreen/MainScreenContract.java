package com.ladwa.aditya.twitone.mainscreen;

import com.ladwa.aditya.twitone.BasePresenter;
import com.ladwa.aditya.twitone.BaseView;

/**
 * Created by Aditya on 27-Jun-16.
 */
public class MainScreenContract {

    interface View extends BaseView<Presenter> {
        void logout();
    }

    interface Presenter extends BasePresenter {

    }
}
