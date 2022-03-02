package com.example.explorejapan.network

import com.example.explorejapan.network.model.CityCollection
import com.example.explorejapan.network.model.DishCollection
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

const val API_PATH = "https://api.npoint.io/"
const val CITIES_PATH = "e81570e822b273f0a366"
const val DISHES_PATH = "b4dd0d44343f7eb08f9c"

interface APIService {

    @GET(CITIES_PATH)
    suspend fun getCities(): Response<List<CityCollection>>

    @GET(DISHES_PATH)
    suspend fun getDishes(): Response<List<DishCollection>>

    companion object {

        var apiService: APIService? = null
        fun getInstance(): APIService {
            val client = OkHttpClient.Builder()
            client.connectTimeout(15, TimeUnit.SECONDS)
            client.readTimeout(15, TimeUnit.SECONDS)
            client.writeTimeout(15, TimeUnit.SECONDS)

            if (apiService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(API_PATH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build()
                apiService = retrofit.create(APIService::class.java)

            }
            return apiService!!
        }
    }
}