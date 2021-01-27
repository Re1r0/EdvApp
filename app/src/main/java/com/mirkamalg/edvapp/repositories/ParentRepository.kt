package com.mirkamalg.edvapp.repositories

import com.mirkamalg.edvapp.util.ResponseState
import retrofit2.Response

/**
 * Created by Mirkamal on 26 January 2021
 */
open class ParentRepository {

    fun <T> getResponseStatus(response: Response<T>): ResponseState {
        return try {
            if (response.isSuccessful) {
                if (response.body() != null) {
                    ResponseState.Success(response.body())
                } else {
                    ResponseState.NullBody
                }
            } else {
                ResponseState.Error(response.message())
            }
        } catch (e: Exception) {
            ResponseState.Error(e.message ?: "null")
        }
    }
}