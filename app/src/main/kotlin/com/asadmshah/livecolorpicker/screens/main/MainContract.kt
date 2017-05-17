package com.asadmshah.livecolorpicker.screens.main

import android.graphics.Bitmap
import android.os.Bundle
import android.support.annotation.StringRes

interface MainContract {

    interface View {

        fun navigateToColorsListActivity()

        fun cameraOpen()

        fun cameraClose()

        fun setCameraTrackerEnabled(enabled: Boolean)

        fun setImageTrackerEnabled(enabled: Boolean)

        fun captureImage(onCapture: (Bitmap) -> Unit)

        fun setImage(bitmap: Bitmap?)

        fun getImage(): Bitmap?

        fun setPoint(x: Float, y: Float)

        fun setColor(color: Int)

        fun setColorName(color: String)

        fun setColorCode(color: Int)

        fun hasCameraPermission(): Boolean

        fun requestCameraPermission()

        fun setError(@StringRes stringRes: Int, vararg args: Any)
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

        fun onPaletteButtonClicked()

        fun onColorCaptureButtonClicked()
    }

}