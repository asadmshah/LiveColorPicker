package com.asadmshah.livecolorpicker.widgets

import android.support.annotation.IdRes
import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {

    inline protected fun <reified T> lazyView(@IdRes id: Int): Lazy<T> {
        return lazy { view!!.findViewById(id) as T }
    }

    inline protected fun <reified T> lazyView(@IdRes id: Int, crossinline init: (T) -> Unit): Lazy<T> {
        return lazy {
            val v = view!!.findViewById(id) as T
            init(v)
            v
        }
    }

}