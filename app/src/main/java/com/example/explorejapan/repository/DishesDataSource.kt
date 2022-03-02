package com.example.explorejapan.repository

import android.content.Context
import com.example.explorejapan.network.APIService
import com.example.explorejapan.network.CommonResponse
import com.example.explorejapan.network.DISHES_PATH
import com.example.explorejapan.network.model.DishCollection
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class DishesDataSource(
    private val context: Context,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiService: APIService = APIService.getInstance(),
    private val networkCacheDataSource: NetworkCacheDataSource = NetworkCacheDataSource(context)
) {

    suspend fun loadListDishes(loadFromCache: Boolean = false): Response<List<DishCollection>> =
        withContext(defaultDispatcher) {
            try {
                val cache = loadDishesFromCache()
                if (loadFromCache && cache != null) {
                    return@withContext CommonResponse.getSuccessResponse(
                        body = cache
                    )
                }

                val res = apiService.getDishes()

                if (res.isSuccessful) {
                    res.body()?.let {
                        networkCacheDataSource.putCache(DISHES_PATH, it)
                    }
                    res
                } else {
                    loadDishesFromCache(cache = cache, res = res)
                }
            } catch (ex: Exception) {
                loadDishesFromCache(CommonResponse.getErrorResponse(message = ex.message))
            }
        }

    private fun loadDishesFromCache(
        cache: List<DishCollection>?,
        res: Response<List<DishCollection>>
    ):
        Response<List<DishCollection>> {
        cache?.let {
            return Response.success(it)
        } ?: run {
            return res
        }
    }

    private suspend fun loadDishesFromCache(res: Response<List<DishCollection>>):
        Response<List<DishCollection>> {
        loadDishesFromCache()?.let {
            return Response.success(it)
        } ?: run {
            return res
        }
    }

    private suspend fun loadDishesFromCache(): List<DishCollection>? {
        return networkCacheDataSource.loadCache(
            DISHES_PATH,
            object : TypeToken<List<DishCollection>>() {}.type
        )
    }
}