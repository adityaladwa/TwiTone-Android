package com.ladwa.aditya.twitone.data;

/**
 * An interface that Encapsulates all the Operations which will be implemented by Local Databse and Remote Twitter Service
 * Created by Aditya on 25-Jun-16.
 */
public interface TwitterDataStore {

    void getUserInfo(long userID);
}
