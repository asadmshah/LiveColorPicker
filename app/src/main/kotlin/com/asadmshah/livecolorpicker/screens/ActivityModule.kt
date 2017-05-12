package com.asadmshah.livecolorpicker.screens

import android.content.Context
import com.asadmshah.livecolorpicker.scopes.ActivityScope
import com.asadmshah.livecolorpicker.widgets.BaseActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: BaseActivity) {

    @Provides
    @ActivityScope
    fun context(): Context = activity

    @Provides
    @ActivityScope
    fun activity(): BaseActivity = activity

}