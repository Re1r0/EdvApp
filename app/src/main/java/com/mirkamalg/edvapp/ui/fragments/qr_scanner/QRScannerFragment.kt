package com.mirkamalg.edvapp.ui.fragments.qr_scanner

import android.Manifest
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.FragmentQrScannerBinding
import com.mirkamalg.edvapp.util.REQUEST_CODE_EXTERNAL_STORAGE_PERMISSION
import com.mirkamalg.edvapp.util.REQUEST_CODE_PICK_IMAGE_FROM_STORAGE
import com.mirkamalg.edvapp.viewmodels.QRScannerViewModel
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Mirkamal on 24 January 2021
 */
class QRScannerFragment : Fragment() {

    private var binding: FragmentQrScannerBinding? = null

    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private lateinit var cameraExecutor: ExecutorService

    private val qrScannerViewModel: QRScannerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQrScannerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        configureScanner()
        configureAnimation()
        configureObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE_FROM_STORAGE) {
            val uri = data?.data
            uri?.let {
                qrScannerViewModel.scanQRCodeFromURI(it)
            }
        }
    }

    private fun configureObservers() {
        qrScannerViewModel.shortID.observe(viewLifecycleOwner) {
            Log.e("HERE", it)
            findNavController().navigate(
                QRScannerFragmentDirections.actionQRScannerFragmentToScanResultBottomSheet(
                    it
                )
            )
        }
        qrScannerViewModel.error.observe(viewLifecycleOwner) {
            Log.e("HERE", "HERE")
            it?.let {
                Snackbar.make(
                    binding!!.previewViewQRScanner,
                    getString(R.string.err_qr_code_not_found),
                    Snackbar.LENGTH_SHORT
                )

            }
        }
    }

    private fun configureAnimation() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = 1000
            addUpdateListener {
                binding?.imageViewOverlayScanner?.alpha = it.animatedValue as Float
            }
            start()
        }
    }

    private fun setOnClickListeners() {
        binding?.apply {
            buttonGoBack.setOnClickListener {
                findNavController().popBackStack()
            }
            imageButtonPickFromGallery.setOnClickListener {
                if (handleStoragePermission()) {
                    startActivityForResult(
                        Intent().apply {
                            type = "image/*"
                            action = Intent.ACTION_GET_CONTENT
                        }, REQUEST_CODE_PICK_IMAGE_FROM_STORAGE
                    )
                }
            }
            imageButtonKeyboard.setOnClickListener {
                previewViewQRScanner.isVisible = false
                imageViewOverlayScanner.isVisible = false
                startAnimation()
                findNavController().navigate(QRScannerFragmentDirections.actionQRScannerFragmentToManualChequeAddFragment())
            }
        }
    }

    private fun startAnimation() {
        ValueAnimator.ofFloat(0f, -300f).apply {
            duration = 600
            addUpdateListener {
                binding?.root?.translationY = it.animatedValue as Float
            }
            start()
        }
    }

    private fun configureScanner() {
        cameraExecutor = Executors.newSingleThreadExecutor()

        context?.let { context ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                // Used to bind the lifecycle of cameras to the lifecycle owner
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                // Preview
                preview = Preview.Builder()
                    .build()

                imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(
                            cameraExecutor,
                            ScanImageAnalyzer(BarcodeAnalyzer { img, rotation, height, width ->
                                qrScannerViewModel.scanQRCode(img, rotation, height, width)
                            })
                        )
                    }

                // Select back camera
                val cameraSelector =
                    CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    camera = cameraProvider.bindToLifecycle(
                        viewLifecycleOwner, cameraSelector, preview, imageAnalyzer
                    )
                    preview?.setSurfaceProvider(binding?.previewViewQRScanner?.surfaceProvider)
                } catch (exc: Exception) {
                    Log.e("TAG", "Use case binding failed $exc")
                }

            }, ContextCompat.getMainExecutor(context))
        }
    }

    fun handleStoragePermission(): Boolean {
        context?.let {
            return if (ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_EXTERNAL_STORAGE_PERMISSION
                )
                false
            }
        }
        return false
    }
}

class BarcodeAnalyzer(val onSendImage: (img: ByteArray, rotation: Int, height: Int, width: Int) -> Unit)

private class ScanImageAnalyzer(val listener: BarcodeAnalyzer) : ImageAnalysis.Analyzer {

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    override fun analyze(image: ImageProxy) {
        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()
        listener.onSendImage(data, image.imageInfo.rotationDegrees, image.height, image.width)
        image.close()
    }
}