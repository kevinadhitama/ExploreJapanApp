package com.example.explorejapan.repository

import android.content.Context
import com.example.explorejapan.db.AppDatabase
import com.example.explorejapan.db.dao.network.NetworkCacheEntity
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

class NetworkCacheDataSource(
    private val context: Context,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val db: AppDatabase = AppDatabase(context)
) {

    suspend fun putCache(endpoint: String, result: Any) = withContext(defaultDispatcher) {
        val gson = GsonBuilder().create()

        db.networkCacheDAO().delete(endpoint)
        db.networkCacheDAO().insert(
            NetworkCacheEntity(
                endpoint = endpoint,
                result = gson.toJson(result)
            )
        )
    }

    suspend fun <T : Any> loadCache(endpoint: String, clazzType: Type): T? =
        withContext(defaultDispatcher) {
            val gson = GsonBuilder().create()
            val data = db.networkCacheDAO().findByEndpoint(endpoint).firstOrNull()
            gson.fromJson(data?.result, clazzType)
        }
}