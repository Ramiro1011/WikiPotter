package com.example.wikipotter.utis
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

object BlurBuilder {
    private const val BITMAP_SCALE = 0.4f
    private const val BLUR_RADIUS = 10f

    fun blur(context: Context, image: Bitmap): Bitmap {
        val width = Math.round(image.width * BITMAP_SCALE)
        val height = Math.round(image.height * BITMAP_SCALE)

        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)

        val rs = RenderScript.create(context)
        val intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        intrinsicBlur.setRadius(BLUR_RADIUS)
        intrinsicBlur.setInput(tmpIn)
        intrinsicBlur.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)

        return outputBitmap
    }

    fun blur(context: Context, image: Drawable): Bitmap {
        val bitmap = drawableToBitmap(image)
        return blur(context, bitmap)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}