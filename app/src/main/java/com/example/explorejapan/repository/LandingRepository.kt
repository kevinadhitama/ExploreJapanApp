package com.example.explorejapan.repository

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LandingRepository(
    private val context: Context,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val citiesDataSource: CitiesDataSource = CitiesDataSource(context),
    private val dishesDataSource: DishesDataSource = DishesDataSource(context)
) {

    suspend fun loadListCities() = withContext(defaultDispatcher) {
        citiesDataSource.loadListCity()
    }

    suspend fun loadListDishes() = withContext(defaultDispatcher) {
        dishesDataSource.loadListDishes()
    }
}