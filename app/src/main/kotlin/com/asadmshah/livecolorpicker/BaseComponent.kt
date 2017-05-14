package com.asadmshah.livecolorpicker

import com.asadmshah.livecolorpicker.colors.ColorizerModule
import com.asadmshah.livecolorpicker.database.DatabaseModule
import com.asadmshah.livecolorpicker.screens.ActivityComponent
import com.asadmshah.livecolorpicker.screens.ActivityModule
import com.asadmshah.livecolorpicker.storage.StorageModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        BaseModule::class,
        DatabaseModule::class,
        ColorizerModule::class,
        StorageModule::class
))
interface BaseComponent {
    fun activityComponent(module: ActivityModule): ActivityComponent
}