package com.rodrigo.acessoproject.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


@Entity
class Document(
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "type") val type: Int,
        @ColumnInfo(name = "number") val number: String,
        @ColumnInfo(name = "date") val date: String,
        @ColumnInfo(name = "front") val front: String,
        @ColumnInfo(name = "back") val back: String): Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

data class User(
        val uid: Int,
        val name: String,
        val email: String,
        val phone: String,
        val address: String,
        val photo: Int
        ) : Serializable

object DocumentType {
    const val RG = 1
    const val NEW = 2
}