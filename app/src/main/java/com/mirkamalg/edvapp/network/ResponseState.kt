package com.mirkamalg.edvapp.network

/**
 * Created by Mirkamal on 26 January 2021
 */
sealed class ResponseState {
    data class Success<T>(val data: T) : ResponseState()
    object NullBody : ResponseState()
    object NotFound : ResponseState()
    data class Error(val error: String) : ResponseState()
}