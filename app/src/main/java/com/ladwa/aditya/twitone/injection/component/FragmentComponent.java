package com.ladwa.aditya.twitone.injection.component;


import com.ladwa.aditya.twitone.injection.PerFragment;
import com.ladwa.aditya.twitone.injection.module.FragmentModule;
import com.ladwa.aditya.twitone.ui.login.LoginActivityFragment;
import com.ladwa.aditya.twitone.ui.login.LoginPresenter;

import dagger.Subcomponent;


@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(LoginActivityFragment loginActivityFragment);
}
