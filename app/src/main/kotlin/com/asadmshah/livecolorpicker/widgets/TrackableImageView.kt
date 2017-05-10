package com.asadmshah.livecolorpicker.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView

class TrackableImageView: ImageView {

    var onTouchEvent: ((x: Float, y: Float, c: Int) -> Unit)? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (onTouchEvent != null) return false

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> captureColor(event.x, event.y)
            MotionEvent.ACTION_MOVE -> captureColor(event.x, event.y)
        }

        return true
    }

    private fun captureColor(x: Float, y: Float) {
        onTouchEvent?.invoke(x, y, Color.WHITE)
    }

}