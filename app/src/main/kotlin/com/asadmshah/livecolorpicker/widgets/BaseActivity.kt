package com.asadmshah.livecolorpicker.widgets

import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import com.asadmshah.livecolorpicker.BaseApplication
import com.asadmshah.livecolorpicker.screens.ActivityModule

abstract class BaseActivity: AppCompatActivity() {

    val component by lazy {
        BaseApplication.component(this).activityComponent(ActivityModule(this))
    }

    inline protected fun <reified T> lazyView(@IdRes id: Int): Lazy<T> {
        return lazy { findViewById(id) as T }
    }

    inline protected fun <reified T> lazyView(@IdRes id: Int, crossinline init: (T) -> Unit): Lazy<T> {
        return lazy {
            val v = findViewById(id) as T
            init(v)
            v
        }
    }

}