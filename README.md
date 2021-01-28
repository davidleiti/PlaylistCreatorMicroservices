Microservices based application server that allows for users to retrieve the current top tracks from a music provider and generate a playlist of tracks by performing CRUD operations on the said list of tracks.

## Components / Layers
The project consists of the following applications:
- **PlaylistClientApp**: A mobile Android application written in Kotlin used as the frondend of the project, allowing authenticated users to view the list and modify the list of tracks supplied by the backend components. The user authentication is done via Firebase Authentication, and all subsequent API communication is done via secured HTTPS calls providing the user's client service id token.

- **PlaylistCreator**: The his service is the entry point for the frontend and the only exposed component, essentially acting as a facade for the application domain, connecting the subsequent microservices and encapsulating the authentication and business logic of the application.
All the HTTPS endpoints exposed by this service are secured, expecting the aforementioned client service id token being supplied in each call's Authorization header and verifying it's validity via the **Firebase Admin SDK**.
    - POST /playlists: Gets the top music tracks from the **MusicService**, stores in the **PlaylistService**, and returns the list of tracks along the newly created UUID uniqely identifying the playlist.
    - GET /playlists/{id}/tracks: Gets the list of tracks from the playlist with the given ID from the **PlaylistService**
    - PUT /playlists/{id}/tracks?albumId={albumId}: Updates the playlist with the given *id* by retrieving the music tracks from the album *albumId* from the **MusicService** and adds them to the playlist stored on the **PlaylistService**.
    - DELTE /playlists/{id}/tracks/{trackId}: Removes the track with the given *trackId* from the *id* playlist on the **PlaylistService**

- **MusicService**: Microservice concerned with the retrieval of the top music tracks from the public **Spotify API**. The service handles the retrieval of valid access tokens for the public Spotify API endpoints by providing a Base64 encoded token composed of the generated clientId and clientSecret. After retrieving valid access tokens, the service can handle HTTP requests on the following endpoints:     
    - GET /albums/{albumId}: Which analogously retrieves and returns the music tracks for a specific album identified by its *albumId*
    - GET /tracks/top-50: Which retrieves, maps the relevant data, then returns the top 50 music tracks from the Spotify API
    
- **PlaylistService**: Microservice providing a set of CRUD operations for storing and modifying the created user playlists. For simplicity, the service will only be maintaining an in-memory repository for the generated playlists.
Exposed HTTP endpoints:
    - GET /playlists/{id}/tracks: Gets the music tracks from the playlist with the given *id*, or returns 404 error in case the playlist hasn't been created beforehand.
    - POST /playlists: Creates a new playlist by supplying in the request body a list of tracks and returns a UUID for the newly created playlist.
    - PUT /playlists/{id}/tracks: Adds a list of tracks to the playlist with the given *id*.
    - DELETE /playlists/{id}/tracks/{trackId}: Remove the track with the given *trackId* from the playlist with the given *id*.
   
   
<p align="center">
  <img src="https://i.imgur.com/hX9eHwL.png">
</p>

## Technologies & Services used

- [Ktor](https://ktor.io/): Ktor is a lightweight, asnychronous open-source Kotlin framework for implementing flexible, scalable, and efficient microservices and web applications in a functional manner. It has been primarily used for implementing all the backend services (PlaylistCreator, MusicService, and PlaylistService). 
- [Retrofit](https://square.github.io/retrofit/): For implementing the networking communication between all the layers (Client->App, App->Microservices, Microservices->Third Parties).
- [Gson](https://github.com/google/gson): Open-source JSON serialization/deserialization library.
- [Firebase Auth](https://firebase.google.com/docs/auth) and [Firebase Auth Admin](https://firebase.google.com/docs/auth/admin): For the user authentication process on the *Client* side and for the verification and security on the *Application* side.
- [Spotify Web API](https://developer.spotify.com/documentation/web-api/): Provided the functionality to retrieve lists of music tracks in the **MusicService** such as global top tracks and tracks of specific Spotify albums in a secure way.
- Client side technologies:
    - [Glide](https://bumptech.github.io/glide/) - Image loading library for displaying track thumbnails
    - [Kotlin coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
    - [Firebase Auth UI](https://firebase.google.com/docs/auth/web/firebaseui) - For generating authentication UI flows declaratively for a set of predefined Auth providers.

## SOA patterns used

- Microservice Architecture: The **MusicService** and **PlaylistService** microservices are self-contained, deployed independently, loosely coupled with other services as they are only referenced by the **PlaylistCreator** app, and can be developed on without interfering with one-another
- API Gateway: The **PlaylistCreator** app acts as a gateway to the backend domain, containing the limited amount of business logic. It essentially behaves as a proxy, connecting the client to the relevant backend microservices and abstracting the entire microservice-based architecture from the client.
- Decompose by subdomain: Each microservice belongs to a different problem subdomain, resulting in stable, loosely-coupled, and cohesive services.
- Database by service: Each microservice works on its own, self-contained database. (In this context the database refers to the in-memory repositories of the services)
- Service instance per container: Each microservice is contained in its own, isolated docker container, except the **PlaylistCreator** gateway, that imposed technical difficulties to mount in a container while still offering secure HTTPS communication. (Based on a self-signed certificate) 

## Screenshots

Authentication |  No playlist | Playlist loaded 
:-------------:|:----------------:|:----------------:
![Imgur](https://i.imgur.com/Bxp9F6o.png)  |  ![Imgur](https://i.imgur.com/q4nB37s.png) | ![Imgur](https://i.imgur.com/f0KI4wY.png)
