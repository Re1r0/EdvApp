package com.mirkamalg.edvapp.ui.dialogs.delete_cheque_confirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.DialogDeleteChequeConfirmationBinding
import com.mirkamalg.edvapp.viewmodels.ChequesViewModel

/**
 * Created by Mirkamal on 07 February 2021
 */
class DeleteChequeConfirmationFragment : DialogFragment() {

    private lateinit var binding: DialogDeleteChequeConfirmationBinding

    private val chequesViewModel: ChequesViewModel by navGraphViewModels(R.id.nav_graph_main)

    private val args: DeleteChequeConfirmationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteChequeConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.apply {
            buttonDelete.setOnClickListener {
                chequesViewModel.deleteCheque(args.chequeEntity)

                // Send data to cheques fragment in order to update cheques list
                findNavController().apply {
                    previousBackStackEntry?.savedStateHandle?.set(
                        "deleteChequeKey",
                        args.chequeEntity.shortDocumentId
                    )
                    popBackStack()
                }
            }
            buttonCancel.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}