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
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.FragmentChequesBinding
import com.mirkamalg.edvapp.model.entities.ChequeEntity
import com.mirkamalg.edvapp.ui.fragments.cheques.recyclerview.ChequesListAdapter
import com.mirkamalg.edvapp.util.DOT_CHAR
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
        adapter = ChequesListAdapter({
            findNavController().navigate(
                ChequesFragmentDirections.actionChequesFragmentToChequeDetailsFragment(
                    it
                )
            )
        }, { entity, position ->
            chequesViewModel.deleteCheque(entity)
            chequesViewModel.removeItemAtPosition(adapter.currentList, position)
        })
        binding.recyclerViewCheques.adapter = adapter

        chequesViewModel.getAllCheques()
    }

    @Suppress("UNCHECKED_CAST")
    private fun configureObservers() {
        chequesViewModel.allCheques.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            chequesViewModel.calculateSumOfExpensesAndVAT(it)
        }
        chequesViewModel.totalExpenseAndVat.observe(viewLifecycleOwner) {
            binding.apply {
                it?.let {
                    val first = it.first.toString()
                    val second = it.second.toString()

                    val spendingText = if ((first.length - first.indexOf(DOT_CHAR)) > 3) {
                        first.substring(0, first.indexOf(DOT_CHAR) + 2)
                    } else {
                        first
                    }
                    val vatText = if ((second.length - second.indexOf(DOT_CHAR)) > 3) {
                        second.substring(0, first.indexOf(DOT_CHAR) + 2)
                    } else {
                        second
                    }

                    textViewTotalSpending.text =
                        getString(R.string.msg_money_amount_template, spendingText)
                    textViewTotalVAT.text =
                        getString(R.string.msg_money_amount_template, vatText)
                }
            }
        }
        chequesViewModel.listData.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it as ArrayList<ChequeEntity>)
                chequesViewModel.calculateSumOfExpensesAndVAT(it)
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
        binding.apply {
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
                        binding.textViewTotalSpendingLabel to "totalExpenseText",
                        binding.textViewTotalSpending to "totalExpenseAmount"
                    )
                findNavController().navigate(
                    ChequesFragmentDirections.actionChequesFragmentToExpensesFragment(binding.textViewTotalSpending.text.toString()),
                    extras
                )
            }
            cardViewVAT.setOnClickListener {
                val extras =
                    FragmentNavigatorExtras(
                        binding.textViewTotalVATLabel to "totalVATText",
                        binding.textViewTotalVAT to "totalVatAmount"
                    )
                findNavController().navigate(
                    ChequesFragmentDirections.actionChequesFragmentToVATFragment(binding.textViewTotalVAT.text.toString()),
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