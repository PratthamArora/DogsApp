package com.prattham.dogsapp.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.prattham.dogsapp.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}
