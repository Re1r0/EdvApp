package com.mirkamalg.edvapp.ui.fragments.cheque_details

import android.content.Intent
import android.os.Bundle
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
import com.mirkamalg.edvapp.util.*
import com.mirkamalg.edvapp.viewmodels.ChequesViewModel
import java.util.*

/**
 * Created by Mirkamal on 26 January 2021
 */
class ChequeDetailsFragment : Fragment() {

    private val args: ChequeDetailsFragmentArgs by navArgs()

    private var binding: FragmentChequeDetailsBinding? = null

    private val chequesViewModel: ChequesViewModel by navGraphViewModels(R.id.nav_graph_main)

    private lateinit var adapter: ChequeItemsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChequeDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureObservers()
        setOnClickListeners()
        configureRecyclerView()

        fetchChequeDetails()
    }

    private fun setOnClickListeners() {
        binding?.apply {
            buttonGoBack.setOnClickListener {
                findNavController().popBackStack()
            }
            imageButtonShare.setOnClickListener {
                shareText(
                    getString(R.string.msg_short_id),
                    args.chequeEntity.shortDocumentId
                )
//                chequesViewModel.drawBitmapFromView(this.mainConstraintLayout)
            }
            buttonRetry.setOnClickListener {
                fetchChequeDetails()
            }
            imageButtonCopyShortID.setOnClickListener {
                context.copyToClipboard(this.textViewShortID.text.toString())
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
                binding?.progressBar?.isVisible = false

                chequesViewModel.generateQRCodeFromString(
                    "${PREFIX_EGOV_URL}${it.cheque?.shortDocumentId}"
                )
                loadDataToUI(it)

                // update local db only if there isn't any data saved before
                if (args.chequeEntity.sum == null) {
                    it.cheque?.let { it1 -> updateLocalChequeData(it1) }
                }

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
                    binding?.textViewCashBack?.text = getText(R.string.msg_cashback_refunded)
                    updateChequeAsCashbackRefunded(args.chequeEntity.documentID)
                }
            }
        }
        chequesViewModel.generatedQRBitmap.observe(viewLifecycleOwner) {
            it?.let {
                binding?.imageViewQRCode?.setImageBitmap(it)
            }
        }
//        chequesViewModel.bitmapOfView.observe(viewLifecycleOwner) {
//            it?.let {
//                shareImage(it)
//            }
//        }
    }

    private fun updateChequeAsCashbackRefunded(documentID: String) {
        chequesViewModel.updateChequeAsCashbackRefunded(documentID)
    }

    private fun updateLocalChequeData(data: ChequeData) {
        chequesViewModel.updateCheque(data)
    }

    private fun loadDataToUI(data: ChequeWrapperData) {
        binding?.apply {
            textViewBannerStoreName.text = data.cheque?.storeName.toString()
            textViewAddress.text = data.cheque?.storeAddress.toString()
            textViewCompanyTaxNumber.text = data.cheque?.companyTaxNumber.toString()
            textViewStoreTaxNumber.text = data.cheque?.storeTaxNumber.toString()
            textViewSaleChequeNumber.text = data.cheque?.content?.docNumber.toString()
            textViewCashier.text = data.cheque?.content?.cashier.toString()
            textViewDateAndTime.text =
                Date(data.cheque?.content?.createdAtUtc?.times(1000) ?: 0L).formatToString()
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
            textViewCashBack.text = if (data.cashback == true) {
                getString(R.string.msg_cashback_refunded)
            } else {
                getString(R.string.msg_cashback_not_refunded)
            }

            nestedScrollViewChequeDetails.isVisible = true
        }
    }

    private fun configureRecyclerView() {
        adapter = ChequeItemsListAdapter()
        binding?.recyclerViewGoods?.adapter = adapter
    }

    private fun fetchChequeDetails() {

        binding?.apply {
            progressBar.isVisible = true
            imageViewError.isVisible = false
            buttonRetry.isVisible = false
            textViewError.isVisible = false
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
            ERROR_NOT_FOUND -> {
                if (binding?.nestedScrollViewChequeDetails?.isVisible == false) {
                    binding?.apply {
                        progressBar.isVisible = false

                        imageViewError.setImageResource(R.drawable.not_found)
                        imageViewError.isVisible = true
                        textViewError.text = getString(R.string.err_cheque_data_not_found)
                        textViewError.isVisible = true

                        buttonRetry.isVisible = true
                    }
                }
            }
            ERROR_RESPONSE_BODY_NULL -> {
                Toast.makeText(
                    context,
                    getString(R.string.err_unknown_error_occurred),
                    Toast.LENGTH_SHORT
                ).show()
            }
            ERROR_UNABLE_TO_RESOLVE_HOST -> {
                if (binding?.nestedScrollViewChequeDetails?.isVisible == false) {
                    binding?.apply {
                        progressBar.isVisible = false

                        imageViewError.setImageResource(R.drawable.internet_lost)
                        imageViewError.isVisible = true
                        textViewError.text = getString(R.string.err_cannot_connect_to_server)
                        textViewError.isVisible = true

                        buttonRetry.isVisible = true
                    }
                }
            }
            else -> {
                Toast.makeText(
                    context,
                    getString(R.string.err_unknown_error_occurred),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun shareText(subject: String, text: String) {
        startActivity(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        })
    }

//    private fun shareImage(bitmap: Bitmap) {
//        context?.let {
//            val share = Intent(Intent.ACTION_SEND)
//            share.type = "image/jpeg"
//            val bytes = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//
//            val file = File(it.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "cheque.jpeg")
//
//            FileOutputStream(file).apply {
//                write(bytes.toByteArray())
//                flush()
//                close()
//            }
//
//            val imageUri = FileProvider.getUriForFile(
//                it,
//                "com.mirkamalg.edvapp.ui.fragments.cheque_details.ChequeDetailsFragment",
//                file
//            )
//
//            share.putExtra(Intent.EXTRA_STREAM, imageUri)
//            startActivity(Intent.createChooser(share, getString(R.string.msg_choose)))
//        }
//    }
}