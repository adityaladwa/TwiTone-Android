package com.ladwa.aditya.twitone.injection.component;


import com.ladwa.aditya.twitone.injection.PerActivity;
import com.ladwa.aditya.twitone.injection.module.ActivityModule;
import com.ladwa.aditya.twitone.ui.base.BaseActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(BaseActivity baseActivity);
}
