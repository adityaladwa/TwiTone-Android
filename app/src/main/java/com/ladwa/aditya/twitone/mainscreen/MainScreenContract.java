package com.ladwa.aditya.twitone.mainscreen;

import com.ladwa.aditya.twitone.BasePresenter;
import com.ladwa.aditya.twitone.BaseView;
import com.ladwa.aditya.twitone.data.local.models.Tweet;

import java.util.List;

/**
 * A contract that holds the abstraction of MainScreen
 * Created by Aditya on 27-Jun-16.
 */
public class MainScreenContract {

    interface View extends BaseView<Presenter> {
        void logout();

        void loadedUser(com.ladwa.aditya.twitone.data.local.models.User user);

        void loadTimeline(List<Tweet> tweetList);

        void setScrollPos();

        void saveScrollPosition();

        void stopRefreshing();

        void showError();
    }

    interface Presenter extends BasePresenter {

        void loadUserInfo();

        void loadTimeLine();

        void refreshRemoteTimeline();


    }
}
