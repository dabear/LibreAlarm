package com.pimpimmobile.librealarm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.HashMap;

public class Preferences extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private HashMap<String, String> mChanged = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        setResult();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getString(R.string.pref_key_glucose_interval).equals(key)) {
            String value = sharedPreferences.getString(key, "10");
            if (TextUtils.isEmpty(value) || Integer.valueOf(value) < 1) {
                sharedPreferences.edit().putString(key, "1").apply();
            }
        }
        mChanged.put(key, sharedPreferences.getAll().get(key).toString());
    }

    private void setResult() {
        Intent result = new Intent();
        Bundle bundle = new Bundle();
        for (String key : mChanged.keySet()) {
            bundle.putString(key, mChanged.get(key));
        }
        result.putExtra("result", bundle);
        setResult(RESULT_OK, result);
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}