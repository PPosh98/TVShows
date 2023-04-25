package com.shah.tvshows.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.shah.tvshows.api.FetchAPI
import com.shah.tvshows.model.tv_show.ImageModel
import com.shah.tvshows.model.tv_show.TvShowModel
import com.shah.tvshows.roomdb.TvShowsDAO
import com.shah.tvshows.roomdb.TvShowsEntity
import com.shah.tvshows.ui.MainViewModel
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.internal.EMPTY_RESPONSE
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    lateinit var viewModel: MainViewModel

    private lateinit var repository: RepositoryImpl

    private val fetchAPI: FetchAPI = mockk()
    private val tvShowsDAO: TvShowsDAO = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = RepositoryImpl(fetchAPI, tvShowsDAO)
        viewModel = MainViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when readTvShow() is called and returns data, check state is updated with data`() {

        val peppa = TvShowsEntity(TvShowModel(
            ImageModel("medImageURL", "bigImageURL"), "Peppa Pig", "2005-10-10"), "pig")

        coEvery { tvShowsDAO.readTvShow("pig") } returns peppa

        viewModel.getTvShow("pig")

        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(peppa.tvShowModel, viewModel.tvShow.value.data)
    }

    @Test
    fun `when readTvShow() is called and returns null, call API and check state is updated with data`() {

        val peppa = TvShowsEntity(TvShowModel(
            ImageModel("medImageURL", "bigImageURL"), "Peppa Pig", "2005-10-10"), "pig")

        coEvery { tvShowsDAO.readTvShow("pig") } returns null

        coEvery { fetchAPI.getTvShow("pig") } returns Response.success(peppa.tvShowModel)

        coEvery { tvShowsDAO.insertTvShow(peppa) } just runs

        viewModel.getTvShow("pig")

        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(peppa.tvShowModel, viewModel.tvShow.value.data)
    }

    @Test
    fun `when readTvShow() is called and returns null and API call returns no data, check state has no data`() {

        val peppa = TvShowsEntity(TvShowModel(
            ImageModel("medImageURL", "bigImageURL"), "Peppa Pig", "2005-10-10"), "pig")

        coEvery { tvShowsDAO.readTvShow("pig") } returns null

        coEvery { fetchAPI.getTvShow("pig") } returns Response.error(404, EMPTY_RESPONSE)

        coEvery { tvShowsDAO.insertTvShow(peppa) } just runs

        viewModel.getTvShow("pig")

        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(null, viewModel.tvShow.value.data)
    }
}