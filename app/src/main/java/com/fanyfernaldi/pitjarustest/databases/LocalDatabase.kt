package com.fanyfernaldi.pitjarustest.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fanyfernaldi.pitjarustest.caches.StoreCache
import com.fanyfernaldi.pitjarustest.daos.StoreDao

@Database(entities = [StoreCache::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao

    companion object {

        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "local_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}