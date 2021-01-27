package com.mirkamalg.edvapp.ui.activities.splash

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.ui.activities.onboarding.OnBoardingActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.statusBarColor = Color.BLACK
        }

        configureHandler()
    }

    private fun configureHandler() {
        Handler(mainLooper).postDelayed({
            //TODO check if app is opened for the first time
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        }, 500)
    }
}