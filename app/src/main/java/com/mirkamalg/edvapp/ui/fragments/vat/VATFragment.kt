package com.mirkamalg.edvapp.ui.fragments.vat

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.mirkamalg.edvapp.databinding.FragmentVatBinding

/**
 * Created by Mirkamal on 29 January 2021
 */
class VATFragment : Fragment() {

    private lateinit var binding: FragmentVatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = null

        binding = FragmentVatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.buttonGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imageButtoninfoVat.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.imageButtoninfoVat to "infoImageVat")
            findNavController().navigate(
                VATFragmentDirections.actionVATFragmentToVATInfoFragment(),
                extras
            )
        }
    }
}