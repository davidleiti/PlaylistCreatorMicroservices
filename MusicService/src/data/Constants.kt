package com.ubb.david.data

// Spotify API urls
const val BASE_SPOTIFY_URL = "https://api.spotify.com/v1/"
const val AUTH_URL = "https://accounts.spotify.com/api/token"
const val TOP_TRACKS_URL = "playlists/{id}/tracks"
const val ALBUM_TRACKS_URL = "albums/{id}/tracks"
const val TOP_TRACKS_PLAYLIST_ID = "37i9dQZEVXbMDoHDwVN2tF"

// Authentication constants TODO extract into local config file
const val CLIENT_SECRET = "601ae269709743ff99407ec431863b08"
const val CLIENT_ID = "fd24ed178a0644bdadffad888befccc2"
const val GRANT_TYPE = "client_credentials"
