package com.sqr.dabbawalaapplication;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {
	
	private EditTextPreference mIPAddress;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.settings);
		
		mIPAddress = (EditTextPreference) getPreferenceScreen().findPreference("ipAddress");
		
		mIPAddress.setSummary(PreferenceManager.getDefaultSharedPreferences(this).getString("ipAddress", "192.168.4.4"));
	}

	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
		
		if(key.equals("ipAddress"))
		{
			mIPAddress.setSummary(sharedPreferences.getString(key, "192.168.4.4"));
		}
	
	}
}
