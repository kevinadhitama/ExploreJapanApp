package com.example.explorejapan.repository

import com.example.explorejapan.db.AppDatabase
import com.example.explorejapan.db.dao.network.NetworkCacheDAO
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class NetworkCacheDataSourceTest {

    @MockK(relaxUnitFun = true)
    lateinit var db: AppDatabase

    @MockK(relaxUnitFun = true)
    lateinit var networkCacheDao: NetworkCacheDAO

    private val dispatcher = Dispatchers.Unconfined

    @Test
    fun putCache() {
        //given
        val endpoint: String = "endpoint"
        coEvery {
            db.networkCacheDAO()
        } returns networkCacheDao
        coEvery {
            networkCacheDao.insert(any())
        } just runs
        coEvery {
            networkCacheDao.delete(any())
        } just runs

        val dataSource = NetworkCacheDataSource(
            mockk(),
            dispatcher,
            db
        )

        //when
        runBlocking {
            dataSource.putCache(
                endpoint = endpoint,
                result = mockk()
            )
        }

        //then
        verifySequence {
            networkCacheDao.delete(endpoint)
            networkCacheDao.insert(any())
        }
    }
}