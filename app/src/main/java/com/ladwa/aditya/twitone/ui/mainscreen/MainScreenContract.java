package com.ladwa.aditya.twitone.ui.mainscreen;

import com.ladwa.aditya.twitone.BasePresenter;
import com.ladwa.aditya.twitone.BaseView;
import com.ladwa.aditya.twitone.data.local.models.Tweet;

import java.util.List;

/**
 * A contract that holds the abstraction of MainScreen
 * Created by Aditya on 27-Jun-16.
 */
public interface MainScreenContract {

    interface View extends BaseView<Presenter> {
        void logout();

        void loadedUser(com.ladwa.aditya.twitone.data.local.models.User user);

        void loadTimeline(List<Tweet> tweetList);

        void setScrollPos();

        void saveScrollPosition();

        void stopRefreshing();

        void showRefreshing();

        void showNotification(int tweets);

        void showError();

        void createdFavouriteCallback(Tweet tweet);

        void destroyFavouriteCallback(Tweet tweet);

        void createRetweetCallback(Tweet tweet);
    }

    interface Presenter extends BasePresenter {

        void loadUserInfo();

        void loadTimeLine();

        void refreshRemoteTimeline();

        void createFavourite(long id);

        void unFavourite(long id);

        void createRetweet(long id);

        void deleteLocalData();

        void refreshBelowTimeline();

    }
}
