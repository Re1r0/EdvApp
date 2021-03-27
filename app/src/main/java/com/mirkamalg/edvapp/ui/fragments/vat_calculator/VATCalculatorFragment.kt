package com.mirkamalg.edvapp.ui.fragments.vat_calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.FragmentVatCalculatorBinding
import com.mirkamalg.edvapp.util.MANAT_CHAR


/**
 * Created by Mirkamal on 29 January 2021
 */
class VATCalculatorFragment : Fragment() {

    private var binding: FragmentVatCalculatorBinding? = null

    // 0 - cash, 1 - cashless
    private var paymentMethod = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVatCalculatorBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        configureCalculator()
        configureSpinner()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun configureSpinner() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(
                requireContext(),
                R.layout.item_spinner_payment_method,
                arrayOf(
                    getString(R.string.msg_cash_payment),
                    getString(R.string.msg_cashless_payment)
                )
            )
        binding?.calculatorSpinnerPaymentMethod?.apply {
            this.adapter = adapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    paymentMethod = position
                    calculateVAT(binding!!.textViewVatCalculatorInput.text.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
        }
    }

    private fun configureCalculator() {
        binding?.apply {
            cardView0.setOnClickListener {
                textViewVatCalculatorInput.text = "${textViewVatCalculatorInput.text}0"
            }
            cardView1.setOnClickListener {
                textViewVatCalculatorInput.text = "${textViewVatCalculatorInput.text}1"
            }
            cardView2.setOnClickListener {
                textViewVatCalculatorInput.text = "${textViewVatCalculatorInput.text}2"
            }
            cardView3.setOnClickListener {
                textViewVatCalculatorInput.text = "${textViewVatCalculatorInput.text}3"
            }
            cardView4.setOnClickListener {
                textViewVatCalculatorInput.text = "${textViewVatCalculatorInput.text}4"
            }
            cardView5.setOnClickListener {
                textViewVatCalculatorInput.text = "${textViewVatCalculatorInput.text}5"
            }
            cardView6.setOnClickListener {
                textViewVatCalculatorInput.text = "${textViewVatCalculatorInput.text}6"
            }
            cardView7.setOnClickListener {
                textViewVatCalculatorInput.text = "${textViewVatCalculatorInput.text}7"
            }
            cardView8.setOnClickListener {
                textViewVatCalculatorInput.text = "${textViewVatCalculatorInput.text}8"
            }
            cardView9.setOnClickListener {
                textViewVatCalculatorInput.text = "${textViewVatCalculatorInput.text}9"
            }
            cardViewDot.setOnClickListener {
                if (!textViewVatCalculatorInput.text.contains('.', false)) {
                    if (textViewVatCalculatorInput.text.isBlank()) {
                        textViewVatCalculatorInput.text = "0."
                    } else {
                        textViewVatCalculatorInput.text = "${textViewVatCalculatorInput.text}."
                    }
                }
            }
            cardViewBackSpace.setOnClickListener {
                if (textViewVatCalculatorInput.text.isNotBlank()) {
                    textViewVatCalculatorInput.text = textViewVatCalculatorInput.text.substring(
                        0,
                        textViewVatCalculatorInput.text.lastIndex
                    )
                }
            }

            textViewVatCalculatorInput.doAfterTextChanged {
                calculateVAT(it.toString())
            }
        }
    }

    private fun calculateVAT(input: String) {
        try {
            if (input.isNotBlank()) {
                val converted = input.toDouble()
                val result = if (paymentMethod == 0) {
                    (converted * 0.1).toString()
                } else {
                    (converted * 0.15).toString()
                }
                binding?.textViewVatResult?.text = if (result.length > 5) {
                    "${result.substring(0, 5).trim('.')}${MANAT_CHAR}"
                } else {
                    "${result}${MANAT_CHAR}"
                }
            } else {
                binding?.textViewVatResult?.text = "0.00${MANAT_CHAR}"
            }
        } catch (e: Exception) {
            binding?.textViewVatResult?.text = getString(R.string.err_error)
        }
    }

    private fun setOnClickListeners() {
        binding?.apply {
            buttonGoBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}