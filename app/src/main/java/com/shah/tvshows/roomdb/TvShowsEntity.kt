package com.shah.tvshows.roomdb

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.shah.tvshows.model.tv_show.ImageModel
import com.shah.tvshows.model.tv_show.TvShowModel

@Entity(tableName = "tvShows")
data class TvShowsEntity(
    val tvShowModel: TvShowModel,
    @PrimaryKey(autoGenerate = false)
    val query: String
    ) {
    var name: String = tvShowModel.name
    var image: ImageModel = ImageModel(tvShowModel.image.medium,tvShowModel.image.original)
    var premiered: String = tvShowModel.premiered
}