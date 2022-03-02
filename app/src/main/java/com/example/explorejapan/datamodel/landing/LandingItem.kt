package com.example.explorejapan.datamodel.landing

data class LandingItem(
    val title: String,
    val imageUrl: String? = null,
    val description: String? = null,
    val type: LandingItemType
)

enum class LandingItemType(val value: Int) {
    HEADER(0),
    CONTENT(1),
    LOADER(2);

    companion object {
        fun from(findValue: Int): LandingItemType = values().first { it.value == findValue }
    }
}