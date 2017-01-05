package com.ladwa.aditya.twitone.ui.login;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.ui.base.BaseActivity;


public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

}
