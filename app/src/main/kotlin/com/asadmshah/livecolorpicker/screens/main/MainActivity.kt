package com.asadmshah.livecolorpicker.screens.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import com.asadmshah.livecolorpicker.R
import com.asadmshah.livecolorpicker.widgets.BaseActivity
import com.asadmshah.livecolorpicker.widgets.CameraOneTextureView
import com.asadmshah.livecolorpicker.widgets.CircleView
import com.asadmshah.livecolorpicker.widgets.TouchPainterView

class MainActivity : BaseActivity(), MainContract.View {

    private val viewCamera by lazyView<CameraOneTextureView>(R.id.camera_view) {
        it.onTouchEvent = { x, y, c ->
            presenter.onTouchEvent(x, y, c)
        }
    }
    private val viewTouchPainter by lazyView<TouchPainterView>(R.id.touch_painter)
    private val viewColorCircle by lazyView<CircleView>(R.id.color_circle)
    private val viewColorName by lazyView<TextView>(R.id.color_name)
    private val viewColorCode by lazyView<TextView>(R.id.color_code)
    private val viewCaptureButton by lazyView<ImageView>(R.id.button_capture)
    private val viewColorsButton by lazyView<ImageView>(R.id.button_colors)

    private val presenter by lazy { MainPresenter(this@MainActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewCaptureButton.setOnClickListener { presenter.onCaptureButtonClicked() }
        viewColorsButton.setOnClickListener { presenter.onColorsButtonClicked() }

        presenter.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()

        presenter.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        presenter.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        presenter.onCameraPermissionResult(grantResults[0] == PackageManager.PERMISSION_GRANTED)
    }

    override fun cameraConnect() {
        viewCamera.cameraConnect()
    }

    override fun cameraDisconnect() {
        viewCamera.cameraDisconnect()
    }

    override fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
    }

    override fun setError(stringRes: Int) {

    }

    override fun setColor(color: Int) {
        viewColorCircle.circleColor = color
    }

    override fun setPoint(x: Float, y: Float) {
        viewTouchPainter.putPoint(x, y)
    }

    override fun setColorName(color: String) {
        viewColorName.text = color
    }

    override fun setColorCode(color: Int) {
        viewColorCode.text = String.format("#%06X", (0xFFFFFF and color))
    }
}