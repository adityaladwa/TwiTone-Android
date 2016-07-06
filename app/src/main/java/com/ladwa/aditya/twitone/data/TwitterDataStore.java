package com.ladwa.aditya.twitone.data;

import com.ladwa.aditya.twitone.data.local.models.User;

import rx.Observable;

/**
 * An interface that Encapsulates all the Operations which will be implemented by Local Database and Remote Twitter Service
 * Created by Aditya on 25-Jun-16.
 */
public interface TwitterDataStore {

    Observable<User> getUserInfo(long userID);
}
