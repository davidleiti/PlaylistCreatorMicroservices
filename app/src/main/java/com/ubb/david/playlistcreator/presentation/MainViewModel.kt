package com.ubb.david.playlistcreator.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.david.playlistcreator.data.NetworkManager
import com.ubb.david.playlistcreator.data.Result
import com.ubb.david.playlistcreator.domain.TrackDto
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val networkManager = NetworkManager()

    private val _tracks = MutableLiveData<List<TrackDto>>()
    val tracks: LiveData<List<TrackDto>> get() = _tracks

    private val _errors = MutableLiveData<String?>()
    val errors: LiveData<String?> get() = _errors

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    fun createNewPlaylist() {
        _loading.value = true
        viewModelScope.launch {
            when (val result = networkManager.createPlaylist()) {
                is Result.Success -> {
                    _tracks.value = result.data
                }
                is Result.Error -> {
                    _errors.value = result.errorMessage
                }
            }
            _loading.postValue(false)
        }
    }

    fun addAlbumTracks(albumId: String) {
        _loading.value = true
        viewModelScope.launch {
            when (val result = networkManager.addAlbumTracks(albumId)) {
                is Result.Success -> {
                    _tracks.value = _tracks.value?.let { tracks -> tracks + result.data }
                }
                is Result.Error -> {
                    _errors.value = result.errorMessage
                }
            }
            _loading.postValue(false)
        }
    }

    fun deleteTrack(trackId: String) {
        _loading.value = true
        viewModelScope.launch {
            when (val result = networkManager.removeTrack(trackId)) {
                is Result.Success -> {
                    if (result.data) {
                        _tracks.value = tracks.value?.let { it - it.first { track -> track.id == trackId } }
                    }
                }
                is Result.Error -> {
                    _errors.value = result.errorMessage
                }
            }
            _loading.postValue(false)
        }
    }

}