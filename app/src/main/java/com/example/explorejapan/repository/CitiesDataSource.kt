package com.example.explorejapan.repository

import android.content.Context
import com.example.explorejapan.network.APIService
import com.example.explorejapan.network.CITIES_PATH
import com.example.explorejapan.network.CommonResponse
import com.example.explorejapan.network.model.CityCollection
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CitiesDataSource(
    private val context: Context,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiService: APIService = APIService.getInstance(),
    private val networkCacheDataSource: NetworkCacheDataSource = NetworkCacheDataSource(context)
) {

    suspend fun loadListCity(loadFromCache: Boolean = false): Response<List<CityCollection>> =
        withContext(defaultDispatcher) {
            try {
                val cache = loadCityFromCache()
                if (loadFromCache && cache != null) {
                    return@withContext CommonResponse.getSuccessResponse(
                        body = cache
                    )
                }

                val res = apiService.getCities()

                if (res.isSuccessful) {
                    res.body()?.let {
                        networkCacheDataSource.putCache(CITIES_PATH, it)
                    }
                    res
                } else {
                    loadCityFromCache(cache = cache, res = res)
                }
            } catch (ex: Exception) {
                loadCityFromCache(CommonResponse.getErrorResponse(message = ex.message))
            }
        }

    private fun loadCityFromCache(
        cache: List<CityCollection>?,
        res: Response<List<CityCollection>>
    ):
        Response<List<CityCollection>> {
        cache?.let {
            return Response.success(it)
        } ?: run {
            return res
        }
    }

    private suspend fun loadCityFromCache(res: Response<List<CityCollection>>):
        Response<List<CityCollection>> {
        loadCityFromCache()?.let {
            return Response.success(it)
        } ?: run {
            return res
        }
    }

    private suspend fun loadCityFromCache(): List<CityCollection>? {
        return networkCacheDataSource.loadCache(
            CITIES_PATH,
            object : TypeToken<List<CityCollection>>() {}.type
        )
    }
}