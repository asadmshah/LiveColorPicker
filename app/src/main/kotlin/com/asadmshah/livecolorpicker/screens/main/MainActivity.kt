package com.asadmshah.livecolorpicker.screens.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.asadmshah.livecolorpicker.R
import com.asadmshah.livecolorpicker.widgets.BaseActivity
import com.asadmshah.livecolorpicker.widgets.CameraOneTextureView
import com.asadmshah.livecolorpicker.widgets.TouchPainterView

class MainActivity : BaseActivity(), MainContract.View {

    private val viewCamera by lazyView<CameraOneTextureView>(R.id.camera_view) {
        it.onTouchListener = { x, y, c ->
            viewTouchPainter.putPoint(x, y, c)
        }
        it.onImageListener = { bitmap ->
            viewColor.setImageBitmap(bitmap)
        }
        it.onRectFListener = { rectF ->
            viewTouchPainter.putRectF(rectF)
        }
    }
    private val viewColor by lazyView<ImageView>(R.id.color_container)

    private val viewTouchPainter by lazyView<TouchPainterView>(R.id.touch_painter)

    private val presenter by lazy { MainPresenter(this@MainActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    override fun setMarker(x: Int, y: Int) {

    }

    override fun moveMarker(x: Int, y: Int) {

    }

    override fun setButtonColor(color: Int) {

    }

    override fun setColorTitle(title: String) {

    }

    override fun setColorHexCode(hexCode: String) {

    }

    override fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
    }

    override fun setError(stringRes: Int) {

    }
}