package com.asadmshah.livecolorpicker.colors

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ColorizerModule {

    @Provides
    @Singleton
    fun colorizer(context: Context): Colorizer {
        return ColorizerImpl(context)
    }

}