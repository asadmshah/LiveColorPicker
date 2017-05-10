package com.asadmshah.livecolorpicker.screens.main

import android.os.Bundle

class MainPresenter(val view: MainContract.View) : MainContract.Presenter {

    override fun onCreate(savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        if (view.hasCameraPermission()) {
            view.cameraConnect()
        } else {
            view.requestCameraPermission()
        }
    }

    override fun onPause() {
        view.cameraDisconnect()
    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun onDestroy() {

    }

    override fun onCameraPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            view.cameraConnect()
        } else {
            // TODO: Handle Permission Error
        }
    }

    override fun onColorTouched(x: Int, y: Int, color: Int, isDrag: Boolean) {

    }

    override fun onFreezeFrameClicked() {

    }

    override fun onHistoryClicked() {

    }
}