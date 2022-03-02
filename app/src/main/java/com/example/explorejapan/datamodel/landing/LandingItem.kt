package com.example.explorejapan.datamodel.landing

data class LandingItem(
    val isHeader: Boolean = false,
    val title: String,
    val imageUrl: String? = null,
    val description: String? = null
)
