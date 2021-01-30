package com.mirkamalg.edvapp.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Mirkamal on 30 January 2021
 */
object PreferencesManager {

    private var preferences: SharedPreferences? = null

    private fun assertPreferences() {
        if (preferences == null) {
            throw NullPointerException("Preferences is not initialized. Call initPreferences(activity) first.")
        }
    }


    fun initPreferences(context: Context) {
        preferences = context.getSharedPreferences("mainSharedPreferences", Context.MODE_PRIVATE)
    }

    fun writeStringPreference(key: String, string: String) {
        assertPreferences()
        val editor = preferences!!.edit()
        editor.putString(key, string)
        editor.apply()
    }

    fun readStringPreference(key: String, default: String?): String? {
        assertPreferences()
        return preferences!!.getString(key, default)
    }

    fun writeIntPreference(key: String, int: Int) {
        assertPreferences()
        val editor = preferences!!.edit()
        editor.putInt(key, int)
        editor.apply()
    }

    fun readIntPreference(key: String, default: Int): Int {
        assertPreferences()
        return preferences!!.getInt(key, default)
    }

    fun writeBooleanPreference(key: String, boolean: Boolean) {
        assertPreferences()
        val editor = preferences!!.edit()
        editor.putBoolean(key, boolean)
        editor.apply()
    }

    fun readBooleanPreference(key: String, default: Boolean): Boolean {
        assertPreferences()
        return preferences!!.getBoolean(key, default)
    }
}