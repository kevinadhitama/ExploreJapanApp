package com.example.explorejapan.page.landing.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.explorejapan.repository.LandingRepository

class LandingViewModelFactory constructor(
    private val context: Context,
    private val repository: LandingRepository = LandingRepository(context)
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LandingViewModel::class.java)) {
            LandingViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}