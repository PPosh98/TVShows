package com.shah.tvshows.di

import android.content.Context
import androidx.room.Room
import com.shah.tvshows.roomdb.TvShowsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideRoomInstance(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        TvShowsDatabase::class.java,
        "TvShowsDatabase"
    ).build()

    @Provides
    fun provideTvShowsDao(database: TvShowsDatabase) = database.tvShowsDAO()
}