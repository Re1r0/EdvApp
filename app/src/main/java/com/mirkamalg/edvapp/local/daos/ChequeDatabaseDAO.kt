package com.mirkamalg.edvapp.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mirkamalg.edvapp.model.entities.ChequeEntity

/**
 * Created by Mirkamal on 25 January 2021
 */

@Dao
interface ChequeDatabaseDAO {

    @Insert
    fun insertNewCheque(chequeEntity: ChequeEntity)

    @Update
    fun updateCheque(chequeEntity: ChequeEntity)

    @Query("SELECT * from cheques_table where document_id = :chequeID")
    fun getChequeByID(chequeID: String): ChequeEntity

    @Query("SELECT * from cheques_table")
    fun getAllCheques(): List<ChequeEntity>
}