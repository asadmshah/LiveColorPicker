package com.asadmshah.livecolorpicker.storage

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    @Singleton
    fun storage(context: Context): Storage {
        return StorageImpl(context)
    }

}