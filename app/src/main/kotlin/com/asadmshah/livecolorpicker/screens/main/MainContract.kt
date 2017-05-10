package com.asadmshah.livecolorpicker.screens.main

import android.os.Bundle
import android.support.annotation.StringRes

interface MainContract {

    interface View {

        fun cameraConnect()

        fun cameraDisconnect()

        fun setPoint(x: Float, y: Float)

        fun setColor(color: Int)

        fun setColorName(color: String)

        fun setColorCode(color: Int)

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

        fun onTouchEvent(x: Float, y: Float, c: Int)

        fun onCaptureButtonClicked()

        fun onColorsButtonClicked()
    }

}