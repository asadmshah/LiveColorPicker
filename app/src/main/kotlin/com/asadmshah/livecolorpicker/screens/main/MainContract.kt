package com.asadmshah.livecolorpicker.screens.main

import android.os.Bundle
import android.support.annotation.StringRes

interface MainContract {

    interface View {

        fun cameraConnect()

        fun cameraDisconnect()

        fun setMarker(x: Int, y: Int)

        fun moveMarker(x: Int, y: Int)

        fun setButtonColor(color: Int)

        fun setColorTitle(title: String)

        fun setColorHexCode(hexCode: String)

        fun hasCameraPermission(): Boolean

        fun requestCameraPermission()

        fun setError(@StringRes stringRes: Int)
    }

    interface Presenter {

        fun onCreate(savedInstanceState: Bundle?)

        fun onResume()

        fun onPause()

        fun onSaveInstanceState(outState: Bundle)

        fun onDestroy()

        fun onCameraPermissionResult(isGranted: Boolean)

        fun onColorTouched(x: Int, y: Int, color: Int, isDrag: Boolean)

        fun onFreezeFrameClicked()

        fun onHistoryClicked()
    }

}