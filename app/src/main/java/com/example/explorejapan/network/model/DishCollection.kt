package com.example.explorejapan.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DishCollection(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String
) : Parcelable