package com.mirkamalg.edvapp.ui.fragments.cheque_details

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

        // Fetch check requests only if there is no local data
        if (args.chequeEntity.sum == null) {
            chequesViewModel.getChequeDetails(args.chequeEntity.documentID)
        } else {
            //Load from local db if data is stored
            chequesViewModel.getChequeDetailsFromDatabase(args.chequeEntity.documentID)
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            buttonGoBack.setOnClickListener {
                findNavController().popBackStack()
            }
            imageButtonShare.setOnClickListener {

            }
            imageButtonCopyShortID.setOnClickListener {

            }
        }
    }

    private fun configureObservers() {
        chequesViewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
        chequesViewModel.viewedChequeData.observe(viewLifecycleOwner) {
            //TODO (generally) complete data class, complete details screen and writing to database
            it?.let {
                loadDataToUI(it)
                it.cheque?.let { it1 -> updateLocalChequeData(it1) }
            }
        }
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
}