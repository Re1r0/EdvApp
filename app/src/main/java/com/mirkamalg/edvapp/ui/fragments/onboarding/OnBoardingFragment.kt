package com.mirkamalg.edvapp.ui.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mirkamalg.edvapp.databinding.FragmentOnboardingBinding
import com.mirkamalg.edvapp.ui.fragments.onboarding.onboarding_pages.OnBoardingPageOneFragment
import com.mirkamalg.edvapp.ui.fragments.onboarding.onboarding_pages.OnBoardingPageThreeFragment
import com.mirkamalg.edvapp.ui.fragments.onboarding.onboarding_pages.OnBoardingPageTwoFragment

/**
 * Created by Mirkamal on 23 January 2021
 */
class OnBoardingFragment : Fragment() {

    private var binding: FragmentOnboardingBinding? = null
    private lateinit var adapter: OnBoardingPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewPager()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun configureViewPager() {
        val fragments = listOf(
            OnBoardingPageOneFragment(),
            OnBoardingPageTwoFragment(),
            OnBoardingPageThreeFragment()
        )

        adapter = OnBoardingPagerAdapter(this, fragments)
        binding?.viewPagerOnBoarding?.apply {
            adapter = this@OnBoardingFragment.adapter
            setPageTransformer(OnBoardingPagerTransformer())
            isUserInputEnabled = false
        }
    }

    fun toNextPage() {
        binding?.viewPagerOnBoarding?.currentItem = binding?.viewPagerOnBoarding?.currentItem?.plus(
            1
        ) ?: return
    }
}