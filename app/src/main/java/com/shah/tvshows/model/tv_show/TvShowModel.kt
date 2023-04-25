package com.shah.tvshows.model.tv_show


import com.google.gson.annotations.SerializedName

data class TvShowModel(
    @SerializedName("image")
    val image: ImageModel,
    @SerializedName("name")
    val name: String,
    @SerializedName("premiered")
    val premiered: String,
)