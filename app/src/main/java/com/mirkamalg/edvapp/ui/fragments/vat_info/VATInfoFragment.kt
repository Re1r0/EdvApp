package com.mirkamalg.edvapp.ui.fragments.vat_info

import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.FragmentVatInfoBinding
import com.mirkamalg.edvapp.util.openURL

/**
 * Created by Mirkamal on 29 January 2021
 */
class VATInfoFragment : Fragment() {

    private var binding: FragmentVatInfoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        binding = FragmentVatInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureAnimation()
        setOnClickListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun configureAnimation() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 800
            addUpdateListener {
                binding?.imageButtonOpen?.alpha = it.animatedValue as Float
                binding?.textViewAboutVat?.alpha = it.animatedValue as Float
            }
            start()
        }
    }

    private fun setOnClickListeners() {
        binding?.apply {
            buttonGoBack.setOnClickListener {
                findNavController().popBackStack()
            }
            imageButtonOpen.setOnClickListener {
                context.openURL(getString(R.string.url_about_vat_wiki))
            }
        }
    }
}