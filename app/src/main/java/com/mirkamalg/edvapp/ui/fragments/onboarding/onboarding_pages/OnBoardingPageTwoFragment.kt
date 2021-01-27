package com.mirkamalg.edvapp.ui.fragments.onboarding.onboarding_pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mirkamalg.edvapp.databinding.FragmentOnboardingPageTwoBinding
import com.mirkamalg.edvapp.ui.fragments.onboarding.OnBoardingFragment

/**
 * Created by Mirkamal on 23 January 2021
 */
class OnBoardingPageTwoFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingPageTwoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingPageTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardViewNextOnBoardingPageTwo.setOnClickListener {
            (parentFragment as OnBoardingFragment).toNextPage()
        }
    }
}