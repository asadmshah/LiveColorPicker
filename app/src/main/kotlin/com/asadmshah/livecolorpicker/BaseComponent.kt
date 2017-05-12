package com.asadmshah.livecolorpicker

import com.asadmshah.livecolorpicker.screens.ActivityComponent
import com.asadmshah.livecolorpicker.screens.ActivityModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        BaseModule::class
))
interface BaseComponent {
    fun activityComponent(module: ActivityModule): ActivityComponent
}