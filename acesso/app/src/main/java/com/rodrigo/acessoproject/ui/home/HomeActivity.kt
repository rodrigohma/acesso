package com.rodrigo.acessoproject.ui.home

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.rodrigo.acessoproject.R
import com.rodrigo.acessoproject.ui.document.AddDocumentActivity
import kotlinx.android.synthetic.main.activity_documents.*
import kotlinx.android.synthetic.main.botton_sheet.*
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rodrigo.acessoproject.data.model.User
import com.rodrigo.acessoproject.ui.document.DocumentActivity
import com.rodrigo.acessoproject.ui.user.UserActivity
import com.rodrigo.acessoproject.data.model.Document
import com.rodrigo.acessoproject.data.model.DocumentType
import com.rodrigo.acessoproject.app.MyApplication
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync


class HomeActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {

    private val documentsDao = MyApplication.database?.documentDAO()

    private var documents = listOf<Document>()

    private val user = User(1, "Nome da pessoa", "emaildapessoa@yahoo.com.br",
            "5511949593849", "Av. Berrini, 1001, Brooklyn, São Paulo",
            R.mipmap.woman)

    private var adapter : HomeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documents)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        setListeners()

        inflateUser()
    }

    override fun onResume() {
        requestDocuments()

        super.onResume()
    }

    /**
     * Method to request documents in database.
     */
    private fun requestDocuments() {
        doAsync {
            documentsDao?.getAll()?.let { request ->
                documents = request

                inflateDocuments()
            }
        }
    }

    /**
     * Method to set listeners in the views.
     */
    private fun setListeners() {
        floating_action_button.setOnClickListener{
            val view = layoutInflater.inflate(R.layout.botton_sheet, null)

            val dialog = BottomSheetDialog(this)
            dialog.setContentView(view)
            dialog.show()

            dialog.add_rg.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(this, AddDocumentActivity::class.java)
                startActivity(intent)
            }

            dialog.add_new_item1.setOnClickListener {
                dialog.dismiss()

                doAsync {
                    documentsDao?.insertAll(Document("New Item", DocumentType.NEW, "-1", "-1", "-1", "-1"))

                    requestDocuments()
                }
            }

            dialog.add_new_item2.setOnClickListener {
                dialog.dismiss()

                doAsync {
                    documentsDao?.insertAll(Document("New Item", DocumentType.NEW, "-1", "-1", "-1", "-1"))

                    requestDocuments()
                }
            }
        }

        image_user.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra("user", user)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, image_user , "user_photo")
            startActivity(intent, options.toBundle())
        }
    }

    /**
     * Method to inflate all documents in database.
     */
    private fun inflateDocuments() {
        runOnUiThread {
            loading_container.visibility = View.GONE

            adapter = HomeAdapter(documents, this, this)
            recycler_documents.layoutManager = LinearLayoutManager(this)
            recycler_documents.adapter = adapter
            adapter?.notifyDataSetChanged()

            if (documents.isEmpty()) {
                empty_container.visibility = View.VISIBLE
            } else {
                empty_container.visibility = View.GONE
            }
        }
    }

    /**
     * Method to inflate current user.
     */
    private fun inflateUser() {
        title_user.text = if (user.name.isNotEmpty()) user.name else getString(R.string.user_no_name)

        Glide.with(this)
                .load(user.photo)
                .apply(RequestOptions.circleCropTransform())
                .into(image_user)
    }

    override fun onClick(view: View?) {

        documents[recycler_documents.getChildLayoutPosition(view)].let { document ->

            if (document.type == DocumentType.NEW) return

            val intent = Intent(this, DocumentActivity::class.java)
            intent.putExtra("document", document)
            startActivity(intent)
        }

    }

    override fun onLongClick(view: View?): Boolean {
        documents[recycler_documents.getChildLayoutPosition(view)].let { document ->

            alert(R.string.delete_document) {
                positiveButton(R.string.yes) {
                    doAsync {
                        documentsDao?.delete(document)

                        runOnUiThread {
                            requestDocuments()
                        }
                    }
                }
                negativeButton(R.string.no) { }
            }.show()

        }

        return true
    }
}
