package com.asadmshah.livecolorpicker

import android.app.Application
import android.content.Context
import timber.log.Timber

class BaseApplication : Application() {

    companion object {
        fun component(context: Context): BaseComponent {
            return (context.applicationContext as BaseApplication).component
        }
    }

    private val component: BaseComponent by lazy {
        DaggerBaseComponent.builder().baseModule(BaseModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }

}