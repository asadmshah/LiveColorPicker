package com.asadmshah.livecolorpicker

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crash.FirebaseCrash
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

        prepareAnalytics()
        prepareLogging()
    }

    private fun prepareLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
                    if (priority == Log.VERBOSE || priority == Log.DEBUG) return
                    FirebaseCrash.log(message)
                    if (t != null) FirebaseCrash.report(t)
                }
            })
        }
    }

    private fun prepareAnalytics() {
        FirebaseAnalytics.getInstance(this)
    }

}