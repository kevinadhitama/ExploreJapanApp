package com.example.explorejapan.page.landing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.explorejapan.R
import com.example.explorejapan.page.landing.vm.LandingViewModelFactory

class LandingActivity : AppCompatActivity(R.layout.landing_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentContainer =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = fragmentContainer.navController
        NavigationUI.setupActionBarWithNavController(this, navController = navController)
    }

    override fun getDefaultViewModelProviderFactory(): Factory {
        return LandingViewModelFactory(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}