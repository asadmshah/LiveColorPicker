package com.asadmshah.livecolorpicker.widgets

import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {

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