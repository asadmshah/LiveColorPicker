package com.asadmshah.livecolorpicker.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.graphics.ColorUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class CircleView : View {

    var circleColor: Int? = null
        set(value) {
            field = value
            invalidate()
        }
    private var circleRect: RectF = RectF(0f, 0f, 0f, 0f)
    private var circleRadius: Float = 0f
    private var circlePaint: Paint = Paint()
    private val circleStrokeColor: Int = ColorUtils.setAlphaComponent(Color.WHITE, 192)
    private var circleStrokeWidth: Float = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        circleStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (changed) {
            circleRect.left = left.toFloat()
            circleRect.top = top.toFloat()
            circleRect.right = right.toFloat()
            circleRect.bottom = bottom.toFloat()

            circleRadius = circleRect.width() * 0.45f
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (circleColor == null) return

        circlePaint.style = Paint.Style.FILL
        circlePaint.color = circleColor!!
        canvas.drawCircle(circleRect.centerX() - circleRect.left, circleRect.centerY() - circleRect.top, circleRadius, circlePaint)

        circlePaint.style = Paint.Style.STROKE
        circlePaint.color = circleStrokeColor
        circlePaint.strokeWidth = circleStrokeWidth
        canvas.drawCircle(circleRect.centerX() - circleRect.left, circleRect.centerY() - circleRect.top, circleRadius, circlePaint)
    }

}