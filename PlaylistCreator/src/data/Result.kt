package com.ubb.david.data

/**
 * Model for a data operation's result.
 */
sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception? = null, val errorMessage: String? = null) : Result<Nothing>()
}