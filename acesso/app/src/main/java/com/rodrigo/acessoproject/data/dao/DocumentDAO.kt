package com.rodrigo.acessoproject.data.dao

import android.arch.persistence.room.Dao
import com.rodrigo.acessoproject.data.model.Document
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update


@Dao
interface DocumentDAO {

    @Query("SELECT * FROM document")
    fun getAll(): List<Document>

    @Query("SELECT * FROM document WHERE id IN (:documentsIds)")
    fun loadAllByIds(documentsIds: IntArray): List<Document>

    @Insert
    fun insertAll(vararg documents: Document)

    @Update
    fun updateAll(vararg documents: Document)

    @Delete
    fun delete(document: Document)
}