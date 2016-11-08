package org.lee.android.analytics;

import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

/**
 * 统计
 * 
 * @author ruiyuLee
 * 
 */
public abstract class AlA extends FragmentActivity {

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		EasyTracker easyTracker = EasyTracker.getInstance(this);
		easyTracker.send(MapBuilder.createEvent("1001", "1010",
				item.getTitle().toString(), (long) item.getItemId()) // Event
																		// value
				.build());
		return super.onOptionsItemSelected(item);
	}

}
