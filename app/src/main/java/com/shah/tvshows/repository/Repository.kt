package com.shah.tvshows.repository

import com.shah.tvshows.model.tv_show.TvShowModel
import com.shah.tvshows.util.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getTvShow(showName: String) : Flow<Resource<TvShowModel>>
}