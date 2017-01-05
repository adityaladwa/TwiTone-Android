package com.ladwa.aditya.twitone.ui.interactions;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ladwa.aditya.twitone.BaseActivity;
import com.ladwa.aditya.twitone.R;


public class Interactions extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
