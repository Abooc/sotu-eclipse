package org.lee.android.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.lee.android.analytics.AlA;
import com.abooc.android.baidupicture.R;

public class AdvActionBarActivity extends AlA {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.update_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.actionbar_main_adv:
			break;
		default:
			break;
		}
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

}