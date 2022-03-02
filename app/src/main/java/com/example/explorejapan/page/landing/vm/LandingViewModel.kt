package com.example.explorejapan.page.landing.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorejapan.R
import com.example.explorejapan.datamodel.landing.LandingItem
import com.example.explorejapan.page.landing.vm.LandingUiState.Empty
import com.example.explorejapan.page.landing.vm.LandingUiState.ErrorPage
import com.example.explorejapan.page.landing.vm.LandingUiState.ListLoading
import com.example.explorejapan.page.landing.vm.LandingUiState.Loading
import com.example.explorejapan.repository.LandingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

const val EMPTY_DUE_ERROR = -1

class LandingViewModel(
    private val context: WeakReference<Context>,
    private val repository: LandingRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<LandingUiState>(Loading(true))
    val uiState: StateFlow<LandingUiState> = _uiState

    init {
        reloadLanding()
    }

    fun reloadLanding() {
        viewModelScope.launch(dispatcher) {
            val citiesCount = initCities(isAddition = false)
            if (citiesCount > 0) _uiState.value = Loading(false)

            val dishesCount = initDishes()


            ListLoading(isLoading = false)
            delay(100L)
            Loading(false)

            if (citiesCount == 0 && dishesCount == 0) {
                _uiState.value = Empty
            }

            if (citiesCount == EMPTY_DUE_ERROR && dishesCount == EMPTY_DUE_ERROR) {
                _uiState.value = ErrorPage
            }
        }
    }

    private suspend fun initCities(isAddition: Boolean = true): Int {
        val result = repository.loadListCities()
        if (!result.isSuccessful) {
            _uiState.value = LandingUiState.Error(Throwable(result.message()))
            return EMPTY_DUE_ERROR
        }

        val res = result.body()
        res?.let {
            val data = listOf(
                LandingItem(
                    isHeader = true,
                    title = context.get()?.getString(
                        R.string.section_title_main_cities_japan
                    ) ?: ""
                )
            ).plus(res.map {
                LandingItem(
                    title = it.name,
                    imageUrl = it.image,
                    description = it.description
                )
            })

            _uiState.value = LandingUiState.Success(
                data = data,
                addition = isAddition
            )
            delay(100L)
            _uiState.value = ListLoading(isLoading = true)
            return data.count()
        }

        return 0
    }

    private suspend fun initDishes(isAddition: Boolean = true): Int {
        val result = repository.loadListDishes()
        if (!result.isSuccessful) {
            _uiState.value = LandingUiState.Error(Throwable(result.message()))
            return EMPTY_DUE_ERROR
        }

        val res = result.body()
        res?.let {
            val data = listOf(
                LandingItem(
                    isHeader = true,
                    title = context.get()?.getString(
                        R.string.section_title_popular_japanese_food
                    ) ?: ""
                )
            ).plus(res.map {
                LandingItem(
                    title = it.name,
                    imageUrl = it.image
                )
            })

            _uiState.value = LandingUiState.Success(
                data = data,
                addition = isAddition
            )
            delay(100L)
            _uiState.value = ListLoading(isLoading = true)

            return data.count()
        }

        return 0
    }
}

sealed class LandingUiState {
    data class Success(val data: List<LandingItem>, val addition: Boolean = true) : LandingUiState()
    data class Loading(val isLoading: Boolean) : LandingUiState()
    data class ListLoading(val isLoading: Boolean) : LandingUiState()
    object Empty : LandingUiState()
    data class Error(val exception: Throwable) : LandingUiState()
    object ErrorPage : LandingUiState()
}