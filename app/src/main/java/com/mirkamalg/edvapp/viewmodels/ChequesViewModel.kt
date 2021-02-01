package com.mirkamalg.edvapp.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.mirkamalg.edvapp.local.database.ChequesDatabase
import com.mirkamalg.edvapp.model.data.*
import com.mirkamalg.edvapp.model.entities.ChequeEntity
import com.mirkamalg.edvapp.repositories.ChequesRepository
import com.mirkamalg.edvapp.util.ERROR_NOT_FOUND
import com.mirkamalg.edvapp.util.ERROR_RESPONSE_BODY_NULL
import com.mirkamalg.edvapp.util.ResponseState
import com.mirkamalg.edvapp.util.toChequeWrapperData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Mirkamal on 25 January 2021
 */
class ChequesViewModel(application: Application) : AndroidViewModel(application) {

    private val chequesRepository = ChequesRepository(ChequesDatabase.getInstance(getApplication()))

    private val _allCheques = MutableLiveData<List<ChequeEntity>>()
    val allCheques: LiveData<List<ChequeEntity>>
        get() = _allCheques

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _viewedChequeData = MutableLiveData<ChequeWrapperData>()
    val viewedChequeData: LiveData<ChequeWrapperData>
        get() = _viewedChequeData

    private val _viewedChequeCashbackStatus = MutableLiveData<CashbackData>()
    val viewedChequeCashbackStatus: LiveData<CashbackData>
        get() = _viewedChequeCashbackStatus

    private val _generatedQRBitmap = MutableLiveData<Bitmap>()
    val generatedQRBitmap: LiveData<Bitmap>
        get() = _generatedQRBitmap

    private val _checkedChequeEntity = MutableLiveData<ChequeEntity>()
    val checkedChequeEntity: LiveData<ChequeEntity>
        get() = _checkedChequeEntity

    private val _totalExpenseAndVat = MutableLiveData<Pair<Double, Double>>()
    val totalExpenseAndVat: LiveData<Pair<Double, Double>>
        get() = _totalExpenseAndVat

    private val _vatList = MutableLiveData<List<VATListItemData>>()
    val vatList: LiveData<List<VATListItemData>>
        get() = _vatList

    fun getAllCheques() {
        viewModelScope.launch(Dispatchers.IO) {
            val cheques = chequesRepository.getAllCheques()?.asReversed()
            withContext(Dispatchers.Main) {
                _allCheques.value = cheques
            }
        }
    }

    fun addCheque(chequeEntity: ChequeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            chequesRepository.addChequeToDatabase(chequeEntity)
        }
    }

    fun updateCheque(data: ChequeData) {
        viewModelScope.launch(Dispatchers.IO) {
            val serializedItems = jacksonObjectMapper().writeValueAsString(data.content.items)

            val newEntity = ChequeEntity(
                data.shortDocumentId,
                data.documentId,
                data.factoryNumber,
                data.cashregisterModelName,
                data.cashregisterFactoryNumber,
                data.storePhone,
                data.storeName,
                data.storeAddress,
                data.companyName,
                data.companyTaxNumber,
                data.storeTaxNumber,
                data.content.prepaymentSum,
                data.content.docNumber,
                data.content.creditSum,
                data.content.docType?.toLong(),
                data.content.vatAmounts?.get(0)?.vatResult,
                data.content.vatAmounts?.get(0)?.vatSum,
                data.content.vatAmounts?.get(0)?.vatPercent,
                data.content.sum,
                data.content.cashSum,
                data.content.cashboxTaxNumber,
                data.content.bonusSum,
                data.content.createdAtUtc,
                data.content.cashier,
                data.content.positionInShift?.toLong(),
                data.content.currency,
                data.content.globalDocNumber,
                data.content.cashlessSum,
                serializedItems
            )

            chequesRepository.updateExistingCheque(newEntity)

            Log.d("UpdatedCheque", newEntity.toString())
        }
    }

    /**
     * Fetches cheque details of given ID from the server
     * @param chequeID ID of cheque whose details are being queried
     */
    fun getChequeDetails(chequeID: String = "", shortChequeID: String = "") {
        viewModelScope.launch(Dispatchers.IO) {

            /**
             * If cheque is added manually, it has an actual short id and a random UUID as document id
             * therefore, if documentID doesn't start with short id, this means it is random, and short id
             * must be used for request
             */
            val requestedID = if (chequeID.startsWith(shortChequeID)) {
                chequeID
            } else {
                shortChequeID
            }

            when (val response = chequesRepository.fetchChequeDetailsFromAPI(requestedID)) {
                is ResponseState.Success<*> -> {
                    val data = response.data as ChequeWrapperData

                    Log.e("HERE", "HERE3")
                    /**
                     * filter data so that only the object with 3 non null fields is remaining, if there
                     * isn't any, keep it as is
                     */
                    val filteredVATAmountsData = data.cheque?.content?.vatAmounts?.filter {
                        it?.vatPercent != null && it.vatResult != null && it.vatSum != null
                    } as ArrayList?

                    Log.e("HERE", "HERE4")
                    if (filteredVATAmountsData?.isNotEmpty() == true) {
                        data.cheque?.content?.vatAmounts = filteredVATAmountsData
                    }

                    withContext(Dispatchers.Main) {
                        Log.e("HERE", "HERE5")
                        _viewedChequeData.value = data
                        _viewedChequeData.value = null
                    }
                }
                is ResponseState.Error -> {
                    withContext(Dispatchers.Main) {
                        _error.value = response.error
                        _error.value = null
                    }
                }
                is ResponseState.NullBody -> {
                    withContext(Dispatchers.Main) {
                        _error.value = ERROR_RESPONSE_BODY_NULL
                        _error.value = null
                    }
                }
                is ResponseState.NotFound -> {
                    withContext(Dispatchers.Main) {
                        _error.value = ERROR_NOT_FOUND
                        _error.value = null
                    }
                }
            }
        }
    }

    fun getChequeCashbackStatus(chequeShortID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = chequesRepository.fetchCashbackStatusFromAPI(chequeShortID)) {
                is ResponseState.Success<*> -> {
                    withContext(Dispatchers.Main) {
                        _viewedChequeCashbackStatus.value = response.data as CashbackData
                        _viewedChequeCashbackStatus.value = null
                    }
                }
                is ResponseState.NullBody -> {
                    withContext(Dispatchers.Main) {
                        _error.value = ERROR_RESPONSE_BODY_NULL
                        _error.value = null
                    }
                }
                is ResponseState.Error -> {
                    withContext(Dispatchers.Main) {
                        _error.value = response.error
                        _error.value = null
                    }
                }
            }
        }
    }

    fun updateChequeAsCashbackRefunded(documentID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chequesRepository.setCashbackAsRefunded(documentID)
        }
    }

    fun getChequeDetailsFromDatabase(shortDocumentID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val cheque = chequesRepository.fetchChequeDetailsFromDatabase(shortDocumentID)
            cheque?.let {
                val converted = it.toChequeWrapperData()
                //Set cashback info here
                converted.cashback = it.cashback
                withContext(Dispatchers.Main) {
                    _viewedChequeData.value = converted
                    _viewedChequeData.value = null
                }
            }
        }
    }

    fun getChequeDetailsFromDatabaseByShortID(shortID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val cheque = chequesRepository.fetchChequeDetailsFromDatabaseByShortID(shortID)
            withContext(Dispatchers.Main) {
                _checkedChequeEntity.value = cheque
                _checkedChequeEntity.value = null
            }
        }
    }

    fun generateQRCodeFromString(string: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val width = 500
            val height = 500
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val codeWriter = MultiFormatWriter()
            try {
                val bitMatrix = codeWriter.encode(string, BarcodeFormat.QR_CODE, width, height)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                withContext(Dispatchers.Main) {
                    _generatedQRBitmap.value = bitmap
                    _generatedQRBitmap.value = null
                }
            } catch (e: WriterException) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message
                    _error.value = null
                }
            }
        }
    }

    fun calculateSumOfExpensesAndVAT(entities: List<ChequeEntity>) {
        viewModelScope.launch(Dispatchers.Default) {
            var sumExpenses = 0.0
            var sumVat = 0.0
            for (item in entities) {
                if (item.sum != null) sumExpenses += item.sum
                if (item.vatResult != null) sumVat += item.vatResult
            }
            withContext(Dispatchers.Main) {
                _totalExpenseAndVat.value = Pair(sumExpenses, sumVat)
                _totalExpenseAndVat.value = null
            }
        }
    }

    fun getVatListData(entities: List<ChequeEntity>) {
        viewModelScope.launch(Dispatchers.Default) {
            val vatListItems = arrayListOf<VATListItemData>()
            for (entity in entities) {
                val items: List<ChequeItemData> = jacksonObjectMapper().readValue(entity.items)
                for (item in items) {
                    if (vatListItems.none { item.itemName == it.name }) {
                        vatListItems.add(
                            VATListItemData(
                                item.itemName.toString(), item.itemPrice ?: 0.0,
                                item.itemPrice?.times(0.18)?.toString()?.substring(0, 4)?.toDouble()
                                    ?: 0.0
                            )
                        )
                    }
                }
            }
            withContext(Dispatchers.Main) {
                _vatList.value = vatListItems
                _vatList.value = null
            }
        }
    }
}