package com.example.explorejapan.page.landing.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorejapan.R
import com.example.explorejapan.datamodel.landing.LandingItem
import com.example.explorejapan.datamodel.landing.LandingItemType
import com.example.explorejapan.network.CommonResponse
import com.example.explorejapan.page.landing.vm.LandingUiState.Empty
import com.example.explorejapan.page.landing.vm.LandingUiState.ErrorPage
import com.example.explorejapan.page.landing.vm.LandingUiState.ListLoading
import com.example.explorejapan.page.landing.vm.LandingUiState.Loading
import com.example.explorejapan.page.landing.vm.LandingUiState.Toast
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

    var selectedItem: LandingItem? = null

    fun reloadLanding(loadFromCache: Boolean = false, showLoading: Boolean = true) {
        _uiState.value = Loading(showLoading && !loadFromCache)
        viewModelScope.launch(dispatcher) {
            val citiesCount = initCities(loadFromCache = loadFromCache, isAddition = false)
            if (citiesCount > 0) _uiState.value = Loading(false)

            val dishesCount = initDishes(loadFromCache = loadFromCache)

            delay(100L)
            _uiState.value = ListLoading(isLoading = false)
            delay(100L)
            _uiState.value = Loading(false)

            if (citiesCount == 0 && dishesCount == 0) {
                _uiState.value = Empty
            }

            if (citiesCount == EMPTY_DUE_ERROR && dishesCount == EMPTY_DUE_ERROR) {
                _uiState.value = ErrorPage
            }
        }
    }

    // my consideration for not combining the result from cities and dishes on repository is
    // because of the support for future improvement
    // in this case init cities and init dishes might be look similar, for future case with
    // another api call for each section with different data model might be more handy this approach
    // also by using this approach, we can show the result to users parallel-ly without let them
    // waiting too long for every api call is done
    // basically its all depends on the product requirements itself
    private suspend fun initCities(
        loadFromCache: Boolean = false,
        isAddition: Boolean = true
    ): Int {
        val result = repository.loadListCities(loadFromCache = loadFromCache)
        if (!result.isSuccessful) {
            _uiState.value = LandingUiState.Error(Throwable(result.message()))
            return EMPTY_DUE_ERROR
        }

        val res = result.body()
        res?.let {
            val data = listOf(
                LandingItem(
                    title = context.get()?.getString(
                        R.string.section_title_main_cities_japan
                    ) ?: "",
                    type = LandingItemType.HEADER
                )
            ).plus(res.map {
                LandingItem(
                    title = it.name,
                    imageUrl = it.image,
                    description = it.description,
                    type = LandingItemType.CONTENT
                )
            })

            _uiState.value = LandingUiState.Success(
                data = data,
                addition = isAddition
            )
            delay(100L)
            _uiState.value = ListLoading(isLoading = true)

            delay(100L)
            showCacheMessageToast(result.raw())
            return data.count()
        }

        return 0
    }

    private suspend fun initDishes(
        loadFromCache: Boolean = false,
        isAddition: Boolean = true
    ): Int {
        val result = repository.loadListDishes(loadFromCache = loadFromCache)
        if (!result.isSuccessful) {
            _uiState.value = LandingUiState.Error(Throwable(result.message()))
            return EMPTY_DUE_ERROR
        }

        val res = result.body()
        res?.let {
            val data = listOf(
                LandingItem(
                    title = context.get()?.getString(
                        R.string.section_title_popular_japanese_food
                    ) ?: "",
                    type = LandingItemType.HEADER
                )
            ).plus(res.map {
                LandingItem(
                    title = it.name,
                    imageUrl = it.image,
                    type = LandingItemType.CONTENT
                )
            })

            _uiState.value = LandingUiState.Success(
                data = data,
                addition = isAddition
            )
            delay(100L)
            _uiState.value = ListLoading(isLoading = true)

            showCacheMessageToast(result.raw())
            return data.count()
        }

        return 0
    }

    private suspend fun showCacheMessageToast(rawResponse: okhttp3.Response) {
        if (CommonResponse.isResponseFromCache(rawResponse = rawResponse)) {
            delay(100L)
            _uiState.value = Toast(
                context.get()?.getString(R.string.toast_message_data_from_cache) ?: ""
            )
        }
    }
}

sealed class LandingUiState {
    data class Success(val data: List<LandingItem>, val addition: Boolean = true) : LandingUiState()
    //Loading: only for state when no result yet retrieved from api (blocking the users)
    data class Loading(val isLoading: Boolean) : LandingUiState()
    //ListLoading: only for showing loading for single api call (will not blocking the users)
    data class ListLoading(val isLoading: Boolean) : LandingUiState()
    //Empty: only for showing empty state caused by no data at all returned from api
    object Empty : LandingUiState()
    //Error: only for single api error (will not blocking the users)
    data class Error(val exception: Throwable) : LandingUiState()
    //ErrorPage: only for all api error (blocking the users since no result to show anyway)
    object ErrorPage : LandingUiState()
    //Toast: currently showing toast for the sake of we know that its from cache, might be
    // annoying to see it keep showing
    data class Toast(val text: String) : LandingUiState()
}