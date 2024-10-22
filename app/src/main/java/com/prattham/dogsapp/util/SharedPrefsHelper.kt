package com.prattham.dogsapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharedPrefsHelper {

    companion object {
        private const val PREF_TIME = "Pref Time"
        private var prefs: SharedPreferences? = null

        @Volatile
        private var instance: SharedPrefsHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPrefsHelper = instance ?: synchronized(LOCK) {
            instance ?: buildHelper(context).also {
                instance = it
            }
        }

        private fun buildHelper(context: Context): SharedPrefsHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPrefsHelper()

        }
    }

    fun saveUpdateTime(time: Long) {
        prefs?.edit(commit = true) { putLong(PREF_TIME, time) }
    }

    fun getUpdateTime() = prefs?.getLong(PREF_TIME, 0)

    fun getCacheDuration() = prefs?.getString("pref_cache_duration", "")
}