package com.example.explorejapan.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.explorejapan.db.dao.network.NetworkCacheDAO
import com.example.explorejapan.db.dao.network.NetworkCacheEntity

@Database(entities = [NetworkCacheEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun networkCacheDAO(): NetworkCacheDAO

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "main-app.db"
        ).build()
    }
}