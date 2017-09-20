package com.k3mshiro.k3mnotes.dao;

import android.content.res.Resources;

import com.k3mshiro.k3mnotes.R;

public class PreferenceKeys {
    public final String night_mode_pref_key;
    public final String view_mode_pref_key;
    public final String pass_mode_pref_key;
    public final String pass_code_pref_key;

    public PreferenceKeys(Resources resources) {
        night_mode_pref_key = resources.getString(R.string.night_mode_pref_key);
        view_mode_pref_key = resources.getString(R.string.view_mode_pref_key);
        pass_mode_pref_key = resources.getString(R.string.pass_mode_pref_key);
        pass_code_pref_key = resources.getString(R.string.pass_code_pref_key);
    }
}
