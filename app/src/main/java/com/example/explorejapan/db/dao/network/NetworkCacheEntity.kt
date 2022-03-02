package com.example.explorejapan.db.dao.network

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NetworkCacheEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "endpoint") var endpoint: String,
    @ColumnInfo(name = "result") var result: String
    //can be added pagination for future improvement if the endpoint support pagination
)