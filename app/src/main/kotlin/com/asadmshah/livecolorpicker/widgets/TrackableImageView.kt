package com.asadmshah.livecolorpicker.widgets

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView

class TrackableImageView: ImageView {

    var onTrackEvent: ((x: Float, y: Float, c: Int) -> Unit)? = null

    private var cacheBitmap: Bitmap? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        isDrawingCacheEnabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (onTrackEvent == null) return false

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> captureColor(event.x, event.y)
            MotionEvent.ACTION_MOVE -> captureColor(event.x, event.y)
        }

        return true
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        cacheBitmap = null
    }

    private fun captureColor(x: Float, y: Float) {
        if (drawable == null) return

        if (cacheBitmap == null) cacheBitmap = drawingCache

        val ax = Math.max(0, Math.min(cacheBitmap!!.width-1, x.toInt()))
        val ay = Math.max(0, Math.min(cacheBitmap!!.height-1, y.toInt()))
        val color = cacheBitmap!!.getPixel(ax, ay)

        onTrackEvent?.invoke(ax.toFloat(), ay.toFloat(), color)
    }

}