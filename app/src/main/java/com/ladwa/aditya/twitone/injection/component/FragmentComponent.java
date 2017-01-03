package com.ladwa.aditya.twitone.injection.component;


import com.ladwa.aditya.twitone.injection.PerFragment;
import com.ladwa.aditya.twitone.injection.module.FragmentModule;

import dagger.Subcomponent;


@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

}
