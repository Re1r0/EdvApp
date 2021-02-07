package com.mirkamalg.edvapp.local.daos

import androidx.room.*
import com.mirkamalg.edvapp.model.entities.ChequeEntity

/**
 * Created by Mirkamal on 25 January 2021
 */

@Dao
interface ChequeDatabaseDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertNewCheque(chequeEntity: ChequeEntity)

    @Delete
    fun deleteCheque(chequeEntity: ChequeEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCheque(chequeEntity: ChequeEntity)

    @Query("UPDATE cheques_table SET cashback = 1 WHERE document_id = :documentID")
    fun setCashBackRefundedTrue(documentID: String)

    @Deprecated("Use getChequeByShortID instead")
    @Query("SELECT * from cheques_table where document_id = :chequeID")
    fun getChequeByID(chequeID: String): ChequeEntity?

    @Query("SELECT * from cheques_table where short_document_id = :shortID")
    fun getChequeByShortID(shortID: String): ChequeEntity?

    @Query("SELECT * from cheques_table")
    fun getAllCheques(): List<ChequeEntity>

    @Query("SELECT * from cheques_table WHERE created_at_utc > :date")
    fun getChequesWithDateGreaterThan(date: Long): List<ChequeEntity>

    @Query("SELECT store_name from cheques_table GROUP BY store_name ORDER BY COUNT(store_name) DESC LIMIT 1;")
    fun getMostFrequentMarket(): String?

    @Query("SELECT items from cheques_table")
    fun getItemsOfAllCheques(): List<String>
}