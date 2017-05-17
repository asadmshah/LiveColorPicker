package com.asadmshah.livecolorpicker.screens.main

import android.graphics.Bitmap
import android.os.Bundle
import com.asadmshah.livecolorpicker.colors.Colorizer
import com.asadmshah.livecolorpicker.database.ColorsStore
import com.asadmshah.livecolorpicker.models.Color
import com.asadmshah.livecolorpicker.models.ColorList
import com.asadmshah.livecolorpicker.models.ColorPalette
import com.asadmshah.livecolorpicker.screens.ActivityComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MainPresenter(val view: MainContract.View, component: ActivityComponent) : MainContract.Presenter {

    @Inject lateinit var colorizer: Colorizer
    @Inject lateinit var colorsStore: ColorsStore

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
    var previousColor: Color? = null

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
                Timber.e(t)
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
            Timber.e(t)
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
                Timber.e(t)
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
                .subscribe { color, error ->
                    if (error != null) {
                        Timber.e(error)
                    } else {
                        previousColor = color

                        view.setPoint(x, y)
                        view.setColor(c)
                        view.setColorCode(c)
                        view.setColorName(color.name)
                    }
                }
    }

    override fun onCaptureButtonClicked() {
        nextTransition?.invoke()
    }

    override fun onColorsButtonClicked() {
        view.navigateToColorsListActivity()
    }

    override fun onPaletteButtonClicked() {
        if (!isCameraOpened) return

        if (isStatic) requestStaticPalette() else requestDynamicPalette()
    }

    override fun onColorCaptureButtonClicked() {
        colorizerDisposable?.dispose()
        colorizerDisposable = previousColor?.let { color ->
            colorsStore.insert(ColorPalette(Date(), ColorList().apply { add(color) }))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { _, error ->
                        if (error != null) {
                            Timber.e(error)
                        }
                    }
        }
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
                .flatMap { colorsStore.insert(it) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _, error ->
                    if (error != null) {
                        Timber.e(error)
                    }
                }
    }
}