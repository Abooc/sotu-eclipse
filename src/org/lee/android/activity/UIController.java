package org.lee.android.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;
import org.lee.android.activity.BackgroundServices.Notification;
import org.lee.android.app.bean.Channel;
import org.lee.android.fragment.ErrorFragment;
import org.lee.android.fragment.GalleryFragment;
import org.lee.android.fragment.MainFragment;
import org.lee.android.fragment.PagerFragment;
import com.abooc.android.baidupicture.R;
import org.lee.framework.print.Lg;
import org.lee.java.util.Empty;

public class UIController implements UI {

	private Activity mActivity;
	private BackgroundServices iBackgroundService;

	public UIController(Activity a) {
		mActivity = a;
	}

	public Activity getActivity() {
		return mActivity;
	}

	@Override
	public void notify(Notification noti) {
		if (noti != null) {
			saveNotify(noti);
			((MainActivity) mActivity).notify(noti);
		}

	}

	@Override
	public void invalidateOptionsMenu() {
		mActivity.invalidateOptionsMenu();
	}

	@Override
	public void onPause() {
		if (mPauseable && iBackgroundService != null) {
			iBackgroundService.cancelTimer();
		}
	}

	@Override
	public void onResume() {
		if (mPauseable && iBackgroundService != null) {
			iBackgroundService.runTimer();
		}
	}

	private boolean isHome = false;

	public boolean isHomePage() {
		return isHome;
	}

	public void swichFragment(Fragment fragment, String tag) {
		FragmentManager fragmentManager = ((FragmentActivity) mActivity)
				.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		fragmentTransaction.replace(R.id.content_frame, fragment, tag).commit();
	}

	@Override
	public void errorPage(Channel c) {
		isHome = false;
		ErrorFragment fragment = new ErrorFragment();
		fragment.setUIController(this);
		Bundle args = new Bundle();
		Message msg = new Message();
		msg.obj = c;
		args.putParcelable(GalleryFragment.MESSAGE_KEY, msg);
		fragment.setArguments(args);
		swichFragment(fragment, "error");
	}

	public void setService(BackgroundServices bds) {
		iBackgroundService = bds;
	}

	private boolean mPauseable = false;

	public void setPauseable(boolean enable) {
		mPauseable = enable;
	}

	@Override
	public void onHomePage() {
		isHome = true;
		Fragment fragment = new MainFragment();
		swichFragment(fragment, "home");
	}

	@Override
	public void onGalleryPage(Channel channel) {
		isHome = false;
		PagerFragment fragment = new PagerFragment();
		fragment.setUIController(this);
		Bundle args = new Bundle();
		args.putBoolean("searchMode", false);
		args.putParcelable(GalleryFragment.MESSAGE_KEY, channel);
		fragment.setArguments(args);
		swichFragment(fragment, "gallery");
	}

	public boolean saveNotify(Notification noti) {
		SharedPreferences preferences = mActivity.getSharedPreferences(
				mActivity.getString(R.string.app_name), Context.MODE_PRIVATE);
		return preferences.edit()
				.putString(Notification.class.getSimpleName(), noti.toString())
				.commit();
	}

	public Notification readNotification() {
		SharedPreferences preferences = mActivity.getSharedPreferences(
				mActivity.getString(R.string.app_name), Context.MODE_PRIVATE);
		String json = preferences.getString(Notification.class.getSimpleName(),
				null);
		if (Empty.isEmpty(json)) {
			return null;
		}
		try {
			JSONObject jo = new JSONObject(json);
			return BackgroundServices.parse(jo);
		} catch (JSONException e) {
			Lg.e(e);
			e.printStackTrace();
		}
		return null;
	}

	public static interface DrawerObserver {
		public boolean isDrawerOpen(View drawer);
	}

	private DrawerObserver mDrawerObserver;

	public boolean isDrawerOpen(View drawer) {
		if (mDrawerObserver != null) {
			return mDrawerObserver.isDrawerOpen(drawer);
		}
		return false;
	}

	public void setDrawerObserver(DrawerObserver d) {
		mDrawerObserver = d;
	}

	@Override
	public void onDrawerClosed() {

	}

	@Override
	public void onDrawerOpened() {

	}

	@SuppressLint("SetJavaScriptEnabled")
	public static void buildWebView(WebView webView) {
		WebSettings webSettings = webView.getSettings();
		// webSettings.setBlockNetworkImage(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDisplayZoomControls(false);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadsImagesAutomatically(true);
	}

	public static void openBrowser(Activity activity, String url) {
		Intent intent = new Intent(activity, BrowserActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		ComponentName componentName = new ComponentName(activity,
				BrowserActivity.class);
		intent.setComponent(componentName);
		intent.setData(Uri.parse(url));
		activity.startActivity(intent);
	}

	private View.OnKeyListener mOnKeyListener;

	public void setOnKeyListener(View v, View.OnKeyListener l) {
		mOnKeyListener = l;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return mOnKeyListener == null ? false : mOnKeyListener.onKey(null,
				keyCode, event);
	}
}
