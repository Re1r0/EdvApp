package com.mirkamalg.edvapp.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mirkamalg.edvapp.model.data.*
import com.mirkamalg.edvapp.model.entities.ChequeEntity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mirkamal on 25 January 2021
 */

fun String.toDate(
    dateFormat: String = "dd.MM.yyy HH:mm",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date? {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun ChequeEntity.toChequeWrapperData(): ChequeWrapperData {
    val items: List<ChequeItemData> = jacksonObjectMapper().readValue(this.items)
    return ChequeWrapperData(
        1,
        ChequeData(
            this.shortDocumentId,
            this.documentID,
            this.factoryNumber,
            this.cashregisterModelName,
            this.cashregisterFactoryNumber,
            this.storePhone,
            this.storeName,
            this.storeAddress,
            this.companyName,
            this.companyTaxNumber,
            this.storeTaxNumber,
            ChequeContentData(
                this.prepaymentSum,
                this.docNumber,
                this.creditSum,
                this.docType?.toInt(),
                listOf(VATAmountsData(this.vatResult, this.vatSum, this.vatPercent)),
                this.sum,
                this.cashSum,
                this.cashboxTaxNumber,
                this.bonusSum,
                this.createdAtUtc,
                this.cashier,
                this.positionInShift?.toInt(),
                this.currency,
                this.globalDocNumber,
                this.cashlessSum,
                items
            )
        )
    )
}