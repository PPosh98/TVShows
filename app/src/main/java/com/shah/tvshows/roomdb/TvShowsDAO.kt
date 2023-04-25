package com.shah.tvshows.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TvShowsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShow(tvShowsEntity: TvShowsEntity)

    @Query("SELECT * FROM tvShows WHERE query = :query")
    suspend fun readTvShow(query: String) : TvShowsEntity?
}