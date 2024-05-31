package com.example.messenger.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import android.graphics.*

class CircleImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable ?: return

        if (width == 0 || height == 0) {
            return
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val scale = width.toFloat() / drawable.intrinsicWidth.coerceAtLeast(drawable.intrinsicHeight)
        drawable.setBounds(0, 0, (drawable.intrinsicWidth * scale).toInt(), (drawable.intrinsicHeight * scale).toInt())
        drawable.draw(canvas)

        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        paint.shader = shader

        val radius = width.coerceAtMost(height) / 2f
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }
}