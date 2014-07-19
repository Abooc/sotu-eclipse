package com.abooc.sdk.update.app;

import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;

import org.lee.android.activity.NotificationInfoActivity;
import org.lee.android.app.database.ImagesDbHelper;
import org.lee.android.app.preset.PresetReader;
import org.lee.android.json.network.APIServices;
import org.lee.android.json.network.NetworkTasker;
import org.lee.android.sdk.util.Downloader;
import org.lee.android.sdk.util.ShareManager;
import com.abooc.android.baidupicture.R;
import org.lee.android.util.Toast;
import org.lee.android.volley.VolleyLoader;
import org.lee.java.util.Empty;

import java.io.File;

public class AppApplication extends Application {

	private AppContext mAppContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mAppContext = AppContext.getInstance();
		mAppContext.setContext(this);
		APIServices.init(this);
		Toast.init(this);
		NetworkTasker.initialize(this);
		VolleyLoader.initialize(this);
		init();
	}

	private void init() {
		PresetReader.initialize(this);
		ImagesDbHelper.initialize(this);

		registerReceiver(receiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		mAppContext.destroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	public static void openGooglePlay(Context context) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		final String appPackageName = context.getPackageName();
		try {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse(ShareManager.get_google_play_app_url(context))));
		} catch (android.content.ActivityNotFoundException anfe) {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("http://play.google.com/store/apps/details?id="
							+ appPackageName)));
		}
	}

	private long mDownloadId;

	public void setDownloadId(long id) {
		mDownloadId = id;
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String apkPath = Downloader.getInstance(AppApplication.this)
					.queryDownloadStatus(mDownloadId);
			if (!Empty.isEmpty(apkPath) && apkPath.endsWith(".apk")) {
				saveNotify(NotificationInfoActivity.SP_KEY_FILE_APK_URI,
						apkPath);
				instalApk(new File(apkPath));
			}
		}
	};

	private boolean saveNotify(String key, String value) {
		SharedPreferences preferences = getSharedPreferences(
				getString(R.string.app_name), Context.MODE_PRIVATE);
		return preferences.edit().putString(key, value).commit();
	}

	public void instalApk(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

}
