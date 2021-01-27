package com.mirkamalg.edvapp.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mirkamalg.edvapp.local.daos.ChequeDatabaseDAO
import com.mirkamalg.edvapp.model.entities.ChequeEntity
import com.mirkamalg.edvapp.util.CHEQUES_DATABASE_NAME

/**
 * Created by Mirkamal on 25 January 2021
 */

@Database(entities = [ChequeEntity::class], version = 1, exportSchema = false)
abstract class ChequesDatabase : RoomDatabase() {

    abstract val chequeDatabaseDAO: ChequeDatabaseDAO

    companion object {

        @Volatile
        private var INSTANCE: ChequesDatabase? = null

        fun getInstance(context: Context): ChequesDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChequesDatabase::class.java,
                        CHEQUES_DATABASE_NAME
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}