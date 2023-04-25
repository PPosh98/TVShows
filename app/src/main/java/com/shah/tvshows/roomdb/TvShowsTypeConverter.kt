package com.shah.tvshows.roomdb

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shah.tvshows.model.tv_show.ImageModel
import com.shah.tvshows.model.tv_show.TvShowModel

class TvShowsTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun tvShowToString(tvShow: TvShowModel): String = gson.toJson(tvShow)

    @TypeConverter
    fun stringToTvShow(data: String): TvShowModel {
        val listType = object : TypeToken<TvShowModel>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun tvShowImageToString(imageModel: ImageModel): String = gson.toJson(imageModel)

    @TypeConverter
    fun stringToTvShowImage(data: String): ImageModel {
        val listType = object : TypeToken<ImageModel>() {}.type
        return gson.fromJson(data, listType)
    }
}