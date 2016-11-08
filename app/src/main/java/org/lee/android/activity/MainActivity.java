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

import org.lee.android.activity.BackgroundServices.LocalBinder;
import org.lee.android.activity.BackgroundServices.Notification;
import org.lee.android.sdk.util.AppFunction;
import org.lee.android.simples.baidupicture.R;
import org.lee.framework.print.Lg;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.Window;

/**
 * 
 * Email:allnet@live.cn </p>
 * 
 */

public class MainActivity extends DrawerActivity {

	private NotifycationViewHolder mNotifycationView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);
		mNotifycationView = new NotifycationViewHolder(this);
		if (savedInstanceState == null) {
			mUIController.onHomePage();
		}
		Notification nf = mUIController.readNotification();
		if (nf != null)
			notify(nf);
		startBackgroundService();
		AppFunction.addShortcut(this, R.string.app_name,
				R.drawable.ic_launcher, 0);
	}

	public UIController getUIController() {
		return mUIController;
	}

	private void startBackgroundService() {
		Intent service = new Intent(this, BackgroundServices.class);
		bindService(service, iServiceConnection, 0);
		startService(service);
	}

	private BackgroundServices mServices;
	private ServiceConnection iServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			mServices = (BackgroundServices) ((LocalBinder) binder)
					.getService();
			mServices.setController(mUIController);
			mUIController.setService(mServices);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	};

	@Override
	public void onResume() {
		super.onResume();
		mUIController.onResume();
		if (mNotifycationView.isEnable())
			mNotifycationView.show();
	}

	@Override
	public void onPause() {
		super.onPause();
		mUIController.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionbar_main_search:
			// UIController.openBrowser(this, "https://www.google.com");
			Intent intent = new Intent(this, SearchActivity.class);
			intent.putExtra("words", "");
			startActivity(intent);
			return true;
		case R.id.actionbar_main_adv:
//			Intent update = new Intent(this, AdvActivity.class);
//			startActivity(update);
			return true;
		case R.id.actionbar_main_official:
			mNotifycationView.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent intent) {
		super.onActivityResult(arg0, arg1, intent);
		if (intent == null) {
			return;
		}
		String words = intent.getStringExtra("words");
		Lg.anchor(words);
	}

	@Override
	protected void onDestroy() {
		Intent service = new Intent(this, BackgroundServices.class);
		stopService(service);
		super.onDestroy();
		System.exit(0);
	}

	public void notify(Notification noti) {
		mNotifycationView.setNotify(noti);
		mNotifycationView.show();
		if (noti.messageType == Notification.Type.NOTIFY_TYPE_UPDATE) {
			update = true;
			invalidateOptionsMenu();
		}
	}

}