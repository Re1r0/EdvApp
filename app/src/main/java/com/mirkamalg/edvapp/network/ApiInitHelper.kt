package com.mirkamalg.edvapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mirkamalg.edvapp.BuildConfig
import com.mirkamalg.edvapp.network.services.ChequeServices
import com.mirkamalg.edvapp.util.BASE_URL_CHEQUE_DETAILS
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Mirkamal on 26 January 2021
 */

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object ApiInitHelper {

    private val okHttpBuilder = OkHttpClient.Builder()
    private var retrofit: Retrofit? = null

    private fun okHttpClient(): OkHttpClient {
        when {
            BuildConfig.DEBUG -> {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                okHttpBuilder.addInterceptor(logging)
            }
        }
        return okHttpBuilder.build()
    }

    private fun getClient(): Retrofit {
        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient())
            .baseUrl(BASE_URL_CHEQUE_DETAILS)
            .build()

        return retrofit as Retrofit
    }

    val chequesServices: ChequeServices by lazy {
        getClient().create(ChequeServices::class.java)
    }

}