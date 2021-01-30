package com.mirkamalg.edvapp.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Mirkamal on 30 January 2021
 */
object PreferencesManager {

    private var preferences: SharedPreferences? = null

    /**
     * @throws [java.lang.NullPointerException] if any other methods are called before initializing
     * (calling PreferencesManager#assertPreferences())
     */
    private fun assertPreferences() {
        if (preferences == null) {
            throw NullPointerException("Preferences is not initialized. Call initPreferences(activity) first.")
        }
    }


    /**
     * Initializes PreferencesManager object with 'mainSharedPreferences' using the passed context
     * @param [context] used for getting aforementioned sharedPreferences object
     */
    fun initPreferences(context: Context) {
        preferences = context.getSharedPreferences("mainSharedPreferences", Context.MODE_PRIVATE)
    }

    /**
     * Writes [String] value with the key provided
     * @param [key] key which points at the value
     * @param [string] value which is saved
     */
    fun writeStringPreference(key: String, string: String) {
        assertPreferences()
        val editor = preferences!!.edit()
        editor.putString(key, string)
        editor.apply()
    }

    /**
     * Reads [String] value which given [key] points at which is @Nullable
     * @return [String]
     */
    fun readStringPreference(key: String, default: String?): String? {
        assertPreferences()
        return preferences!!.getString(key, default)
    }

    /**
     * Writes [Int] value with the key provided
     * @param [key] key which points at the value
     * @param [int] value which is saved
     */
    fun writeIntPreference(key: String, int: Int) {
        assertPreferences()
        val editor = preferences!!.edit()
        editor.putInt(key, int)
        editor.apply()
    }

    /**
     * Reads [Int] value which given [key] points at
     * @return [Int]
     */
    fun readIntPreference(key: String, default: Int): Int {
        assertPreferences()
        return preferences!!.getInt(key, default)
    }

    /**
     * Writes [Boolean] value with the key provided
     * @param [key] key which points at the value
     * @param [int] value which is saved
     */
    fun writeBooleanPreference(key: String, boolean: Boolean) {
        assertPreferences()
        val editor = preferences!!.edit()
        editor.putBoolean(key, boolean)
        editor.apply()
    }

    /**
     * Reads [Boolean] value which given [key] points at
     * @return [Boolean]
     */
    fun readBooleanPreference(key: String, default: Boolean): Boolean {
        assertPreferences()
        return preferences!!.getBoolean(key, default)
    }
}