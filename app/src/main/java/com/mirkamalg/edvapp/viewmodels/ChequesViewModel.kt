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
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.mirkamalg.edvapp.local.database.ChequesDatabase
import com.mirkamalg.edvapp.model.data.CashbackData
import com.mirkamalg.edvapp.model.data.ChequeData
import com.mirkamalg.edvapp.model.data.ChequeWrapperData
import com.mirkamalg.edvapp.model.entities.ChequeEntity
import com.mirkamalg.edvapp.repositories.ChequesRepository
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
                data.documentId,
                data.shortDocumentId,
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
    fun getChequeDetails(chequeID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = chequesRepository.fetchChequeDetailsFromAPI(chequeID)) {
                is ResponseState.Success<*> -> {
                    withContext(Dispatchers.Main) {
                        _viewedChequeData.value = response.data as ChequeWrapperData
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

    fun getChequeDetailsFromDatabase(documentID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val cheque = chequesRepository.fetchChequeDetailsFromDatabase(documentID)
            cheque?.let {
                val converted = it.toChequeWrapperData()
                withContext(Dispatchers.Main) {
                    _viewedChequeData.value = converted
                    _viewedChequeData.value = null
                }
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
}