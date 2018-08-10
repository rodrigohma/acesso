package com.rodrigo.acessoproject.ui.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rodrigo.acessoproject.R
import com.rodrigo.acessoproject.data.model.Document
import com.rodrigo.acessoproject.data.model.DocumentType
import kotlinx.android.synthetic.main.document_item.view.*
import org.jetbrains.anko.textResource

class HomeAdapter(private val documents: List<Document>, private val onClickListener: View.OnClickListener,
                  private val onLongClickListener: View.OnLongClickListener)
    : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(document: Document) {

            when (document.type) {
                DocumentType.RG -> itemView.text_title.textResource = R.string.document_rg

                DocumentType.NEW -> itemView.text_title.textResource = R.string.document_new_item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.document_item, parent, false)
        view.setOnClickListener(onClickListener)
        view.setOnLongClickListener(onLongClickListener)
        return ViewHolder(view)
    }

    override fun getItemCount() = documents.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(documents[position])

}
