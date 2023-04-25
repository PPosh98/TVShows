package com.shah.tvshows.api

import com.shah.tvshows.model.tv_show.TvShowModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FetchAPI {

    @GET("singlesearch/shows")
    suspend fun getTvShow(
        @Query("q") showName: String
    ) : Response<TvShowModel>
}