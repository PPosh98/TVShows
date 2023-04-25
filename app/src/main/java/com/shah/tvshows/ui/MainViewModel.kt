package com.shah.tvshows.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shah.tvshows.model.tv_show.TvShowModel
import com.shah.tvshows.repository.Repository
import com.shah.tvshows.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository): ViewModel(){

    private val _tvShow : MutableStateFlow<Resource<TvShowModel>> = MutableStateFlow(Resource.Loading())
    val tvShow: StateFlow<Resource<TvShowModel>> get() = _tvShow

    fun getTvShow(showName: String) {
        repository.getTvShow(showName)
            .onEach { _tvShow.value = it }
            .launchIn(viewModelScope)
    }
}