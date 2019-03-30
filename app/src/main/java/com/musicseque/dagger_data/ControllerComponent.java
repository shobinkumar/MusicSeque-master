package com.musicseque.dagger_data;


import com.musicseque.MainActivity;

import dagger.Component;

@Component(modules = {ControllerModule.class},dependencies = {ApplicationComponent.class})
@ActivityLevelScope
public interface ControllerComponent {



    void inject(MainActivity mainActivity);

}
