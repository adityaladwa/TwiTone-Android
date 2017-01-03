package com.ladwa.aditya.twitone.injection.component;


import com.ladwa.aditya.twitone.injection.ConfigPersistent;
import com.ladwa.aditya.twitone.injection.module.ActivityModule;
import com.ladwa.aditya.twitone.injection.module.FragmentModule;

import dagger.Component;

@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {

    ActivityComponent activityComponent(ActivityModule activityModule);

    FragmentComponent fragmentComponent(FragmentModule fragmentModule);

}
