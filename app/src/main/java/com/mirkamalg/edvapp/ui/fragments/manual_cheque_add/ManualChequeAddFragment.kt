package com.mirkamalg.edvapp.ui.fragments.manual_cheque_add

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.FragmentManualChequeAddBinding
import com.mirkamalg.edvapp.model.entities.ChequeEntity
import com.mirkamalg.edvapp.viewmodels.ChequesViewModel
import java.util.*

/**
 * Created by Mirkamal on 30 January 2021
 */
class ManualChequeAddFragment : Fragment() {

    private lateinit var binding: FragmentManualChequeAddBinding

    private val chequesViewModel: ChequesViewModel by navGraphViewModels(R.id.nav_graph_main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManualChequeAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        configureValidation()
        configureObservers()
        configureSuccessAnimation()
    }

    private fun configureObservers() {
        chequesViewModel.checkedChequeEntity.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.textInputLayoutShortID.error =
                    getString(R.string.msg_cheque_has_already_been_added)
            }
        }
    }

    private fun configureValidation() {
        binding.textInputEditTextShortID.doAfterTextChanged {
            if (it?.length != 12) {
                binding.textInputLayoutShortID.error =
                    getString(R.string.err_short_id_must_be_twelve)
            } else {
                binding.textInputLayoutShortID.error = null
                chequesViewModel.getChequeDetailsFromDatabaseByShortID(it.toString())
            }
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            buttonAdd.setOnClickListener {
                if (textInputLayoutShortID.error == null) {
                    if (textInputEditTextShortID.text?.isNotBlank() == true) {
                        chequesViewModel.addCheque(
                            ChequeEntity(
                                UUID.randomUUID().toString(),
                                binding.textInputEditTextShortID.text.toString()
                            )
                        )
                        binding.lottieAnimationViewSuccess.apply {
                            isVisible = true
                            progress = 0f
                            playAnimation()
                        }
                    } else {
                        textInputLayoutShortID.error =
                            getString(R.string.err_short_id_cannot_be_blank)
                    }
                }
            }
            buttonGoBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun configureSuccessAnimation() {
        binding.lottieAnimationViewSuccess.addAnimatorUpdateListener {
            if ((it.animatedValue as Float) >= 0.95f) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.apply {
                        lottieAnimationViewSuccess.apply {
                            isVisible = false
                            progress = 0f
                        }
                        textInputEditTextShortID.text?.clear()
                    }
                }, 300)
            }
        }
    }
}