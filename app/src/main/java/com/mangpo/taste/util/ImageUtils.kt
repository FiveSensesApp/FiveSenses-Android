package com.mangpo.taste.util

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun getBitmapFromView(width: Int, height: Int, view: View, canvasColor: Int): Bitmap? {
    //Define a bitmap with the same size as the view
    val returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    //Bind a canvas to it
    val canvas = Canvas(returnedBitmap)
    canvas.drawColor(canvasColor)

    /*//Bitmap 에 라운드 주기
    val paint = Paint()
    val rect = Rect(0, 0, returnedBitmap.width, returnedBitmap.height)
    val rectF = RectF(rect)
    val roundPx = 50f
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(returnedBitmap, rect, rect, paint)*/

    // draw the view on the canvas
    view.draw(canvas)

    return returnedBitmap
}

fun saveImage(bitmap: Bitmap, context: Context, folderName: String, callback: (Boolean, Uri?) -> Unit) {
    var uri: Uri? = null

    try {
        if (Build.VERSION.SDK_INT >= 29) {  // RELATIVE_PATH and IS_PENDING are introduced in API 29.
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
            values.put(MediaStore.Images.Media.IS_PENDING, true)

            uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
        } else {    // getExternalStorageDirectory is deprecated in API 29
            val directory = File(Environment.getExternalStorageDirectory().toString() + File.separator + folderName)

            if (!directory.exists()) {
                directory.mkdirs()
            }

            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))

            if (file.absolutePath != null) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath) // .DATA is deprecated in API 29
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }

            uri = file.toUri()
        }

        callback.invoke(true, uri)
    } catch (e: Exception) {
        e.printStackTrace()
        callback.invoke(false, uri)
    }
}

private fun contentValues() : ContentValues {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())

    return values
}

private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
    if (outputStream != null) {
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}