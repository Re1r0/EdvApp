package com.mirkamalg.edvapp.ui.fragments.onboarding

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * Created by Mirkamal on 24 January 2021
 */
class OnBoardingPagerTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.translationX = -position * page.width
        page.alpha = 1 - abs(position)
    }
}