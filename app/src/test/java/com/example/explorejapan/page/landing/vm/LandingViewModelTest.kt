package com.example.explorejapan.page.landing.vm

import com.example.explorejapan.network.model.CityCollection
import com.example.explorejapan.repository.LandingRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import retrofit2.Response
import java.util.stream.Stream

@ExtendWith(MockKExtension::class)
class LandingViewModelTest {

    @MockK(relaxUnitFun = true)
    lateinit var repository: LandingRepository

    @MockK(relaxUnitFun = true)
    private lateinit var mockCities: Response<List<CityCollection>>

    private val dispatcher = Dispatchers.Unconfined

    @BeforeEach
    fun setUp() {
        coEvery {
            repository.loadListCities(any())
        } returns mockCities

        mockkConstructor(LandingViewModel::class)
        coEvery { anyConstructed<LandingViewModel>().showCacheMessageToast(any()) } just runs
    }

    @Test
    fun `load city with success loading cities`() = runBlocking {
        //given
        coEvery { mockCities.isSuccessful } returns true
        coEvery { mockCities.body() } returns listOf(
            CityCollection("", "", ""),
            CityCollection("", "", "")
        )
        coEvery { mockCities.raw() } returns mockk()

        val vm = LandingViewModel(
            context = mockk(),
            repository = repository,
            dispatcher = dispatcher
        )

        val listState = mutableListOf<LandingUiState>()
        val job = launch {
            vm.uiState.toList(listState)
        }
        //when
        val resultCount = vm.initCities()

        //then
        Assert.assertTrue(listState[0] is LandingUiState.Success)
        Assert.assertTrue(listState[1] is LandingUiState.ListLoading)
        Assert.assertEquals(2, listState.size)
        //1 from title section, 2 from mocked city, total 3 items returned
        Assert.assertEquals(3, resultCount)
        job.cancel()
    }

    @ParameterizedTest
    @MethodSource(
        "com.example.explorejapan.page.landing.vm.LandingViewModelTest" +
            "#provideArgumentsForEmptyBodyCitiesTest"
    )
    fun `load city with success but no cities returned`(
        cityCollections: List<CityCollection>?
    ) = runBlocking {
        //given
        coEvery { mockCities.isSuccessful } returns true
        coEvery { mockCities.body() } returns cityCollections
        coEvery { mockCities.raw() } returns mockk()

        val vm = LandingViewModel(
            context = mockk(),
            repository = repository,
            dispatcher = dispatcher
        )

        val listState = mutableListOf<LandingUiState>()
        val job = launch {
            vm.uiState.toList(listState)
        }
        //when
        val resultCount = vm.initCities()

        //then
        Assert.assertEquals(0, listState.size)
        Assert.assertEquals(0, resultCount)

        job.cancel()
    }

    @Test
    fun `load city with unsuccessful loading cities`() = runBlocking {
        //given
        coEvery { mockCities.isSuccessful } returns false
        coEvery { mockCities.message() } returns "errorMessage"

        val vm = LandingViewModel(
            context = mockk(),
            repository = repository,
            dispatcher = dispatcher
        )

        val listState = mutableListOf<LandingUiState>()
        val job = launch {
            vm.uiState.toList(listState)
        }
        //when
        val resultCount = vm.initCities()

        //then
        Assert.assertTrue(listState[0] is LandingUiState.Error)
        Assert.assertEquals(1, listState.size)
        Assert.assertEquals(EMPTY_DUE_ERROR, resultCount)
        job.cancel()
    }

    companion object {

        @JvmStatic
        fun provideArgumentsForEmptyBodyCitiesTest(): Stream<Arguments> {
            //params consist of: empty list and null
            return Stream.of(
                Arguments.of(listOf<CityCollection>()),
                Arguments.of(null)
            )
        }
    }
}