package com.mirkamalg.edvapp.ui.activities.splash

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.ui.activities.main.MainActivity
import com.mirkamalg.edvapp.ui.activities.onboarding.OnBoardingActivity
import com.mirkamalg.edvapp.util.DONT_SHOW_ONBOARDING_SCREEN
import com.mirkamalg.edvapp.util.PreferencesManager

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
        PreferencesManager.initPreferences(this)
        val dontShowOnBoardingScreen = PreferencesManager.readBooleanPreference(
            DONT_SHOW_ONBOARDING_SCREEN, false
        )
        Log.e("HERE", dontShowOnBoardingScreen.toString())

        Handler(mainLooper).postDelayed({
            if (dontShowOnBoardingScreen) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, OnBoardingActivity::class.java))
            }
            finish()
        }, 500)
    }
}