package com.mirkamalg.edvapp.ui.fragments.app_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mirkamalg.edvapp.databinding.FragmentAppInfoBinding

/**
 * Created by Mirkamal on 28 January 2021
 */
class AppInfoFragment : Fragment() {

    private lateinit var binding: FragmentAppInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppInfoBinding.inflate(inflater, container, false)
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
            imageButtonUpdate.setOnClickListener {
                //TODO implement
            }
        }
    }
}