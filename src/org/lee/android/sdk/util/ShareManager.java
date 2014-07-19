package org.lee.android.sdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.abooc.android.baidupicture.R;

public class ShareManager {

	private static ShareManager mInstance;

	private ShareManager() {

	}

	public static ShareManager getInstance() {
		if (mInstance == null) {
			// synchronized (ShareManager.this) {
			mInstance = new ShareManager();
			// }
		}
		return mInstance;
	}

	private static String google_play_app_url;

	String title = "标题title";
	String content = "分享一下应用，可以看海量美女、帅哥、汽车，还有‘宅男女神’的专属图片等图片！"
			+ "\n没兴趣？！看看也无妨，\n访问" + google_play_app_url;

	public static String get_google_play_app_url(Context context) {
		return context.getString(R.string.google_paly_app_url);
	}

	// activity
	// .getString(R.string.circles_messagetrends_item_share_end)

	public void share(Activity activity, String title, String header,
			String content, String footer) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TITLE, title);
		intent.putExtra(Intent.EXTRA_TEXT, content + "\n" + header);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(Intent.createChooser(intent, title));
	}

}
