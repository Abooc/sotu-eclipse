package org.lee.android.sdk.properties;

import org.lee.a.b.PA;
import org.lee.android.simples.baidupicture.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * 设置/关于页面
 * 
 * @author ruiyuLee
 * 
 */
public class AboutActivity extends PA implements PreferencesKeys,
		OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getPreferenceManager().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(pref_key_feedback)) {
			return;
		}
		if (key.equals(pref_key_update)) {
			return;
		}
	}
}
