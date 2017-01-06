package com.ladwa.aditya.twitone.ui.tweetdetail;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.ui.base.BaseActivity;

public class TweetDetail extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
