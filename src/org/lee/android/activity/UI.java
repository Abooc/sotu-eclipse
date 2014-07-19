package org.lee.android.activity;

import android.view.KeyEvent;
import android.view.View;

import org.lee.android.activity.BackgroundServices.Notification;
import org.lee.android.app.bean.Channel;

public interface UI {
	void notify(Notification noti);

	void invalidateOptionsMenu();

	void onPause();

	void onResume();

	void onHomePage();

	void onGalleryPage(Channel channel);

	void errorPage(Channel channel);

	void onDrawerClosed();

	void onDrawerOpened();

	public boolean isDrawerOpen(View drawer);

	public boolean onKeyUp(int keyCode, KeyEvent event);

	public void setOnKeyListener(View v, View.OnKeyListener l);

}