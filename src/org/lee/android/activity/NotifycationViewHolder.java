package org.lee.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import org.lee.android.activity.BackgroundServices.Notification;
import com.abooc.android.baidupicture.R;

public class NotifycationViewHolder {
	private boolean mShowing = false;
	private LinearLayout mContainerLayout;
	private TextView mNameText;
	private ImageView mClearView;
	private TextView mContentText;
	private Animation iEnterAnimation;
	private Animation iExitAnimation;
	private Animation iTimerAnimation;
	@SuppressWarnings("unused")
	private Animation iShakeAnimation;

	private Activity iActivity;
	private Notification iNotify;

	public NotifycationViewHolder(Activity a) {
		iActivity = a;
		installView();
		loadAnimaition();
	}

	private void loadAnimaition() {
		iEnterAnimation = AnimationUtils.loadAnimation(iActivity,
				R.anim.fade_in);
		iExitAnimation = AnimationUtils.loadAnimation(iActivity,
				R.anim.fade_out);
		iTimerAnimation = AnimationUtils.loadAnimation(iActivity,
				R.anim.timer_3x1000);
		iShakeAnimation = AnimationUtils.loadAnimation(iActivity, R.anim.shake);
		iTimerAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				mClearView.setEnabled(false);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mClearView.setEnabled(true);
				mClearView.setImageResource(R.drawable.ic_clear);
			}
		});
		iExitAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mContainerLayout.setVisibility(View.GONE);
			}
		});
	}

	private void installView() {
		mContainerLayout = (LinearLayout) iActivity
				.findViewById(R.id.notifyLayout);
		mNameText = (TextView) iActivity.findViewById(R.id.notifyTitle);
		mClearView = (ImageView) iActivity.findViewById(R.id.clear);
		mContentText = (TextView) iActivity.findViewById(R.id.notifyContent);
		OnClickListener iOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int id = v.getId();
				if (id == R.id.clear) {
					hide();
				} else if (id == R.id.notifyTitle) {
					openNotifi();
				} else {
					// mNameText.startAnimation(iShakeAnimation);
					openNotifi();
				}
			}
		};
		mNameText.setOnClickListener(iOnClickListener);
		mContentText.setOnClickListener(iOnClickListener);
		mClearView.setOnClickListener(iOnClickListener);
	}

	private void openNotifi() {
		EasyTracker easyTracker = EasyTracker.getInstance(iActivity);
		easyTracker.send(MapBuilder.createEvent("通知栏", "进入通知",
				mNameText.getText().toString(), null).build());
		Intent notify = new Intent(iActivity, NotificationInfoActivity.class);
		notify.putExtra(NotificationInfoActivity.INTENT_KEY_N, iNotify);
		iActivity.startActivity(notify);
	}

	boolean isEnable() {
		return iNotify != null;
	}

	void show() {
		mShowing = true;
		mNameText.setTag(iNotify);
		mNameText.setText(iNotify.name + ":");
		mContentText.setText(iNotify.content);
		mContainerLayout.setVisibility(View.VISIBLE);
		mContainerLayout.startAnimation(iEnterAnimation);
		mContentText.requestFocus();
		mClearView.startAnimation(iTimerAnimation);
	}

	void setNotify(Notification noti) {
		iNotify = noti;
	}

	private void hide() {
		mContainerLayout.startAnimation(iExitAnimation);
		mShowing = false;
	}

	@SuppressWarnings("unused")
	private boolean isShowing() {
		return mShowing;
	}
}