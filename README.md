# Playlist-creator

Playlist creator is a microservices based application server that allows for users to retrieve the current top tracks from a music provider and generate a playlist of tracks by performing CRUD operations on the said list of tracks.

## Components
The project consists of the following applications:
- PlaylistClientApp: A mobile Android application written in Kotlin used as the frondend of the project, allowing authenticated users to view the list and modify the list of tracks supplied by the backend components. The user authentication is done via Firebase Authentication, and all subsequent API communication is done via secured HTTPS calls providing the user's client service id token.

- PlaylistCreator: This service is the entry point for the frontend and the only exposed component, essentially acting as a facade ƒor the application domain, connecting the subsequent microservices and encapsulating the authentication and business logic of the application.
All the HTTPS endpoints exposed by this service are secured, expecting the aforementioned client service id token being supplied in each call's Authorization header and verifying it's validity via the **Firebase Admin SDK**.
    - POST /playlists: Gets the top music tracks from the **MusicService**, stores in the **PlaylistService**, and returns the list of tracks along the newly created UUID uniqely identifying the playlist.
    - GET /playlists/{id}/tracks: Gets the list of tracks from the playlist with the given ID from the **PlaylistService**
    - PUT /playlists/{id}/tracks?albumId={albumId}: Updates the playlist with the given *id* by retrieving the music tracks from the album *albumId* from the **MusicService** and adds them to the playlist stored on the **PlaylistService**.
    - DELTE /playlists/{id}/tracks/{trackId}: Removes the track with the given *trackId* from the *id* playlist on the **PlaylistService**

- MusicService: Microservice concerned with the retrieval of the top music tracks from the public **Spotify API**. The service handles the retrieval of valid access tokens for the public Spotify API endpoints by providing a Base64 encoded token composed of the generated clientId and clientSecret. After retrieving valid access tokens, the service can handle HTTP requests on the following endpoints:     
    - GET /albums/{albumId}: Which analogously retrieves and returns the music tracks for a specific album identified by its *albumId*
    - GET /tracks/top-50: Which retrieves, maps the relevant data, then returns the top 50 music tracks from the Spotify API
    
- PlaylistService: Microservice providing a set of CRUD operations for storing and modifying the created user playlists. For simplicity, the service will only be maintaining an in-memory repository for the generated playlists.
Exposed HTTP endpoints:
    - GET /playlists/{id}/tracks: Gets the music tracks from the playlist with the given *id*, or returns 404 error in case the playlist hasn't been created beforehand.
    - POST /playlists: Creates a new playlist by supplying in the request body a list of tracks and returns a UUID for the newly created playlist.
    - PUT /playlists/{id}/tracks: Adds a list of tracks to the playlist with the given *id*.
    - DELETE /playlists/{id}/tracks/{trackId}: Remove the track with the given *trackId* from the playlist with the given *id*.

All 3 backend services were implemented in Kotlin via the Ktor framework, which is a lightweight functional framework for developing efficient and flexible microservices and web applications.

![System architecure](https://i.imgur.com/hX9eHwL.png)
