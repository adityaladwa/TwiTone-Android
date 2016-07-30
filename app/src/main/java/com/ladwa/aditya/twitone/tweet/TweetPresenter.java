package com.ladwa.aditya.twitone.tweet;

import com.ladwa.aditya.twitone.BasePresenter;
import com.ladwa.aditya.twitone.BaseView;

/**
 * A Class for Tweet Presenter
 * Created by Aditya on 30-Jul-16.
 */
public class TweetPresenter {
    interface View extends BaseView<Presenter> {
        void getUserLocation();

        void onClickLocation();

        void onClickTweet();

    }

    interface Presenter extends BasePresenter {

    }
}
