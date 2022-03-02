package com.example.explorejapan.db.dao.network

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NetworkCacheDAO {

    @Query("SELECT * FROM NetworkCacheEntity WHERE endpoint LIKE :endpoint")
    fun findByEndpoint(endpoint: String): List<NetworkCacheEntity>

    @Insert
    fun insert(vararg networkCacheEntity: NetworkCacheEntity)

    @Query("DELETE FROM NetworkCacheEntity WHERE endpoint LIKE :endpoint")
    fun delete(endpoint: String)

    @Update
    fun updateTodo(vararg todos: NetworkCacheEntity)
}