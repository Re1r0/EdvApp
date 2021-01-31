package com.mirkamalg.edvapp.model.data

import com.squareup.moshi.Json

/**
 * Created by Mirkamal on 27 January 2021
 */
data class CashbackData(
    @Json(name = "returnedAmount") val returnedAmount: Double?,
    @Json(name = "returnedDate") val returnedDate: String?,
    @Json(name = "returnStatus") val returnStatus: Int?,
    @Json(name = "returnRequestTransactionId") val returnRequestTransactionId: String?,
    @Json(name = "returnReturnTransactionId") val returnReturnTransactionId: String?
)