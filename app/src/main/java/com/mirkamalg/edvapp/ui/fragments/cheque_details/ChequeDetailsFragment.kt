package com.mirkamalg.edvapp.ui.fragments.cheque_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.FragmentChequeDetailsBinding
import com.mirkamalg.edvapp.model.data.ChequeData
import com.mirkamalg.edvapp.model.data.ChequeWrapperData
import com.mirkamalg.edvapp.ui.fragments.cheque_details.recyclerview.ChequeItemsListAdapter
import com.mirkamalg.edvapp.util.ERROR_RESPONSE_BODY_NULL
import com.mirkamalg.edvapp.util.PREFIX_EGOV_URL
import com.mirkamalg.edvapp.util.copyToClipboard
import com.mirkamalg.edvapp.viewmodels.ChequesViewModel

/**
 * Created by Mirkamal on 26 January 2021
 */
class ChequeDetailsFragment : Fragment() {

    private val args: ChequeDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentChequeDetailsBinding

    private val chequesViewModel: ChequesViewModel by navGraphViewModels(R.id.nav_graph_main)

    private lateinit var adapter: ChequeItemsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChequeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureObservers()
        setOnClickListeners()
        configureRecyclerView()

        fetchChequeDetails()
    }

    private fun setOnClickListeners() {
        binding.apply {
            buttonGoBack.setOnClickListener {
                findNavController().popBackStack()
            }
            imageButtonShare.setOnClickListener {

            }
            buttonRetry.setOnClickListener {
                fetchChequeDetails()
            }
            imageButtonCopyShortID.setOnClickListener {
                context.copyToClipboard(binding.textViewShortID.text.toString())
                Toast.makeText(
                    context,
                    getString(R.string.msg_short_document_id_was_copied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun configureObservers() {
        chequesViewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                handleError(it)
            }
        }
        chequesViewModel.viewedChequeData.observe(viewLifecycleOwner) {
            it?.let {
                binding.progressBar.isVisible = false

                chequesViewModel.generateQRCodeFromString(
                    "${PREFIX_EGOV_URL}${it.cheque?.shortDocumentId}"
                )
                loadDataToUI(it)
                it.cheque?.let { it1 -> updateLocalChequeData(it1) }

                // Fetch cashback status every time unless it is refunded
                if (!args.chequeEntity.cashback) {
                    chequesViewModel.getChequeCashbackStatus(args.chequeEntity.shortDocumentId)
                }
            }
        }
        chequesViewModel.viewedChequeCashbackStatus.observe(viewLifecycleOwner) {
            //TODO change checking method to using 'returnStatus' instead of 'returnedAmount'
            it?.let {
                it.returnedAmount?.let {
                    binding.textViewCashBack.text = getText(R.string.msg_cashback_refunded)
                    updateChequeAsCashbackRefunded(args.chequeEntity.documentID)
                }
            }
        }
        chequesViewModel.generatedQRBitmap.observe(viewLifecycleOwner) {
            it?.let {
                binding.imageViewQRCode.setImageBitmap(it)
            }
        }
    }

    private fun updateChequeAsCashbackRefunded(documentID: String) {
        chequesViewModel.updateChequeAsCashbackRefunded(documentID)
    }

    private fun updateLocalChequeData(data: ChequeData) {
        chequesViewModel.updateCheque(data)
    }

    private fun loadDataToUI(data: ChequeWrapperData) {
        binding.apply {
            textViewBannerStoreName.text = data.cheque?.storeName.toString()
            textViewAddress.text = data.cheque?.storeAddress.toString()
            textViewCompanyTaxNumber.text = data.cheque?.companyTaxNumber.toString()
            textViewStoreTaxNumber.text = data.cheque?.storeTaxNumber.toString()
            textViewSaleChequeNumber.text = data.cheque?.content?.docNumber.toString()
            textViewCashier.text = data.cheque?.content?.cashier.toString()
            //TODO convert utc to date time text and set it here
            adapter.submitList(data.cheque?.content?.items ?: emptyList())
            textViewSum.text =
                getString(R.string.msg_money_amount_template, data.cheque?.content?.sum.toString())
            textViewVAT.text = getString(
                R.string.msg_money_amount_template,
                data.cheque?.content?.vatAmounts?.get(0)?.vatResult.toString()
            )
            textViewCashlessPayment.text = getString(
                R.string.msg_money_amount_template,
                data.cheque?.content?.cashlessSum.toString()
            )
            textViewCashPayment.text = getString(
                R.string.msg_money_amount_template,
                data.cheque?.content?.cashSum.toString()
            )
            textViewBonus.text = getString(
                R.string.msg_money_amount_template,
                data.cheque?.content?.bonusSum.toString()
            )
            textViewCredit.text = getString(
                R.string.msg_money_amount_template,
                data.cheque?.content?.creditSum.toString()
            )
            textViewAdvance.text = getString(
                R.string.msg_money_amount_template,
                data.cheque?.content?.prepaymentSum.toString()
            )
            textViewShortID.text = data.cheque?.shortDocumentId.toString()
            //TODO configure text for cashback with new api

            nestedScrollViewChequeDetails.isVisible = true
        }
    }

    private fun configureRecyclerView() {
        adapter = ChequeItemsListAdapter()
        binding.recyclerViewGoods.adapter = adapter
    }

    private fun fetchChequeDetails() {

        binding.apply {
            progressBar.isVisible = true
            imageViewConnectionLost.isVisible = false
            buttonRetry.isVisible = false
            textViewConnectionLost.isVisible = false
        }

        // Fetch check requests only if there is no local data
        if (args.chequeEntity.sum == null) {
            chequesViewModel.getChequeDetails(args.chequeEntity.shortDocumentId)
        } else {
            //Load from local db if data is stored
            chequesViewModel.getChequeDetailsFromDatabase(args.chequeEntity.shortDocumentId)
        }
    }

    private fun handleError(error: String) {
        when (error) {
            ERROR_RESPONSE_BODY_NULL -> {
                Toast.makeText(
                    context,
                    getString(R.string.err_cheque_data_not_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                binding.apply {
                    progressBar.isVisible = false
                    imageViewConnectionLost.isVisible = true
                    textViewConnectionLost.isVisible = true
                    buttonRetry.isVisible = true
                }
                Log.e("ChequeDetailsError", error)
            }
        }
    }
}