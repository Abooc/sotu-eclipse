package com.abooc.sdk.update;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * @author allnet@live.cn
 * @title 升级任务
 */
public class UpdateTask extends AsyncTask<String, Object, Apk> {
	private Context mContext;
	private File localFile;
	private ProgressDialog progressDialog;
	private static Handler UIThread;

	public static final int CHECKUPDATE = 0;
	public static final int DOWNLOAD = 1;
	// http://rom.16tree.com/apituzi/index/p/tuzi3{channelName}/v/{versionCode}
	// http://rom.16tree.com/apituzi/index/p/tuziPhone/v/
	public static final String CHECKUPDATE_URL = "http://rom.16tree.com/apituzi/index/p/tuziPhone-{channelName}/v/{versionCode}.{svn}";

	public UpdateTask(Context ctx) {
		mContext = ctx;
	}

	/**
	 * 拼装检查版本更新的地址
	 * 
	 * @param context
	 * @return
	 */
	public static String assemblyUrl(Context context) {
		String channelName = readApplicationInfo(context, "CHANNEL");
		String versionCode = getVersionCode(context) + "";
		String svn = readApplicationInfo(context, "SVN");
		String url = CHECKUPDATE_URL.replace("{channelName}", channelName);
		url = url.replace("{versionCode}", versionCode);
		url = url.replace("{svn}", svn);
		return url;
	}

	public static String readApplicationInfo(Context context, String key) {
		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			return appInfo.metaData.getString("CHANNEL");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@SuppressLint("HandlerLeak")
	@Override
	protected void onPreExecute() {
		UIThread = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWNLOAD:
					showDialog((Apk) msg.obj, false);// 提示下载
					break;
				default:
					onDownloadProgress(msg.arg1, msg.arg2);
					break;
				}
			}
		};

		progressDialog = new ProgressDialog(mContext);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("loading...");
		progressDialog.show();
	}

	@Override
	protected Apk doInBackground(String... params) {
		int action = Integer.valueOf(params[0]);
		String url = (params[1]);
		switch (action) {
		case CHECKUPDATE:
			String json = getHttpContent(url);
			Apk apk = onLoadFinish(json);
			return apk;
		case DOWNLOAD:
			onDownload(url);
			onPostExecute(null);
			return null;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Apk apk) {
		progressDialog.dismiss();
		if (apk == null) {// 下载完成，可直接安装
			installApk(mContext, localFile);
			return;
		}
		try {
			if (apk.update()) {
				if (!apk.vname.endsWith("apk")) {
					apk.vname += ".apk";
				}
				localFile = new File(mContext.getFilesDir(), apk.vname);
				if (localFile.exists()) {
					if (apk.md5.equals(MD5Util.getFileMD5String(localFile))) {
						showDialog(apk, true); //  提示安装
						return;
					} else {
						localFile.delete();
					}
				}
				if (!localFile.getParentFile().exists()) {
					localFile.getParentFile().mkdirs();
				}
				// 提示下载
				showDialog(apk, false);
			} else {
				Toast.makeText(mContext, "已经是最新版本!", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(mContext, "出现错误！", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	private Apk onLoadFinish(String json) {
		Gson gson = new Gson();
		Apk apk = gson.fromJson(json, Apk.class);
		return apk;
	}

	private void showDialog(final Apk apk, final boolean exist) {
		Builder iBuilder = new Builder(mContext);
		iBuilder.setTitle("有新版发布！").setMessage(apk.desc);
		iBuilder.setCancelable(!apk.update());

		if (!apk.update()) {
			iBuilder.setNegativeButton("取消", null);
		}
		iBuilder.setPositiveButton(exist ? "立即安装" : "马上升级",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (exist) {
							installApk(mContext, localFile);
						} else {
							onDownloadStart();
							execute(new Runnable() {
								@Override
								public void run() {
									doInBackground("" + DOWNLOAD, apk.url);
								}
							});
						}
					}
				});
		iBuilder.create().show();
	}

	private void onDownloadStart() {
		progressDialog.setMessage("下载中...");
		progressDialog.show();
	}

	private void onDownloadProgress(long length, long current) {
		float lengthSize = ((float) length / (1024 * 1024));
		float currentSize = ((float) current / (1024 * 1024));
		DecimalFormat df = new DecimalFormat("0.00");
		progressDialog.setMessage((df.format(currentSize)) + "/"
				+ (df.format(lengthSize)) + "MB 下载中...");
		progressDialog.show();
	}

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	private File onDownload(String downloadUrl) {
		try {
			URL url = new URL(downloadUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			final InputStream input = conn.getInputStream();
			FileOutputStream fos = null;
			fos = mContext.openFileOutput(localFile.getName(),
					Context.MODE_WORLD_READABLE);

			final int length = conn.getContentLength();

			final byte[] buf = new byte[1024];
			int nRead;
			Message msg;
			while ((nRead = input.read(buf, 0, 1024)) > 0) {
				fos.write(buf, 0, nRead);
				msg = new Message();
				msg.arg1 = length;
				msg.arg2 = (int) (localFile.length());
				UIThread.sendMessage(msg);
			}
			fos.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return localFile;
	}

	public static void installApk(Context context, File apkFile) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(apkFile),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public String getHttpContent(String strUrl) {
		InputStream is = null;
		try {

			URL url = new URL(strUrl);
			HttpURLConnection urlCon = null;

			urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setDoOutput(true);
			urlCon.setConnectTimeout(5000);
			urlCon.setDoInput(true);
			urlCon.setRequestMethod("POST");
			urlCon.setUseCaches(false);

			is = urlCon.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}

			return b.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}

		return null;
	}

}

class Apk {
	/** =“1” 的时候说明是有升级版本 */
	public String f;
	/** 下载地址 */
	public String url;
	/**  */
	public String vname;
	/**  */
	public String vcode;
	/**  */
	public String md5;
	/**  */
	public String e_update;
	/** 升级Note */
	public String desc;

	/**
	 * true代表强制升级
	 * 
	 * @return
	 */
	public boolean update() {
		return f != null && "1".equals(f);
	}
}
