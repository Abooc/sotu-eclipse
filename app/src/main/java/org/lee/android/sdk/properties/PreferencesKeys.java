package org.lee.android.sdk.properties;

import org.lee.android.app.AppContext;

import android.content.res.Resources;

import org.lee.android.simples.baidupicture.R;

public interface PreferencesKeys {

	Resources res = AppContext.getContext().getResources();

	final String pref_key_feedback = res.getString(R.string.pre_key_feedback);
	final String pref_key_update = res.getString(R.string.pre_key_update);

}
