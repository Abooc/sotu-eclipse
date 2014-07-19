/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lee.android.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.abooc.sdk.update.app.AppApplication;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import org.lee.android.activity.BackgroundServices.Notification;
import org.lee.android.sdk.util.Downloader;
import com.abooc.android.baidupicture.R;
import org.lee.java.util.Empty;

import java.io.File;

public class NotificationInfoActivity extends Activity {

	public static final String INTENT_KEY_N = "intent.key.notify";
	public static final String SP_KEY_FILE_APK_URI = "sp.key.file.apk.uri";
	private Notification mNotification;
	private TextView mNameText;
	private TextView mVersionText;
	private TextView mContentText;
	private TextView mDownloadText;
	private TextView mGoogleText;

	private AdView adView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		installView();
		addListener();

		parseIntent();
		if (mNotification != null) {
			attachData();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private String readPrefrence(String key) {
		SharedPreferences preferences = getSharedPreferences(
				getString(R.string.app_name), Context.MODE_PRIVATE);
		return preferences.getString(key, null);
	}

	private void parseIntent() {
		Object obj = getIntent().getParcelableExtra(INTENT_KEY_N);
		if (obj != null && obj instanceof Notification) {
			mNotification = (Notification) obj;
		}
	}

	private void installView() {
		setContentView(R.layout.activity_notify_info);
		mNameText = (TextView) findViewById(R.id.notifyName);
		mVersionText = (TextView) findViewById(R.id.versionCode);
		mContentText = (TextView) findViewById(R.id.content);

		mDownloadText = (TextView) findViewById(R.id.Download);
		mGoogleText = (TextView) findViewById(R.id.openGooglePlay);
	}

	@Override
	protected void onResume() {
		super.onResume();
		String apk_uri = readPrefrence(SP_KEY_FILE_APK_URI);
		if (Empty.isEmpty(apk_uri)) {
			mDownloadText.setTag(null);
			mDownloadText.setText(R.string.download_normal);
		} else {
			File file = new File(apk_uri);
			if (file.exists()) {
				mDownloadText.setTag(apk_uri);
				mDownloadText.setText(R.string.localInstall);
			} else {
				mDownloadText.setTag(null);
				mDownloadText.setText(R.string.download_normal);
			}
		}
	}

	private void attachData() {
		mNameText.setText(mNotification.name);
		mVersionText.setText("NO." + mNotification.versionCode
				+ mNotification.id);
		mContentText.setText(mNotification.content);
		if (mNotification.messageType == Notification.Type.NOTIFY_TYPE_UPDATE) {
			mGoogleText.setText(R.string.open_googlepaly_update);
		} else {
			mGoogleText.setText(R.string.open_googlepaly_pingfen);
		}
	}

	private void addListener() {
		OnClickListener l = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int id = v.getId();
				switch (id) {
				case R.id.Download:
					boolean b = true;
					if(b){
						return;
					}
					if (mDownloadText.getText().equals(
							getString(R.string.localInstall))) {
						String apk_uri = (String) mDownloadText.getTag();
						AppApplication app = (AppApplication) getApplication();
						app.instalApk(new File(apk_uri));
						return;
					}
					Downloader D = Downloader
							.getInstance(getApplicationContext());
					String name = getString(R.string.app_name);
					String version = getString(R.string.versionName);
					long downloadId = D.downloadApk(name + version,
							getString(R.string.apk_download_url));
					AppApplication app = (AppApplication) getApplication();
					app.setDownloadId(downloadId);
					break;
				case R.id.openGooglePlay:
					EasyTracker easyTracker = EasyTracker
							.getInstance(NotificationInfoActivity.this);
					easyTracker.send(MapBuilder.createEvent("4001", "4011",
							mGoogleText.getText().toString(), null).build());
					AppApplication
							.openGooglePlay(NotificationInfoActivity.this);
					break;
				}
			}
		};
		mDownloadText.setOnClickListener(l);
		mGoogleText.setOnClickListener(l);
	}

	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

}