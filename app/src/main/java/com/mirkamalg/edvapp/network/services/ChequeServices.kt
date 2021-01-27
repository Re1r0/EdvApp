package com.mirkamalg.edvapp.network.services

import com.mirkamalg.edvapp.model.data.ChequeWrapperData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Mirkamal on 26 January 2021
 */
interface ChequeServices {

    @GET("documents/{chequeID}/")
    suspend fun getChequeDetails(
        @Path("chequeID") chequeID: String
    ): Response<ChequeWrapperData>

}