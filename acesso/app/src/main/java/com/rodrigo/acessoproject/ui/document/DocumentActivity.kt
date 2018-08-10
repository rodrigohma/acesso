package com.rodrigo.acessoproject.ui.document

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.rodrigo.acessoproject.R
import com.rodrigo.acessoproject.data.dao.ImageDAO
import com.rodrigo.acessoproject.data.model.Document
import kotlinx.android.synthetic.main.activity_document.*

class DocumentActivity : AppCompatActivity() {

    private val EDIT_DOCUMENT = 1

    private var document: Document? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra("document")) {
            intent.extras.getSerializable("document")?.let { document ->
                inflateDocument(document as Document)
            }
        }

        setListeners()
    }

    private fun setListeners() {
        document_edit.setOnClickListener {
            val intent = Intent(this, AddDocumentActivity::class.java)
            intent.putExtra("document", document)
            startActivityForResult(intent, EDIT_DOCUMENT)
        }
    }

    private fun inflateDocument(document: Document) {
        this.document = document

        document_name.text = if (document.name.isNotEmpty()) document.name else getString(R.string.activity_no_name)

        document_number.text = if (document.number.isNotEmpty()) document.number else getString(R.string.activity_no_number)

        document_date.text = if (document.date.isNotEmpty()) document.date else getString(R.string.activity_no_date)

        Glide.with(this).load(ImageDAO().loadImage(document.front)).into(document_image_front)

        Glide.with(this).load(ImageDAO().loadImage(document.back)).into(document_image_back)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if(resultCode == Activity.RESULT_OK){
                inflateDocument(data?.getSerializableExtra("document") as Document)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
