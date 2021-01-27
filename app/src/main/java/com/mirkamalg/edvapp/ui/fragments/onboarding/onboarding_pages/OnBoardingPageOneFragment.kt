package com.mirkamalg.edvapp.ui.fragments.onboarding.onboarding_pages

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mirkamalg.edvapp.databinding.FragmentOnboardingPageOneBinding
import com.mirkamalg.edvapp.ui.fragments.onboarding.OnBoardingFragment

/**
 * Created by Mirkamal on 23 January 2021
 */
class OnBoardingPageOneFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingPageOneBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingPageOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureAnimations()
        setOnClickListeners()

    }

    private fun setOnClickListeners() {
        binding.cardViewNextOnBoardingPageOne.setOnClickListener {
            (parentFragment as OnBoardingFragment).toNextPage()
        }
    }

    private fun configureAnimations() {
        ValueAnimator.ofFloat(0.0f, 1.0f).apply {
            duration = 600
            addUpdateListener {
                binding.textViewOnBoardingPageOne.alpha = (it.animatedValue as Float)
            }
            start()
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            ValueAnimator.ofFloat(0.0f, 1.0f).apply {
                duration = 600
                addUpdateListener {
                    binding.imageViewOnBoardingPageOne.alpha = (it.animatedValue as Float)
                }
                start()
            }
        }, 150)
        handler.postDelayed({
            ValueAnimator.ofFloat(0.0f, 1.0f).apply {
                duration = 600
                addUpdateListener {
                    binding.cardViewNextOnBoardingPageOne.alpha = (it.animatedValue as Float)
                }
                start()
            }
        }, 300)
    }
}