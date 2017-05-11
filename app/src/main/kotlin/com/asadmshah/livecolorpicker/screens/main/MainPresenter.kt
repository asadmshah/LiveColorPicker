package com.asadmshah.livecolorpicker.screens.main

import android.os.Bundle

class MainPresenter(val view: MainContract.View) : MainContract.Presenter {

    private val doTransitionToStatic: () -> Unit = {
        nextTransition = null
        view.captureImage {
            view.setImage(it)
            view.setImageTrackerEnabled(true)
            view.setCameraTrackerEnabled(false)
            nextTransition = doTransitionToDynamic
        }
    }

    private val doTransitionToDynamic: () -> Unit = {
        nextTransition = null
        view.setImage(null)
        view.setImageTrackerEnabled(false)
        view.setCameraTrackerEnabled(true)
        nextTransition = doTransitionToStatic
    }

    var nextTransition: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        if (view.hasCameraPermission()) {
            try {
                view.cameraOpen()
                doTransitionToDynamic()
            } catch (t: Throwable) {
                // TODO: Handle Error
            }
        } else {
            view.requestCameraPermission()
        }
    }

    override fun onPause() {
        view.setImageTrackerEnabled(false)
        view.setImage(null)
        view.setCameraTrackerEnabled(false)
        nextTransition = null

        try {
            view.cameraClose()
        } catch (t: Throwable) {
            // TODO: Handle Error
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun onDestroy() {

    }

    override fun onCameraPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            try {
                view.cameraOpen()
                doTransitionToDynamic()
            } catch (t: Throwable) {
                // TODO: Handle Error
            }
        } else {
            // TODO: Handle Permission Error
        }
    }

    override fun onTouchEvent(x: Float, y: Float, c: Int) {
        view.setPoint(x, y)
        view.setColor(c)
        view.setColorCode(c)
        view.setColorName("Unknown")
    }

    override fun onCaptureButtonClicked() {
        nextTransition?.invoke()
    }

    override fun onColorsButtonClicked() {

    }
}