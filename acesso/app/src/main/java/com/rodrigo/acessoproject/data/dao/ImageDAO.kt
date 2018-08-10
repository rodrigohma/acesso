package com.rodrigo.acessoproject.data.dao

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.ContextWrapper
import com.rodrigo.acessoproject.utils.Utils
import java.io.*


class ImageDAO {

    /**
     * Method to save image on internal storage
     *
     * @param context the current context.
     * @param receive the name of image.
     * @param bitmap the image.
     */
    fun saveImage(context: Context, name: String, bitmap: Bitmap): String {
        val cw = ContextWrapper(context)

        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)

        val path = File(directory, Utils.generateName() + ".jpg")

        val fos = FileOutputStream(path)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()

        return path.absolutePath
    }

    /**
     * Method to load image.
     *
     * @param path the image path.
     */
    fun loadImage(path: String): Bitmap {
        val file = File(path)
        return BitmapFactory.decodeStream(FileInputStream(file))
    }
}