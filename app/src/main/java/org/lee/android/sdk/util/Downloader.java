package org.lee.android.sdk.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.lee.android.util.Log;
import org.lee.android.util.Toast;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import org.lee.android.simples.baidupicture.R;

public class Downloader {

	private static Downloader iDownloader;
	private static DownloadManager downloadManager;

	private Downloader(Context context) {
		downloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		STATUS_RUNNING = context.getString(R.string.save_running);
		STATUS_SUCCESSFUL = context.getString(R.string.save_successful);
		STATUS_FAILED = context.getString(R.string.save_failed);
	}

	public static Downloader getInstance(Context context) {
		if (iDownloader == null) {
			iDownloader = new Downloader(context);
		}
		return iDownloader;
	}

	public long download(String title, String url) {
		Uri resource = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(resource);
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
				| Request.NETWORK_WIFI);
		request.setAllowedOverRoaming(false);
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
				.getFileExtensionFromUrl(url));
		request.setMimeType(mimeString);
		request.setShowRunningNotification(true);
		request.setVisibleInDownloadsUi(true);
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss.SSSZ", Locale.getDefault());
		String date = formatter.format(System.currentTimeMillis());
		request.setDestinationInExternalPublicDir("/pictures/", title + date
				+ ".jpg");
		request.setTitle(title);
		return downloadManager.enqueue(request);
	}

	/**
	 * 如果服务器不支持中文路径的情况下需要转换url的编码。
	 * 
	 * @param string
	 * @return
	 */
	public String encodeGB(String string) {
		// 转换中文编码
		String split[] = string.split("/");
		for (int i = 1; i < split.length; i++) {
			try {
				split[i] = URLEncoder.encode(split[i], "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			split[0] = split[0] + "/" + split[i];
		}
		split[0] = split[0].replaceAll("\\+", "%20");// 处理空格
		return split[0];
	}

	private String STATUS_RUNNING;
	private String STATUS_SUCCESSFUL;
	private String STATUS_FAILED;

	public String queryDownloadStatus(long id) {
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(id);
		Cursor cursor = downloadManager.query(query);
		if (cursor.moveToFirst()) {
			int status = cursor.getInt(cursor
					.getColumnIndex(DownloadManager.COLUMN_STATUS));
			switch (status) {
			case DownloadManager.STATUS_PAUSED:
				Log.anchor("STATUS_PAUSED");
			case DownloadManager.STATUS_PENDING:
				Log.anchor("STATUS_PENDING");
			case DownloadManager.STATUS_RUNNING:
				// 正在下载，不做任何事情
				Log.anchor(STATUS_RUNNING);
				Toast.show(STATUS_RUNNING);
				break;
			case DownloadManager.STATUS_SUCCESSFUL:
				// 完成
				Toast.show(STATUS_SUCCESSFUL);
				return cursor.getString(cursor
						.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
			case DownloadManager.STATUS_FAILED:
				Toast.show(STATUS_FAILED);
				// 清除已下载的内容，重新下载
				Log.anchor(STATUS_FAILED);
				downloadManager.remove(id);
				break;
			}
			return null;
		} else {
			return null;
		}
	}

	public long downloadApk(String title, String url) {
		Uri resource = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(resource);
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
				| Request.NETWORK_WIFI);
		request.setAllowedOverRoaming(false);
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
				.getFileExtensionFromUrl(url));
		request.setMimeType(mimeString);
		request.setShowRunningNotification(true);
		request.setVisibleInDownloadsUi(true);
		request.setDestinationInExternalPublicDir("/download/",
				title + System.currentTimeMillis() + ".apk");
		request.setTitle(title);
		return downloadManager.enqueue(request);
	}

}
