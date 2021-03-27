package com.mirkamalg.edvapp.ui.fragments.cheques

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.FragmentChequesBinding
import com.mirkamalg.edvapp.ui.fragments.cheques.recyclerview.ChequesListAdapter
import com.mirkamalg.edvapp.util.REQUEST_CODE_CAMERA_PERMISSION
import com.mirkamalg.edvapp.util.round
import com.mirkamalg.edvapp.viewmodels.ChequesViewModel

/**
 * Created by Mirkamal on 24 January 2021
 */
class ChequesFragment : Fragment() {

    private var binding: FragmentChequesBinding? = null

    private val chequesViewModel: ChequesViewModel by navGraphViewModels(R.id.nav_graph_main)

    private lateinit var adapter: ChequesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChequesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        configureRecyclerView()
        configureObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun configureRecyclerView() {
        adapter = ChequesListAdapter(itemClickListener = {
            findNavController().navigate(
                ChequesFragmentDirections.actionChequesFragmentToChequeDetailsFragment(
                    it
                )
            )
        }, deleteListener = { entity, _ -> //position is unused
            findNavController().navigate(
                ChequesFragmentDirections.actionChequesFragmentToDeleteChequeConfirmationFragment(
                    entity
                )
            )
        })
        binding?.recyclerViewCheques?.adapter = adapter

        chequesViewModel.getAllCheques()
    }

    @Suppress("UNCHECKED_CAST")
    private fun configureObservers() {
        chequesViewModel.allCheques.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            chequesViewModel.calculateSumOfExpensesAndVAT(it)
        }
        chequesViewModel.totalExpenseAndVat.observe(viewLifecycleOwner) {
            binding?.apply {
                it?.let {
                    textViewTotalSpending.text =
                        getString(R.string.msg_money_amount_template, it.first.round(2).toString())
                    textViewTotalVAT.text =
                        getString(R.string.msg_money_amount_template, it.second.round(2).toString())
                }
            }
        }

        //Observer for retrieving data from delete confirmation dialog
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("deleteChequeKey")
            ?.observe(viewLifecycleOwner) {
                it?.let {
                    // Get cheques after 200ms since update taking effect takes some time
                    Handler(Looper.getMainLooper()).postDelayed({
                        chequesViewModel.getAllCheques()
                    }, 300)
                }
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
        binding?.apply {
            floationActionButtonScanQRCode.setOnClickListener {
                if (handlePermissions()) {
                    navigateToQRScanner()
                }
            }
            imageButtonInfoAboutApp.setOnClickListener {
                findNavController().navigate(ChequesFragmentDirections.actionChequesFragmentToAppInfoFragment())
            }
            imageButtonCalculator.setOnClickListener {
                findNavController().navigate(ChequesFragmentDirections.actionChequesFragmentToVATCalculatorFragment())
            }
            cardViewExpenses.setOnClickListener {
                val extras =
                    FragmentNavigatorExtras(
                        textViewTotalSpendingLabel to "totalExpenseText",
                        textViewTotalSpending to "totalExpenseAmount"
                    )
                findNavController().navigate(
                    ChequesFragmentDirections.actionChequesFragmentToExpensesFragment(
                        textViewTotalSpending.text.toString()
                    ),
                    extras
                )
            }
            cardViewVAT.setOnClickListener {
                val extras =
                    FragmentNavigatorExtras(
                        textViewTotalVATLabel to "totalVATText",
                        textViewTotalVAT to "totalVatAmount"
                    )
                findNavController().navigate(
                    ChequesFragmentDirections.actionChequesFragmentToVATFragment(textViewTotalVAT.text.toString()),
                    extras
                )
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