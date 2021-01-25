package com.ubb.david.playlistcreator

import android.app.Application
import com.google.firebase.FirebaseApp

class PlaylistApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}