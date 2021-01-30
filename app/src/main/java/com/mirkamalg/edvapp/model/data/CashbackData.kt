package com.mirkamalg.edvapp.model.data

/**
 * Created by Mirkamal on 27 January 2021
 */
data class CashbackData(
    val returnedAmount: Double?,
    val returnedDate: String?,
    val returnStatus: Int?,
    val returnRequestTransactionId: String?,
    val returnReturnTransactionId: String?
)