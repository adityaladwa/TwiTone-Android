package com.ladwa.aditya.twitone.tweetdetail;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ladwa.aditya.twitone.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class TweetDetailFragment extends Fragment {

    public TweetDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweet_detail, container, false);
    }
}
