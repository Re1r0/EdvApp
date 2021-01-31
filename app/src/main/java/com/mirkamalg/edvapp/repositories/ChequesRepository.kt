package com.mirkamalg.edvapp.repositories

import com.mirkamalg.edvapp.local.database.ChequesDatabase
import com.mirkamalg.edvapp.model.entities.ChequeEntity
import com.mirkamalg.edvapp.network.ApiInitHelper
import com.mirkamalg.edvapp.util.ResponseState

/**
 * Created by Mirkamal on 25 January 2021
 */
class ChequesRepository(private val chequesDatabase: ChequesDatabase? = null) : ParentRepository() {

    private val chequeDatabaseDAO = chequesDatabase?.chequeDatabaseDAO
    private val chequesServices by lazy {
        ApiInitHelper.chequesServices
    }

    fun getAllCheques(): List<ChequeEntity>? {
        return chequeDatabaseDAO?.getAllCheques()
    }

    fun setCashbackAsRefunded(documentID: String) {
        chequeDatabaseDAO?.setCashBackRefundedTrue(documentID)
    }

    fun fetchChequeDetailsFromDatabase(shortDocumentID: String): ChequeEntity? {
        return chequeDatabaseDAO?.getChequeByShortID(shortDocumentID)
    }

    fun fetchChequeDetailsFromDatabaseByShortID(shortID: String): ChequeEntity? {
        return chequeDatabaseDAO?.getChequeByShortID(shortID)
    }

    fun addChequeToDatabase(chequeEntity: ChequeEntity) {
        chequeDatabaseDAO?.insertNewCheque(chequeEntity)
    }

    fun updateExistingCheque(chequeEntity: ChequeEntity) {
        chequeDatabaseDAO?.updateCheque(chequeEntity)
    }

    suspend fun fetchChequeDetailsFromAPI(chequeID: String): ResponseState {
        return try {
            getResponseStatus(chequesServices.getChequeDetails(chequeID))
        } catch (e: Exception) {
            ResponseState.Error(e.message.toString())
        }
    }

    suspend fun fetchCashbackStatusFromAPI(chequeShortID: String): ResponseState {
        return try {
            getResponseStatus(chequesServices.getChequeCashbackStatus(chequeShortID))
        } catch (e: java.lang.Exception) {
            ResponseState.Error(e.message.toString())
        }
    }
}