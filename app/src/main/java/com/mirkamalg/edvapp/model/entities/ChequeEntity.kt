package com.mirkamalg.edvapp.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Created by Mirkamal on 24 January 2021
 */

@Entity(tableName = "cheques_table")
@Parcelize
data class ChequeEntity(

    @PrimaryKey
    @ColumnInfo(name = "short_document_id")
    val shortDocumentId: String,

    @ColumnInfo(name = "document_id")
    val documentID: String,

    @ColumnInfo(name = "factory_number")
    val factoryNumber: String? = null,

    @ColumnInfo(name = "cashregister_model_name")
    val cashregisterModelName: String? = null,

    @ColumnInfo(name = "cashregister_factory_number")
    val cashregisterFactoryNumber: String? = null,

    @ColumnInfo(name = "store_phone")
    val storePhone: String? = null,

    @ColumnInfo(name = "store_name")
    val storeName: String? = null,

    @ColumnInfo(name = "store_address")
    val storeAddress: String? = null,

    @ColumnInfo(name = "company_name")
    val companyName: String? = null,

    @ColumnInfo(name = "company_tax_name")
    val companyTaxNumber: String? = null,

    @ColumnInfo(name = "store_tax_number")
    val storeTaxNumber: String? = null,

    @ColumnInfo(name = "prepayment_sum")
    val prepaymentSum: Double? = null,

    @ColumnInfo(name = "doc_number")
    val docNumber: Long? = null,

    @ColumnInfo(name = "credit_sum")
    val creditSum: Double? = null,

    @ColumnInfo(name = "doc_type")
    val docType: Long? = null,

    @ColumnInfo(name = "vat_result")
    val vatResult: Double? = null,

    @ColumnInfo(name = "vat_sum")
    val vatSum: Double? = null,

    @ColumnInfo(name = "vat_percent")
    val vatPercent: Double? = null,

    @ColumnInfo(name = "sum")
    val sum: Double? = null,

    @ColumnInfo(name = "cash_sum")
    val cashSum: Double? = null,

    @ColumnInfo(name = "cashbox_tax_number")
    val cashboxTaxNumber: String? = null,

    @ColumnInfo(name = "bonus_sum")
    val bonusSum: Double? = null,

    @ColumnInfo(name = "created_at_utc")
    val createdAtUtc: Long? = null,

    @ColumnInfo(name = "cashier")
    val cashier: String? = null,

    @ColumnInfo(name = "position_in_shift")
    val positionInShift: Long? = null,

    @ColumnInfo(name = "currency")
    val currency: String? = null,

    @ColumnInfo(name = "global_doc_number")
    val globalDocNumber: Long? = null,

    @ColumnInfo(name = "cashless_sum")
    val cashlessSum: Double? = null,

    @ColumnInfo(name = "items")
    val items: String = "",

    @ColumnInfo(name = "cashback")
    val cashback: Boolean = false
) : Parcelable