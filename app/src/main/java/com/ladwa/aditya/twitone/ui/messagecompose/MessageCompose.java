package com.ladwa.aditya.twitone.ui.messagecompose;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ladwa.aditya.twitone.BaseActivity;
import com.ladwa.aditya.twitone.R;

public class MessageCompose extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_message_compose);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}
