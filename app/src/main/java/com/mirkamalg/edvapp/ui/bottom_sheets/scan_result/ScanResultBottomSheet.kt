package com.mirkamalg.edvapp.ui.bottom_sheets.scan_result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.SheetScanResultBinding
import com.mirkamalg.edvapp.model.entities.ChequeEntity
import com.mirkamalg.edvapp.viewmodels.ChequesViewModel

/**
 * Created by Mirkamal on 24 January 2021
 */
class ScanResultBottomSheet : BottomSheetDialogFragment() {

    private val args: ScanResultBottomSheetArgs by navArgs()

    private lateinit var binding: SheetScanResultBinding

    private val chequesViewModel: ChequesViewModel by navGraphViewModels(R.id.nav_graph_main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SheetScanResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleArgs()
        setOnClickListeners()
        configureObservers()
        checkIfChequeIsAlreadyAdded()
    }

    private fun checkIfChequeIsAlreadyAdded() {
        chequesViewModel.getChequeDetailsFromDatabase(args.url.split("=")[1].substring(0, 12))
    }

    private fun configureObservers() {
        chequesViewModel.viewedChequeData.observe(viewLifecycleOwner) {
            if (it != null) {
                updateUIForExistingCheque()
            }
        }
    }

    private fun updateUIForExistingCheque() {
        binding.textViewChequeShortID.visibility = View.INVISIBLE
        binding.cardCancel.visibility = View.INVISIBLE
        binding.cardAccept.visibility = View.INVISIBLE
        binding.textViewConfirmation.text = getString(R.string.msg_cheque_has_already_been_added)
    }

    private fun handleArgs() {
        //Set short id as text
        binding.textViewChequeShortID.text = args.url.split("=")[1].substring(0, 12)
    }

    private fun setOnClickListeners() {
        binding.cardAccept.setOnClickListener {
            val id = args.url.split("=")[1]
            chequesViewModel.addCheque(ChequeEntity(id.substring(0, 12), id))

            Toast.makeText(context, getString(R.string.msg_was_added), Toast.LENGTH_SHORT).show()

            findNavController().popBackStack()
        }
        binding.cardCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}