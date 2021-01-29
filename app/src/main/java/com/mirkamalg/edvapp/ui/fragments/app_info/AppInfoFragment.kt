package com.mirkamalg.edvapp.ui.fragments.app_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.FragmentAppInfoBinding
import com.mirkamalg.edvapp.util.makeColored
import com.mirkamalg.edvapp.util.makeLinks
import com.mirkamalg.edvapp.util.openURL

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
        configureSpans()
    }

    private fun configureSpans() {
        binding.apply {
            textViewDescription.makeColored(
                listOf(getString(R.string.msg_e_kassa_url)),
                R.color.colorPrimary
            )
            textViewDescription.makeLinks(
                Pair(
                    getString(R.string.msg_e_kassa_url),
                    object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            context.openURL(getString(R.string.url_e_kassa))
                        }

                    })
            )
        }
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