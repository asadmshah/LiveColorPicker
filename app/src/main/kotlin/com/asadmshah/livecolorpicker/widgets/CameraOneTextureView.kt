package com.asadmshah.livecolorpicker.widgets

import android.content.Context
import android.graphics.*
import android.hardware.Camera
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.TextureView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CameraOneTextureView : TextureView, TextureView.SurfaceTextureListener {

    private var camera: Camera? = null

    private var previewW: Int = 0
    private var previewH: Int = 0
    private var pointRectF: RectF? = null

    var onTouchListener: ((Int, Int, Int) -> Unit)? = null
    var onImageListener: ((Bitmap) -> Unit)? = null
    var onRectFListener: ((RectF) -> Unit)? = null

    var dstBitmap: Bitmap? = null

    var txtMatrix: Matrix? = null
    var bmpMatrix: Matrix? = null

    var disposable: Disposable? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val initX = event.x
                val initY = event.y
                val points = floatArrayOf(initX, initY)
                captureThen {
                    bmpMatrix!!.mapPoints(points)

                    val x = (points[0] - pointRectF!!.left).toInt()
                    val y = (points[1] - pointRectF!!.top).toInt()
                    val color = it.getPixel(x, y)

                    onTouchListener?.invoke(initX.toInt(), initY.toInt(), color)
                }
            }
        }

        return true
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        Timber.d("onSurfaceTextureAvailable: $width, $height")

        val camera = Camera.open()
        val params = camera.parameters

        val prefRatio = width.toFloat() / height.toFloat()
        val prefSize = params
                .supportedPreviewSizes
                .sortedByDescending {
                    it.width * it.height
                }
                .filter { it.width <= width && it.height <= height }
                .reduce { acc, size ->
                    val accr = acc.width.toFloat() / acc.height.toFloat()
                    val nowr = size.width.toFloat() / acc.height.toFloat()

                    if (Math.abs(accr - prefRatio) <= Math.abs(nowr - prefRatio)) {
                        size
                    } else acc
                }
        params.setPreviewSize(prefSize.width, prefSize.height)
        params.setPreviewFormat(ImageFormat.NV21)
        camera.setParameters(params)

        previewW = prefSize.width
        previewH = prefSize.height

        dstBitmap = Bitmap.createBitmap(previewW, previewH, Bitmap.Config.ARGB_8888)

        val centerX = width / 2f
        val centerY = height / 2f
        txtMatrix = Matrix()
        txtMatrix!!.postRotate(90f, centerX, centerY)
        txtMatrix!!.postScale(previewH.toFloat() / height.toFloat(), previewW.toFloat() / width.toFloat(), centerX, centerY)
        val scaleXY = Math.max(width.toFloat() / previewH.toFloat(), height.toFloat() / previewW.toFloat())
        txtMatrix!!.postScale(scaleXY, scaleXY, centerX, centerY)
        setTransform(txtMatrix)

        val scaleXYRev = Math.min(previewH.toFloat() / width.toFloat(), previewW.toFloat() / height.toFloat())
        bmpMatrix = Matrix()
        bmpMatrix!!.postRotate(-90f, centerX, centerY)
        bmpMatrix!!.postScale(scaleXYRev, scaleXYRev, centerX, centerY)

        val pointRectL = (centerX - previewW/2)
        val pointRectT = (centerY - previewH/2)
        pointRectF = RectF(pointRectL, pointRectT, previewW.toFloat(), previewH.toFloat())

        camera.setPreviewTexture(surface)
        camera.startPreview()

        this.camera = camera
    }

    fun cameraConnect() {
        surfaceTextureListener = this
    }

    fun cameraDisconnect() {
        disposable?.dispose()
        disposable = null

        surfaceTextureListener = null

        camera?.setPreviewTexture(null)
        camera?.release()
        camera = null

        dstBitmap?.recycle()
        dstBitmap = null
    }

    fun cameraCapture() {
        captureThen {
            onImageListener?.invoke(Bitmap.createBitmap(it))
        }
    }

    private fun captureThen(function: (Bitmap) -> Unit) {
        if (!(disposable?.isDisposed ?: true)) return

        disposable = Single
                .fromCallable {
                    getBitmap(dstBitmap)
                    Bitmap.createBitmap(dstBitmap)
                }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bitmap, _ -> function(bitmap) }
    }

}