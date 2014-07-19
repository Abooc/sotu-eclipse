package org.lee.android.sdk.properties;

import android.content.res.Resources;

import com.abooc.sdk.update.app.AppContext;

import com.abooc.android.baidupicture.R;

public interface PreferencesKeys {

	Resources res = AppContext.getContext().getResources();

	final String pref_key_feedback = res.getString(R.string.pre_key_feedback);
	final String pref_key_update = res.getString(R.string.pre_key_update);

}
