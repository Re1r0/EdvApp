package com.mirkamalg.edvapp.model.data

/**
 * Created by Mirkamal on 24 January 2021
 */

data class ChequeWrapperData(
    val count: Int?,
    val cheque: ChequeData?
)

data class ChequeData(
    val shortDocumentId: String,
    val documentId: String,
    val factoryNumber: String?,
    val cashregisterModelName: String?,
    val cashregisterFactoryNumber: String?,
    val storePhone: String?,
    val storeName: String?,
    val storeAddress: String?,
    val companyName: String?,
    val companyTaxNumber: String?,
    val storeTaxNumber: String?,
    val content: ChequeContentData
)

data class ChequeContentData(
    val prepaymentSum: Double?,
    val docNumber: Long?,
    val creditSum: Double?,
    val docType: Int?,
    val vatAmounts: List<VATAmountsData?>?,
    val sum: Double?,
    val cashSum: Double?,
    val cashboxTaxNumber: String?,
    val bonusSum: Double?,
    val createdAtUtc: Long?,
    val cashier: String?,
    val positionInShift: Int?,
    val currency: String?,
    val globalDocNumber: Long?,
    val cashlessSum: Double?,
    val items: List<ChequeItemData?>?
)

data class VATAmountsData(
    val vatResult: Double?,
    val vatSum: Double?,
    val vatPercent: Double?
)

data class ChequeItemData(
    val itemNumber: Int?,
    val itemName: String?,
    val itemQuantity: Double?,
    val itemCodeType: Int?,
    val itemCode: Long?,
    val itemSum: Double?,
    val itemPrice: Double?,
    val itemVatPercent: Double?
)