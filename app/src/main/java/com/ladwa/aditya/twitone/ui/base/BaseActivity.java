package com.ladwa.aditya.twitone.ui.base;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.injection.component.ActivityComponent;
import com.ladwa.aditya.twitone.injection.component.ConfigPersistentComponent;
import com.ladwa.aditya.twitone.injection.component.DaggerConfigPersistentComponent;
import com.ladwa.aditya.twitone.injection.module.ActivityModule;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new ArrayMap<>();

    private ActivityComponent mActivityComponent;
    private long mActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mActivityId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(TwitoneApp.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
        mActivityComponent.inject(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            sComponentsMap.remove(mActivityId);
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

    protected void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
