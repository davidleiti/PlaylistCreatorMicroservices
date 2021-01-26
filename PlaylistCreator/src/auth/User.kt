package com.ubb.david.auth

import io.ktor.auth.Principal

data class User(val id: String) : Principal