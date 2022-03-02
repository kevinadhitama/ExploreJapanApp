package com.example.explorejapan.page.landing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider.Factory
import com.example.explorejapan.R
import com.example.explorejapan.page.landing.vm.LandingViewModelFactory

class LandingActivity : AppCompatActivity(R.layout.landing_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getDefaultViewModelProviderFactory(): Factory {
        return LandingViewModelFactory(this)
    }
}