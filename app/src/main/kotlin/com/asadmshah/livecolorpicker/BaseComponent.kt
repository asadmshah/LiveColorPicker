package com.asadmshah.livecolorpicker

import com.asadmshah.livecolorpicker.colors.ColorizerModule
import com.asadmshah.livecolorpicker.database.DatabaseModule
import com.asadmshah.livecolorpicker.screens.ActivityComponent
import com.asadmshah.livecolorpicker.screens.ActivityModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        BaseModule::class,
        DatabaseModule::class,
        ColorizerModule::class
))
interface BaseComponent {
    fun activityComponent(module: ActivityModule): ActivityComponent
}