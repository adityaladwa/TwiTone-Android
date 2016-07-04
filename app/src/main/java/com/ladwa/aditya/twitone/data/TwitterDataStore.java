package com.ladwa.aditya.twitone.data;

import rx.Observable;
import twitter4j.User;

/**
 * An interface that Encapsulates all the Operations which will be implemented by Local Databse and Remote Twitter Service
 * Created by Aditya on 25-Jun-16.
 */
public interface TwitterDataStore {

    Observable<User> getUserInfo(long userID);
}
