package com.asadmshah.livecolorpicker.screens.main

import android.graphics.Bitmap
import android.os.Bundle
import com.asadmshah.livecolorpicker.colors.Colorizer
import com.asadmshah.livecolorpicker.screens.ActivityComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainPresenter(val view: MainContract.View, component: ActivityComponent) : MainContract.Presenter {

    @Inject lateinit var colorizer: Colorizer

    private val doTransitionToStatic: () -> Unit = {
        nextTransition = null
        view.captureImage {
            view.setImage(it)
            view.setImageTrackerEnabled(true)
            view.setCameraTrackerEnabled(false)
            nextTransition = doTransitionToDynamic
            isStatic = true
        }
    }

    private val doTransitionToDynamic: () -> Unit = {
        nextTransition = null
        view.setImage(null)
        view.setImageTrackerEnabled(false)
        view.setCameraTrackerEnabled(true)
        nextTransition = doTransitionToStatic
        isStatic = false
    }

    var isStatic: Boolean = false
    var nextTransition: (() -> Unit)? = null
    var isCameraOpened: Boolean = false
    var colorizerDisposable: Disposable? = null

    init {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        if (view.hasCameraPermission()) {
            try {
                view.cameraOpen()
                doTransitionToDynamic()
                isCameraOpened = true
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
        colorizerDisposable?.dispose()
    }

    override fun onCameraPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            try {
                view.cameraOpen()
                doTransitionToDynamic()
                isCameraOpened = true
            } catch (t: Throwable) {
                // TODO: Handle Error
            }
        } else {
            // TODO: Handle Permission Error
        }
    }

    override fun onTouchEvent(x: Float, y: Float, c: Int) {
        colorizerDisposable?.dispose()

        colorizer.map(c)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (name, _, _), _ ->
                    view.setPoint(x, y)
                    view.setColor(c)
                    view.setColorCode(c)
                    view.setColorName(name)
                }
    }

    override fun onCaptureButtonClicked() {
        nextTransition?.invoke()
    }

    override fun onColorsButtonClicked() {

    }

    override fun onPaletteButtonClicked() {
        if (!isCameraOpened) return

        if (isStatic) requestStaticPalette() else requestDynamicPalette()
    }

    fun requestStaticPalette() {
        view.getImage()?.let { requestPalette(it) }
    }

    fun requestDynamicPalette() {
        view.captureImage { requestPalette(it) }
    }

    fun requestPalette(bitmap: Bitmap) {
        colorizerDisposable?.dispose()
        colorizerDisposable = colorizer
                .palette(bitmap)
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { colors, _ ->

                }
    }
}