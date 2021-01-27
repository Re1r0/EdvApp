package com.mirkamalg.edvapp.ui.fragments.cheques

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.FragmentChequesBinding
import com.mirkamalg.edvapp.ui.fragments.cheques.recyclerview.ChequesListAdapter
import com.mirkamalg.edvapp.util.REQUEST_CODE_CAMERA_PERMISSION
import com.mirkamalg.edvapp.viewmodels.ChequesViewModel

/**
 * Created by Mirkamal on 24 January 2021
 */
class ChequesFragment : Fragment() {

    private lateinit var binding: FragmentChequesBinding

    private val chequesViewModel: ChequesViewModel by navGraphViewModels(R.id.nav_graph_main)

    private lateinit var adapter: ChequesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChequesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        configureRecyclerView()
        configureObservers()
    }

    private fun configureRecyclerView() {
        adapter = ChequesListAdapter {
            findNavController().navigate(
                ChequesFragmentDirections.actionChequesFragmentToChequeDetailsFragment(
                    it
                )
            )
        }
        binding.recyclerViewCheques.adapter = adapter

        chequesViewModel.getAllCheques()
    }

    private fun configureObservers() {
        chequesViewModel.allCheques.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            navigateToQRScanner()
        } else {
            Toast.makeText(
                context,
                getString(R.string.err_camera_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setOnClickListeners() {
        binding.floationActionButtonScanQRCode.setOnClickListener {
            if (handlePermissions()) {
                navigateToQRScanner()
            }
        }
    }

    private fun handlePermissions(): Boolean {
        context?.let { ctx ->
            if (ContextCompat.checkSelfPermission(
                    ctx,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
            ) {
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(Manifest.permission.CAMERA),
                        REQUEST_CODE_CAMERA_PERMISSION
                    )
                }
                return false
            } else {
                return true
            }
        }
        return false
    }

    private fun navigateToQRScanner() {
        findNavController().navigate(ChequesFragmentDirections.actionChequesFragmentToQRScannerFragment())
    }
}