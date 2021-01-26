package com.ubb.david.playlistcreator.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.david.playlistcreator.data.NetworkManager
import com.ubb.david.playlistcreator.data.Result
import com.ubb.david.playlistcreator.domain.PlaylistDto
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

    fun createNewPlaylist(onCreate: (PlaylistDto) -> Unit) = launchAsyncAction {
        when (val result = networkManager.createPlaylist()) {
            is Result.Success -> {
                onCreate(result.data)
                _tracks.value = result.data.tracks.transformOrdering()
            }
            is Result.Error -> _errors.value = result.errorMessage
        }
    }

    fun fetchPlaylistTracks(playlistId: String) = launchAsyncAction {
        when (val result = networkManager.getPlaylistTracks(playlistId)) {
            is Result.Success -> _tracks.value = result.data.transformOrdering()
            is Result.Error -> _errors.value = result.errorMessage
        }
    }

    fun addAlbumTracks(playlistId: String, trackDto: TrackDto) = launchAsyncAction {
        when (val result = networkManager.addAlbumTracks(playlistId, trackDto.albumId, trackDto.albumName, trackDto.thumbnailUrl)) {
            is Result.Success -> {
                val resultWithAlbumData = result.data.map { track ->
                    track.copy(
                        albumId = trackDto.albumId,
                        albumName = trackDto.albumName,
                        thumbnailUrl = trackDto.thumbnailUrl
                    )
                }
                _tracks.value = _tracks.value?.let { tracks -> (tracks + resultWithAlbumData).transformOrdering() }
            }
            is Result.Error -> _errors.value = result.errorMessage
        }
    }

    fun deleteTrack(playlistId: String, trackId: String) = launchAsyncAction {
        when (val result = networkManager.removeTrack(playlistId, trackId)) {
            is Result.Success -> {
                if (result.data) {
                    _tracks.value = tracks.value?.let { it - it.first { track -> track.id == trackId } }
                }
            }
            is Result.Error -> _errors.value = result.errorMessage
        }
    }

    private fun launchAsyncAction(action: suspend () -> Unit) {
        _loading.value = true
        viewModelScope.launch {
            action()
            _loading.postValue(false)
        }
    }

    private fun List<TrackDto>.transformOrdering(): List<TrackDto> = distinctBy { it.id }.sortedBy { it.albumName }

}