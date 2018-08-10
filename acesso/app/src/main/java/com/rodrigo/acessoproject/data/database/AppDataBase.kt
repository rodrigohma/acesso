package com.rodrigo.acessoproject.data.database

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import com.rodrigo.acessoproject.data.dao.DocumentDAO
import com.rodrigo.acessoproject.data.model.Document

@Database(version = 1, entities = arrayOf(Document::class))
abstract class AppDataBase : RoomDatabase() {
    abstract fun documentDAO(): DocumentDAO
}