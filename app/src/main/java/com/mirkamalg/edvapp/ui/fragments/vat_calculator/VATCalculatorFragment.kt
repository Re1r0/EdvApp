package com.mirkamalg.edvapp.ui.fragments.vat_calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mirkamalg.edvapp.databinding.FragmentVatCalculatorBinding

/**
 * Created by Mirkamal on 29 January 2021
 */
class VATCalculatorFragment : Fragment() {

    private lateinit var binding: FragmentVatCalculatorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVatCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.apply {
            buttonGoBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}