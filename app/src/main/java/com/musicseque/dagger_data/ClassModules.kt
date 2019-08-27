package com.musicseque.dagger_data

import com.musicseque.utilities.Utils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClassModules {

    @Provides
    @Singleton
    fun utilsObject(): Utils {
        val utils = Utils()
        return utils
    }

}