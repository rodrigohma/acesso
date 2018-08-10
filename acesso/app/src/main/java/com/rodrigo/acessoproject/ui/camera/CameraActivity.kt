package com.rodrigo.acessoproject.ui.camera


import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import com.rodrigo.acessoproject.R
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.IOException
import android.hardware.Camera
import android.graphics.BitmapFactory
import android.graphics.Matrix


class CameraActivity : AppCompatActivity(), SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback {

    private var camera: android.hardware.Camera? = null

    private var request: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        if (intent.hasExtra("request")) {
            request = intent.getIntExtra("request", 1)
        }

        surface_view.holder.addCallback(this)

        setListeners()
    }

    /**
     * Method to set listeners in the views.
     */
    private fun setListeners() {
        take_picture.setOnClickListener {
            takePicture(this, this, this)
        }

        camera_cancel.setOnClickListener {
            onBackPressed()
        }
    }

    /**
     * Method to take a picture.
     *
     * @param shutter callback of shutter.
     * @param raw callback of picture
     * @param jpeg callback of picture
     */
    private fun takePicture(shutter: Camera.ShutterCallback, raw: Camera.PictureCallback, jpeg: Camera.PictureCallback) {
        camera?.takePicture(shutter, raw, jpeg)
    }

    override fun onShutter() {

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

        if (camera != null) {
            try {
                camera?.setPreviewDisplay(surface_view.holder)
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        stop()
        camera?.release()
        camera = null
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        camera = android.hardware.Camera.open()
        camera?.setDisplayOrientation(90)
    }

    override fun onPictureTaken(data: ByteArray?, camera: Camera?) {

        data?.let {
            var bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

            val startWidth = bitmap.width/8
            val startHeight = (bitmap.height/8) * 2

            val sizeWidth = (bitmap.width/8) * 4
            val sizeHeight = (bitmap.width/8) * 4

            bitmap = Bitmap.createBitmap(bitmap, startWidth,startHeight, sizeWidth, sizeHeight)

            bitmap = bitmap.rotate(90)

            val intent = Intent()
            intent.putExtra("data", bitmap)
            setResult(request, intent)
            finish()
        }
    }

    /**
     * Method to start camera preview.
     */
    private fun start() {
        camera?.startPreview()
    }

    /**
     * Method to stop camera preview.
     */
    private fun stop() {
        camera?.stopPreview()
    }

    /**
     * Method to rotate bitmap.
     *
     * @param degrees value of rotate in degrees.
     */
    private fun Bitmap.rotate(degrees: Int): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

}
