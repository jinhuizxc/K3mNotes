package com.k3mshiro.k3mnotes.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.k3mshiro.k3mnotes.R;
import com.k3mshiro.k3mnotes.aconstant.ConstantUtil;
import com.k3mshiro.k3mnotes.dao.PreferenceKeys;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private PreferenceKeys preferenceKeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences_layout);
        preferenceKeys = new PreferenceKeys(getResources());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(preferenceKeys.night_mode_pref_key)) {
            SharedPreferences themePreferences = getActivity()
                    .getSharedPreferences(ConstantUtil.THEME_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor themeEditor = themePreferences.edit();
            //Tell MainLayout to recreate itself because mode has changed
            themeEditor.putBoolean(ConstantUtil.RECREATE_ACTIVITY, true);

            SwitchPreference switchPreference = (SwitchPreference) findPreference(preferenceKeys.night_mode_pref_key);
            if (switchPreference.isChecked()) {
                themeEditor.putString(ConstantUtil.THEME_SAVED, ConstantUtil.DARKTHEME);
            } else {
                themeEditor.putString(ConstantUtil.THEME_SAVED, ConstantUtil.LIGHTTHEME);
            }
            themeEditor.apply();

            getActivity().recreate();
        } else if (key.equals(preferenceKeys.view_mode_pref_key)) {
            SharedPreferences viewModePreferences = getActivity()
                    .getSharedPreferences(ConstantUtil.VIEW_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor viewModeEditor = viewModePreferences.edit();
            //Tell MainLayout to recreate itself because mode has changed
            viewModeEditor.putBoolean(ConstantUtil.RECREATE_ACTIVITY, true);

            ListPreference listPreference = (ListPreference) findPreference(preferenceKeys.view_mode_pref_key);
            String currentValue = listPreference.getValue();
            if (currentValue.equals("3")) {
                viewModeEditor.putString(ConstantUtil.VIEWMODE_SAVED, ConstantUtil.REMINDERMODE);
                listPreference.setSummary(R.string.view_mode_reminder);
            } else if (currentValue.equals("2")) {
                viewModeEditor.putString(ConstantUtil.VIEWMODE_SAVED, ConstantUtil.FAVORITEMODE);
                listPreference.setSummary(R.string.view_mode_favorite);
            } else {
                viewModeEditor.putString(ConstantUtil.VIEWMODE_SAVED, ConstantUtil.ALLMODE);
                listPreference.setSummary(R.string.view_mode_all);
            }
            viewModeEditor.apply();

            getActivity().recreate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
