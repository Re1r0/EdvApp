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

    fun fetchChequeDetailsFromDatabase(documentID: String): ChequeEntity? {
        return chequeDatabaseDAO?.getChequeByID(documentID)
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
}