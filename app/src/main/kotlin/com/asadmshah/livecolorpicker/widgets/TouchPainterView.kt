package com.asadmshah.livecolorpicker.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.graphics.ColorUtils
import android.util.AttributeSet
import android.view.View

class TouchPainterView : View {

    private var pointX: Float = Float.NEGATIVE_INFINITY
    private var pointY: Float = Float.NEGATIVE_INFINITY
    private var paint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.WHITE
    }

    private var pointAnimation: ValueAnimator? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    fun putPoint(x: Float, y: Float) {
        pointX = x
        pointY = y
        paint.color = Color.WHITE

        pointAnimation?.cancel()
        pointAnimation = null

        val animation = ValueAnimator.ofInt(255, 0)
        animation.duration = 500
        animation.startDelay = 500
        animation.addUpdateListener {
            paint.color = ColorUtils.setAlphaComponent(Color.WHITE, it.animatedValue as Int)
            invalidate()
        }
        animation.start()

        pointAnimation = animation

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        paint.strokeWidth = 2f
        canvas.drawCircle(pointX, pointY, 10f, paint)
        paint.strokeWidth = 5f
        canvas.drawCircle(pointX, pointY, 25f, paint)
    }

}