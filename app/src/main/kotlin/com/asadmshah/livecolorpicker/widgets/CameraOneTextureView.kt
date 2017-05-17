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

    private var previousTouchX: Float = Float.NEGATIVE_INFINITY
    private var previousTouchY: Float = Float.NEGATIVE_INFINITY
    var onTrackEvent: ((Float, Float, Int) -> Unit)? = null

    var dstBitmap: Bitmap? = null

    var txtMatrix: Matrix? = null
    var bmpMatrix: Matrix? = null

    var colorDisposable: Disposable? = null
    var imageDisposable: Disposable? = null

    var surfaceTextureAvailableCalled = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (onTrackEvent == null) return false

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> captureColor(event.x, event.y) { x, y, c -> onTrackEvent?.invoke(x, y, c) }
            MotionEvent.ACTION_MOVE -> captureColor(event.x, event.y) { x, y, c -> onTrackEvent?.invoke(x, y, c) }
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
        Timber.d("onSurfaceTextureAvailable")

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

        val pointRectL = (centerX - previewW / 2)
        val pointRectT = (centerY - previewH / 2)
        pointRectF = RectF(pointRectL, pointRectT, previewW.toFloat(), previewH.toFloat())

        camera.setPreviewTexture(surface)
        camera.startPreview()

        this.camera = camera
    }

    fun cameraConnect() {
        surfaceTextureListener = this

        if (surfaceTexture != null && !surfaceTextureAvailableCalled) {
            onSurfaceTextureAvailable(surfaceTexture, width, height)
            surfaceTextureAvailableCalled = true
        }
    }

    fun cameraDisconnect() {
        colorDisposable?.dispose()
        colorDisposable = null

        imageDisposable?.dispose()
        imageDisposable = null

        surfaceTextureListener = null

        camera?.setPreviewTexture(null)
        camera?.release()
        camera = null

        dstBitmap?.recycle()
        dstBitmap = null
    }

    fun captureImage(onCapture: (Bitmap) -> Unit) {
        if (!(imageDisposable?.isDisposed ?: true)) return

        imageDisposable = Single
                .fromCallable {
                    getBitmap(dstBitmap)
                    val matrix = Matrix()
                    matrix.postRotate(90f, (width/2).toFloat(), (height/2).toFloat())
                    Bitmap.createBitmap(dstBitmap!!, 0, 0, dstBitmap!!.width, dstBitmap!!.height, matrix, true)
                }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bitmap, _ -> onCapture(bitmap) }
    }

    private fun captureColor(x: Float, y: Float, callback: (Float, Float, Int) -> Unit) {
        previousTouchX = x
        previousTouchY = y

        if (!(colorDisposable?.isDisposed ?: true)) return

        colorDisposable = Single
                .fromCallable {
                    getBitmap(dstBitmap)
                    val ax = previousTouchX
                    val ay = previousTouchY
                    val points = floatArrayOf(ax, ay)
                    bmpMatrix!!.mapPoints(points)
                    val bx = (points[0] - pointRectF!!.left).toInt()
                    val by = (points[1] - pointRectF!!.top).toInt()
                    val cx = Math.max(0, Math.min(dstBitmap!!.width-1, bx))
                    val cy = Math.max(0, Math.min(dstBitmap!!.height-1, by))
                    Triple(Math.max(0f, Math.min(ax, width.toFloat())), Math.max(0f, Math.min(ay, height.toFloat())), dstBitmap!!.getPixel(cx, cy))
                }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (x, y, color), _ -> callback(x, y, color) }
    }

}