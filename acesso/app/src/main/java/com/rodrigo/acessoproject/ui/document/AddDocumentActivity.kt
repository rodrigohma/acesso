package com.rodrigo.acessoproject.ui.document

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.rodrigo.acessoproject.R
import com.rodrigo.acessoproject.data.dao.ImageDAO
import com.rodrigo.acessoproject.data.model.Document
import com.rodrigo.acessoproject.data.model.DocumentType
import com.rodrigo.acessoproject.utils.MaskUtil
import com.rodrigo.acessoproject.utils.Utils
import kotlinx.android.synthetic.main.activity_add_document.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.design.snackbar
import android.text.InputType
import android.os.Build
import com.rodrigo.acessoproject.app.MyApplication
import com.rodrigo.acessoproject.ui.camera.CameraActivity
import android.app.Activity
import android.view.inputmethod.InputMethodManager


class AddDocumentActivity : AppCompatActivity() {

    private val EDIT_DOCUMENT = 1

    private val REQUEST_CAMERA_PERMISSION = 0

    private val REQUEST_PHOTO_FRONT = 1

    private val REQUEST_PHOTO_BACK = 2

    private val documentDAO = MyApplication.database?.documentDAO()

    private val imageDAO = ImageDAO()

    private var documentFront = ""

    private var documentBack = ""

    private var documentEditing : Document? = null

    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_document)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        if (intent.hasExtra("document")) {
            intent.extras.getSerializable("document").let { document ->
                isEdit = true
                inflateDocument(document as Document)
            }
        }

        setListeners()
    }

    /**
     * Method to inflate current document.
     *
     * @param document the document instance.
     */
    private fun inflateDocument(document: Document) {
        documentEditing = document
        documentFront = document.front
        documentBack = document.back

        edit_full_name.setText(document.name)
        edit_rg_number.setText(document.number)
        edit_date.setText(document.date)

        button_photo_front.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_camera, 0, R.mipmap.ic_check, 0)
        button_photo_back.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_camera, 0, R.mipmap.ic_check, 0)
    }

    /**
     * Method to set listeners in views.
     */
    private fun setListeners() {
        edit_full_name.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        edit_rg_number.addTextChangedListener(MaskUtil.mask(edit_rg_number, MaskUtil.FORMAT_RG))
        edit_date.addTextChangedListener(MaskUtil.mask(edit_date, MaskUtil.FORMAT_DATE))

        button_save.setOnClickListener(saveDocument)

        button_photo_front.setOnClickListener {
            if(documentFront.isBlank()) {
                verifyPermission(REQUEST_PHOTO_FRONT)
            } else {
                alert(R.string.overwrite_photo) {
                    positiveButton(R.string.yes) {
                        verifyPermission(REQUEST_PHOTO_FRONT)
                    }
                    negativeButton(R.string.no) { }
                }.show()
            }
        }

        button_photo_back.setOnClickListener {
            if(documentBack.isBlank()) {
                verifyPermission(REQUEST_PHOTO_BACK)
            } else {
                alert(R.string.overwrite_photo) {
                    positiveButton(R.string.yes) {
                        verifyPermission(REQUEST_PHOTO_BACK)
                    }
                    negativeButton(R.string.no) { }
                }.show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    /**
     * Method to verify permissions app.
     *
     * @param request the request permission id.
     */
    private fun verifyPermission(request: Int) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA_PERMISSION)
        } else {
            openCamera(request)
        }
    }

    /**
     * Method to call camera activity.
     */
    private fun openCamera(request: Int) {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("request", request)
        startActivityForResult(intent, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            data?.let { dataPhoto ->

                val photo = dataPhoto.extras.get("data") as Bitmap

                when (requestCode) {
                    REQUEST_PHOTO_FRONT -> {
                        documentFront = imageDAO.saveImage(this, Utils.generateName(), photo)
                        button_photo_front.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_camera, 0, R.mipmap.ic_check, 0)
                    }

                    REQUEST_PHOTO_BACK -> {
                        documentBack = imageDAO.saveImage(this, Utils.generateName(), photo)
                        button_photo_back.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_camera, 0, R.mipmap.ic_check, 0)
                    }
                }
            }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Method to save document in database.
     */
    private val saveDocument = View.OnClickListener {
        val fullName = edit_full_name.text.toString()
        val rgNumber = edit_rg_number.text.toString()
        val birthDate = edit_date.text.toString()

        if (fullName.isBlank()) {
            snackbar(edit_full_name, R.string.snack_enter_name)
            return@OnClickListener
        }

        if (rgNumber.isBlank()) {
            snackbar(edit_rg_number, R.string.snack_enter_rg_number)
            return@OnClickListener
        } else if (rgNumber.length != 10) {
            snackbar(edit_rg_number, R.string.snack_valid_rg_number)
            return@OnClickListener
        }

        if (birthDate.isBlank()) {
            snackbar(edit_date, R.string.snack_enter_birth_day)
            return@OnClickListener
        } else if (birthDate.length != 10) {
            snackbar(edit_rg_number, R.string.snack_valid_birth_day)
            return@OnClickListener
        }

        if (documentFront.isBlank()) {
            snackbar(edit_date, R.string.snack_enter_photo_front)
            return@OnClickListener
        }

        if (documentBack.isBlank()) {
            snackbar(edit_date, R.string.snack_enter_photo_back)
            return@OnClickListener
        }

        val document = Document(fullName, DocumentType.RG ,rgNumber, birthDate, documentFront, documentBack)

        if (isEdit) {
            documentEditing?.id?.let { id ->
                document.id = id
                documentDAO?.updateAll(document)
                val intent = Intent()
                intent.putExtra("document", document)
                setResult(EDIT_DOCUMENT, intent)
                finish()
            }
        } else {
            documentDAO?.insertAll(document)
            finish()
        }
    }

}
