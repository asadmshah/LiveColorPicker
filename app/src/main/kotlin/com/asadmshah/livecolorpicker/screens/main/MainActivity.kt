package com.asadmshah.livecolorpicker.screens.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import com.asadmshah.livecolorpicker.R
import com.asadmshah.livecolorpicker.screens.colors_list.ColorsListActivity
import com.asadmshah.livecolorpicker.widgets.*

class MainActivity : BaseActivity(), MainContract.View {

    private val viewCamera by lazyView<CameraOneTextureView>(R.id.camera_view)
    private val viewImage by lazyView<TrackableImageView>(R.id.image_view)
    private val viewTouchPainter by lazyView<TouchPainterView>(R.id.touch_painter)
    private val viewColorCircle by lazyView<CircleView>(R.id.color_circle)
    private val viewColorName by lazyView<TextView>(R.id.color_name)
    private val viewColorCode by lazyView<TextView>(R.id.color_code)
    private val viewCaptureButton by lazyView<ImageView>(R.id.button_capture)
    private val viewPaletteButton by lazyView<ImageView>(R.id.button_palette)
    private val viewColorsButton by lazyView<ImageView>(R.id.button_colors)

    private val presenter by lazy { MainPresenter(this@MainActivity, this@MainActivity.component) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewColorCircle.setOnClickListener { presenter.onColorCaptureButtonClicked() }
        viewCaptureButton.setOnClickListener { presenter.onCaptureButtonClicked() }
        viewPaletteButton.setOnClickListener { presenter.onPaletteButtonClicked() }
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

    override fun navigateToColorsListActivity() {
        val intent = ColorsListActivity.createIntent(this)
        startActivity(intent)
    }

    override fun cameraOpen() {
        viewCamera.cameraConnect()
    }

    override fun cameraClose() {
        viewCamera.cameraDisconnect()
    }

    override fun setCameraTrackerEnabled(enabled: Boolean) {
        if (enabled) {
            viewCamera.onTrackEvent = { x, y, c ->
                presenter.onTouchEvent(x, y, c)
            }
        } else {
            viewCamera.onTrackEvent = null
        }
    }

    override fun setImageTrackerEnabled(enabled: Boolean) {
        if (enabled) {
            viewImage.onTrackEvent = { x, y, c ->
                presenter.onTouchEvent(x, y, c)
            }
        } else {
            viewImage.onTrackEvent = null
        }
    }

    override fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
    }

    override fun setError(stringRes: Int, vararg args: Any) {

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

    override fun captureImage(onCapture: (Bitmap) -> Unit) {
        viewCamera.captureImage(onCapture)
    }

    override fun setImage(bitmap: Bitmap?) {
        viewImage.setImageBitmap(bitmap)
    }

    override fun getImage(): Bitmap? {
        return viewImage.getImageBitmap()
    }
}