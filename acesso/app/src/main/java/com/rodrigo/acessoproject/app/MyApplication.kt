package com.rodrigo.acessoproject.app

import android.app.Application
import android.arch.persistence.room.Room
import com.rodrigo.acessoproject.data.database.AppDataBase

/**
 * Custom application to create a instance of db.
 */
open class MyApplication : Application() {

    /**
     * Instance of database.
     */
    companion object {
        var database: AppDataBase? = null
    }


    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, AppDataBase::class.java, "my-db").allowMainThreadQueries().build()
    }
}