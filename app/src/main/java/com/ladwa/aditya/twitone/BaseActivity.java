package com.ladwa.aditya.twitone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ladwa.aditya.twitone.settings.SettingsRepository;

/**
 * A Base Activity that all other activites extend
 * Created by Aditya on 20-Oct-16.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsRepository.getInstance().setTheme(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SettingsRepository.destroyInstance();
    }
}
