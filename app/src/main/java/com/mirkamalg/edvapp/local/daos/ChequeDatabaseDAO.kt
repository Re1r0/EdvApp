package com.mirkamalg.edvapp.local.daos

import androidx.room.*
import com.mirkamalg.edvapp.model.entities.ChequeEntity

/**
 * Created by Mirkamal on 25 January 2021
 */

@Dao
interface ChequeDatabaseDAO {

    @Insert
    fun insertNewCheque(chequeEntity: ChequeEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCheque(chequeEntity: ChequeEntity)

    @Query("UPDATE cheques_table SET cashback = 1 WHERE document_id = :documentID")
    fun setCashBackRefundedTrue(documentID: String)

    @Query("SELECT * from cheques_table where document_id = :chequeID")
    fun getChequeByID(chequeID: String): ChequeEntity?

    @Query("SELECT * from cheques_table where short_document_id = :shortID")
    fun getChequeByShortID(shortID: String): ChequeEntity?

    @Query("SELECT * from cheques_table")
    fun getAllCheques(): List<ChequeEntity>
}