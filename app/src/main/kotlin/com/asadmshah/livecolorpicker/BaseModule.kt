package com.asadmshah.livecolorpicker

import android.content.Context
import com.asadmshah.livecolorpicker.colors.Colorizer
import com.asadmshah.livecolorpicker.colors.ColorizerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BaseModule(val context: Context) {

    @Provides
    @Singleton
    fun context(): Context {
        return context
    }

    @Provides
    @Singleton
    fun colorMapper(context: Context): Colorizer {
        return ColorizerImpl(context)
    }

}