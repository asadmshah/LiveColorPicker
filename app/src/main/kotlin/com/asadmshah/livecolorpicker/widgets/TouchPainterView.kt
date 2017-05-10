package com.asadmshah.livecolorpicker.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import timber.log.Timber

class TouchPainterView : View {

    private var pointXY: Pair<Float, Float>? = null
    private var pointColor: Int = Color.TRANSPARENT
    private var rectF: RectF? = null

    private var paint: Paint = Paint()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    fun putPoint(x: Int, y: Int, color: Int) {
        Timber.d("Drawing Points: ($x, $y, ${String.format("#%06X", (0xFFFFFF and color))})")
        pointXY = x.toFloat() to y.toFloat()
        pointColor = color
        invalidate()
    }

    fun putRectF(rectF: RectF) {
        Timber.d("Drawing RectF: ${rectF.toShortString()}")
        this.rectF = rectF
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (rectF != null) {
            paint.color = Color.argb(128, 128, 128, 128)
            canvas.drawRect(rectF, paint)
        }

        if (pointXY != null) {
            paint.color = pointColor
            canvas.drawCircle(pointXY!!.first, pointXY!!.second, 50f, paint)
        }
    }

}