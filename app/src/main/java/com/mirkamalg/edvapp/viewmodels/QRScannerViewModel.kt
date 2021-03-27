package com.mirkamalg.edvapp.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Mirkamal on 24 January 2021
 */
class QRScannerViewModel(application: Application) : AndroidViewModel(application) {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()
    private val scanner = BarcodeScanning.getClient(options)

    private val _shortID = MutableLiveData<String>()
    val shortID: LiveData<String>
        get() = _shortID

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun scanQRCode(img: ByteArray, rotation: Int, height: Int, width: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val image = InputImage.fromByteArray(
                img,
                width,
                height,
                rotation,
                InputImage.IMAGE_FORMAT_NV21 // or IMAGE_FORMAT_YV12
            )
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    try {
                        val barcode = barcodes[0]
                        if (barcode.valueType == Barcode.TYPE_URL) {
                            barcode?.url?.url.let {
                                if (isValidURL(it.toString()) && _shortID.value != it) {
                                    _shortID.value = it
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("ScannerValueRead", e.message.toString())
                    }

                }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                    Log.i("ScannerFail", it.message.toString())
                }
        }

    }

    fun scanQRCodeFromURI(uri: Uri) {
        Log.e("HERE", uri.toString())
        scanner.process(InputImage.fromFilePath(getApplication(), uri))
            .addOnSuccessListener { barcodes ->
                try {
                    val barcode = barcodes[0]
                    if (barcode.valueType == Barcode.TYPE_URL) {
                        barcode?.url?.url.let {
                            if (isValidURL(it.toString()) && _shortID.value != it) {
                                _shortID.value = it
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ScannerValueRead", e.message.toString())
                }
            }.addOnFailureListener {
                _error.value = it.message
                _error.value = null
            }
    }

    private fun isValidURL(url: String): Boolean {
        return url.startsWith("https://monitoring.e-kassa.gov.az/#/index?doc=")
    }

}