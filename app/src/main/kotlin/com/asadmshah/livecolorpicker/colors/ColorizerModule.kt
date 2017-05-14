package com.asadmshah.livecolorpicker.colors

import com.asadmshah.livecolorpicker.storage.Storage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ColorizerModule {

    @Provides
    @Singleton
    fun colorizer(storage: Storage): Colorizer {
        return ColorizerImpl(storage)
    }

}