package org.lee.android.sdk.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 
 * @author allnet@live.cn
 * 
 */
public class AppFunction {
	/**
	 * 为程序创建桌面快捷方式。
	 * 
	 * @param activity
	 *            指定当前的Activity为快捷方式启动的对象
	 * @param nameId
	 *            快捷方式的名称
	 * @param iconId
	 *            快捷方式的图标
	 * @param appendFlags
	 *            需要在快捷方式启动应用的Intent中附加的Flag
	 */
	public static void addShortcut(Activity activity, int nameId, int iconId,
			int appendFlags) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				activity.getString(nameId));
		shortcut.putExtra("duplicate", false);
		// 指定当前的Activity为快捷方式启动的对象
		ComponentName comp = new ComponentName(activity.getPackageName(),
				activity.getClass().getName());
		Intent intent = new Intent(Intent.ACTION_MAIN).setComponent(comp);
		if (appendFlags != 0) {
			intent.addFlags(appendFlags);
		}
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				activity, iconId);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		activity.sendBroadcast(shortcut);
	}

	/**
	 * 隐藏键盘
	 * 
	 * @param context
	 * @param view 
	 * @return
	 */
	public static boolean hideInputMethod(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

		return false;
	}

	public static boolean showInputMethod(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			return imm.showSoftInput(view, 0);
		}

		return false;
	}

	/**
	 * 双击BACK键退出程序
	 * 
	 * @author allnet@live.cn
	 * 
	 */
	public static class DoubleClickBackExit {
		/** 间隔时间 */
		private static long intervalTime = 0;

		/**
		 * 处理BACK事件，如果连续两次BACK事件在2秒之内，则退出程序。
		 * 
		 * @param activity
		 *            供Toast使用和执行Activity的退出。
		 * @param keyCode
		 * @param event
		 * @return 如果不符合退出，返回false。
		 */
		public static boolean backEvent(Activity activity, int keyCode,
				KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_UP) {
				if ((System.currentTimeMillis() - intervalTime) > 2000) {
					Log.d("TAG", "intervalTime:" + intervalTime);
					intervalTime = System.currentTimeMillis();
					android.widget.Toast.makeText(activity, "再按一次退出程序",
							android.widget.Toast.LENGTH_SHORT).show();
				} else {
					activity.finish();
				}
				return true;
			}
			return false;
		}
	}
}
