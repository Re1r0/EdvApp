package com.mirkamalg.edvapp.model.data

import com.squareup.moshi.Json

/**
 * Created by Mirkamal on 24 January 2021
 */

data class ChequeWrapperData(
    @Json(name = "count") val count: Int?,
    @Json(name = "cheque") val cheque: ChequeData?,
    // Set manually afterwards and used only for retrieving cashback status
    // in the observer of cheque details screen
    var cashback: Boolean = false
)

data class ChequeData(
    @Json(name = "shortDocumentId") val shortDocumentId: String,
    @Json(name = "documentId") val documentId: String,
    @Json(name = "factoryNumber") val factoryNumber: String?,
    @Json(name = "cashregisterModelName") val cashregisterModelName: String?,
    @Json(name = "cashregisterFactoryNumber") val cashregisterFactoryNumber: String?,
    @Json(name = "storePhone") val storePhone: String?,
    @Json(name = "storeName") val storeName: String?,
    @Json(name = "storeAddress") val storeAddress: String?,
    @Json(name = "companyName") val companyName: String?,
    @Json(name = "companyTaxNumber") val companyTaxNumber: String?,
    @Json(name = "storeTaxNumber") val storeTaxNumber: String?,
    @Json(name = "content") val content: ChequeContentData
)

data class ChequeContentData(
    @Json(name = "prepaymentSum") val prepaymentSum: Double?,
    @Json(name = "docNumber") val docNumber: Long?,
    @Json(name = "creditSum") val creditSum: Double?,
    @Json(name = "docType") val docType: Int?,
    @Json(name = "vatAmounts") var vatAmounts: List<VATAmountsData?>?,
    @Json(name = "sum") val sum: Double?,
    @Json(name = "cashSum") val cashSum: Double?,
    @Json(name = "cashboxTaxNumber") val cashboxTaxNumber: String?,
    @Json(name = "bonusSum") val bonusSum: Double?,
    @Json(name = "createdAtUtc") val createdAtUtc: Long?,
    @Json(name = "cashier") val cashier: String?,
    @Json(name = "positionInShift") val positionInShift: Int?,
    @Json(name = "currency") val currency: String?,
    @Json(name = "globalDocNumber") val globalDocNumber: Long?,
    @Json(name = "cashlessSum") val cashlessSum: Double?,
    @Json(name = "items") val items: List<ChequeItemData?>?
)

data class VATAmountsData(
    @Json(name = "vatResult") val vatResult: Double?,
    @Json(name = "vatSum") val vatSum: Double?,
    @Json(name = "vatPercent") val vatPercent: Double?
)

data class ChequeItemData(
    @Json(name = "itemNumber") val itemNumber: Int?,
    @Json(name = "itemName") val itemName: String?,
    @Json(name = "itemQuantity") val itemQuantity: Double?,
    @Json(name = "itemCodeType") val itemCodeType: Int?,
    @Json(name = "itemCode") val itemCode: Long?,
    @Json(name = "itemSum") val itemSum: Double?,
    @Json(name = "itemPrice") val itemPrice: Double?,
    @Json(name = "itemVatPercent") val itemVatPercent: Double?
)