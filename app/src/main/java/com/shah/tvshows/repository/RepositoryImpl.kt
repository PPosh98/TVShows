package com.shah.tvshows.repository

import com.shah.tvshows.api.FetchAPI
import com.shah.tvshows.model.tv_show.TvShowModel
import com.shah.tvshows.roomdb.TvShowsDAO
import com.shah.tvshows.roomdb.TvShowsEntity
import com.shah.tvshows.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val fetchAPI: FetchAPI,
    private val tvShowsDAO: TvShowsDAO
) :
    Repository {

    override fun getTvShow(showName: String): Flow<Resource<TvShowModel>> {
        return flow {
            emit(Resource.Loading(true))
            val tvShowFromLocal = tvShowsDAO.readTvShow(showName)
            val shouldJustLoadFromCache =
                tvShowFromLocal != null && showName.isNotBlank()
            if (shouldJustLoadFromCache) {
                emit(Resource.Success(tvShowFromLocal?.tvShowModel))
                return@flow
            }
            try {
                val response = fetchAPI.getTvShow(showName)
                if (response.isSuccessful) {
                    val tvShow = response.body() as TvShowModel
                    tvShowsDAO.insertTvShow(TvShowsEntity(tvShow, showName))
                    emit(Resource.Success(tvShow))
                } else {
                    emit(Resource.Error(response.message()))
                }
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }
        }
    }
}