package com.asadmshah.livecolorpicker.database

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun colorsStore(context: Context): ColorsStore {
        return ColorsStoreImpl(context)
    }

}