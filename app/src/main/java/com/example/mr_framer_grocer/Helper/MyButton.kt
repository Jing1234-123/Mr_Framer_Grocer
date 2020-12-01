package com.example.mr_framer_grocer.Helper

import android.graphics.*

class MyButton(private val text: String, private val imageResId: Int,
               private val color: Int, private val clickListener: MyButtonClickListener) {
    private var pos = 0
    private var clickRegion: RectF? = null

    fun onClick(x: Float, y: Float): Boolean {
        if (clickRegion != null && clickRegion!!.contains(x, y)) {
            clickListener.onClick(pos)
            return true
        }
        return false
    }

    fun onDraw(c: Canvas, rect: RectF, pos: Int) {
        val p = Paint()
        p.isAntiAlias = true
        p.isSubpixelText = true
        val corners = 20f
        // Draw background
        p.color = color
        c.drawRoundRect(rect,corners,corners,p)

        // Draw Text
        p.color = Color.WHITE
        val textSize = 35f
        p.textSize = textSize
        val r = Rect()
        val cHeight = rect.height()
        val cWidth = rect.width()
        p.textAlign = Paint.Align.LEFT
        p.getTextBounds(text, 0, text.length, r)
        val x: Float = cWidth / 2f - r.width() / 2f - r.left
        val y: Float = cHeight / 2f + r.height() / 2f - r.bottom
        c.drawText(text, rect.left + x, rect.top + y, p)

        clickRegion = rect
        this.pos = pos
    }

}

