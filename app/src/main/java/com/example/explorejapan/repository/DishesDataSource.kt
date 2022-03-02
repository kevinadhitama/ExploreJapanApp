package com.example.explorejapan.repository

import android.content.Context
import com.example.explorejapan.network.APIService
import com.example.explorejapan.network.CITIES_PATH
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

    suspend fun loadListDishes(): Response<List<DishCollection>> = withContext(defaultDispatcher) {
        try {
            val res = apiService.getDishes()

            if (res.isSuccessful) {
                res.body()?.let {
                    networkCacheDataSource.putCache(DISHES_PATH, it)
                }
                res
            } else {
                loadDishesFromCache(res)
            }
        } catch (ex: Exception) {
            loadDishesFromCache(CommonResponse.getErrorResponse(message = ex.message))
        }
    }

    private suspend fun loadDishesFromCache(res: Response<List<DishCollection>>):
        Response<List<DishCollection>> {
        networkCacheDataSource.loadCache<List<DishCollection>>(
            DISHES_PATH,
            object : TypeToken<List<DishCollection>>() {}.type
        )?.let {
            return Response.success(it)
        } ?: run {
            return res
        }
    }
}