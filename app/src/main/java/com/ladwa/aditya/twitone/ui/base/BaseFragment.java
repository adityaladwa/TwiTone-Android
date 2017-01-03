package com.ladwa.aditya.twitone.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;


import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.injection.component.ConfigPersistentComponent;
import com.ladwa.aditya.twitone.injection.component.DaggerConfigPersistentComponent;
import com.ladwa.aditya.twitone.injection.component.FragmentComponent;
import com.ladwa.aditya.twitone.injection.module.FragmentModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;

public abstract class BaseFragment extends Fragment {

    private static final String KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID";
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();
    private static final AtomicLong NEXT_ID = new AtomicLong(0);

    private FragmentComponent mFragmentComponent;
    private long mFragmentId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the FragmentComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mFragmentId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_FRAGMENT_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mFragmentId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mFragmentId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(TwitoneApp.get(
                            getActivity()).getComponent())
                    .build();
            sComponentsMap.put(mFragmentId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mFragmentId);
            configPersistentComponent = sComponentsMap.get(mFragmentId);
        }
        mFragmentComponent = configPersistentComponent.fragmentComponent(new FragmentModule(this));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_FRAGMENT_ID, mFragmentId);
    }

    @Override
    public void onDestroy() {
        if (!getActivity().isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mFragmentId);
            sComponentsMap.remove(mFragmentId);
        }
        super.onDestroy();
    }

    public FragmentComponent fragmentComponent() {
        return mFragmentComponent;
    }

    public void setupToolbar(Toolbar toolbar) {
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((BaseActivity) getActivity()).getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
    }
}
