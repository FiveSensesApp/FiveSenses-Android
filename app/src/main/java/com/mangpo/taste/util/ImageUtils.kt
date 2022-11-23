package com.mangpo.taste.util

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment`
import android.provider.MediaStore
import android.view.View
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

fun getBitmapFromView(width: Int, height: Int, view: View, canvasColor: Int): Bitmap? {
    //Define a bitmap with the same size as the view
    val returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    //Bind a canvas to it
    val canvas = Canvas(returnedBitmap)
    canvas.drawColor(canvasColor)

    // draw the view on the canvas
    view.draw(canvas)

    return returnedBitmap
}

fun saveImage(bitmap: Bitmap, context: Context, folderName: String, callback: (Boolean) -> Unit) {
    try {
        if (Build.VERSION.SDK_INT >= 29) {  // RELATIVE_PATH and IS_PENDING are introduced in API 29.
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
            values.put(MediaStore.Images.Media.IS_PENDING, true)

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
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
        }

        callback.invoke(true)
    } catch (e: Exception) {
        e.printStackTrace()
        callback.invoke(false)
    }
}

fun getImageUri(inContext: Context?, inImage: Bitmap?): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage?.compress(Bitmap.CompressFormat.PNG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(inContext?.contentResolver, inImage, "Title" + " - " + Calendar.getInstance().time, null)
    return Uri.parse(path)
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