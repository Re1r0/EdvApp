package com.mirkamalg.edvapp.ui.fragments.onboarding.onboarding_pages

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.FragmentOnboardingPageThreeBinding
import com.mirkamalg.edvapp.ui.activities.main.MainActivity

/**
 * Created by Mirkamal on 23 January 2021
 */
class OnBoardingPageThreeFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingPageThreeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingPageThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureMotionLayout()
    }

    private fun configureMotionLayout() {
        binding.motionLayoutOnBoardingPageThree.addTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                binding.imageViewArrowUpOnBoardingPageThree.alpha = 1 - p3
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if (p0?.progress == 1f) {
                    p0.getTransition(R.id.transitionSwipeUpOnBoardingPageThree)?.setEnable(false)

                    ValueAnimator.ofFloat(0.0f, 1f).apply {
                        duration = 600
                        addUpdateListener {
                            val value = (it.animatedValue as Float)
                            binding.textViewPostAnimationOnBoardingPageThree.alpha = value
                            if (value == 1f) {
                                //TODO add shared preference that onboarding screen has been shown
                                Handler(Looper.getMainLooper()).postDelayed({
                                    startActivity(Intent(context, MainActivity::class.java))
                                    activity?.finish()
                                }, 1000)
                            }
                        }
                        start()
                    }
                }
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

        })
    }

}