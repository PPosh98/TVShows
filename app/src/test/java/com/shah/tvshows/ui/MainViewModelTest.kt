package com.shah.tvshows.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.shah.tvshows.model.tv_show.ImageModel
import com.shah.tvshows.model.tv_show.TvShowModel
import com.shah.tvshows.repository.RepositoryImpl
import com.shah.tvshows.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    lateinit var viewModel: MainViewModel

    private val repository: RepositoryImpl = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = MainViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTvShow() updates TV show state with data`() {

        val peppa = TvShowModel(
            ImageModel("medImageURL", "bigImageURL"), "Peppa Pig", "2005-10-10")

        coEvery { repository.getTvShow("pig") } returns flowOf(Resource.Success(peppa))

        viewModel.getTvShow("pig")

        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(peppa, viewModel.tvShow.value.data)
    }

    @Test
    fun `getTvShow() updates TV show state with no data`() {

        coEvery { repository.getTvShow("spiderman 3") } returns flowOf(Resource.Error("Show doesn't exist"))

        viewModel.getTvShow("spiderman 3")

        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(null, viewModel.tvShow.value.data)
    }
}