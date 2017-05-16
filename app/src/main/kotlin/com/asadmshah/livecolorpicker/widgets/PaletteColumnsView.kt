package com.asadmshah.livecolorpicker.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.asadmshah.livecolorpicker.models.ColorList

class PaletteColumnsView : View {

    var colorList: ColorList? = null
        set(value) {
            field = value
            invalidate()
        }

    private val paint: Paint = Paint()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {

    }

    override fun onDraw(canvas: Canvas) {
        if (colorList == null || (canvas.width == 0 || canvas.height == 0)) return

        val w = canvas.width.toFloat() / colorList!!.size.toFloat()
        val h = (canvas.height).toFloat()

        for (i in 0 until colorList!!.size) {
            paint.color = colorList!![i].rgb
            canvas.drawRect(i * w, 0f, i * w + w, h, paint)
        }
    }
}